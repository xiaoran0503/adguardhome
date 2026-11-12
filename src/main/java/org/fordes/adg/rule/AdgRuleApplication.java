package org.fordes.adg.rule;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fordes.adg.rule.config.OutputConfig;
import org.fordes.adg.rule.config.RuleConfig;
import org.fordes.adg.rule.enums.RuleType;
import org.fordes.adg.rule.handler.RuleHandler;
import org.fordes.adg.rule.util.ReadmeSync;
import org.fordes.adg.rule.util.RuleUtil;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class AdgRuleApplication {

    private final RuleConfig ruleConfig;

    private final OutputConfig outputConfig;

    private final ThreadPoolTaskExecutor ruleExecutor;


    public static void main(String[] args) {
        SpringApplication.run(AdgRuleApplication.class, args);
    }

    @Bean
    ApplicationRunner ruleRunner() {
        return args -> {
            if (ruleConfig.isEmpty() || outputConfig.isEmpty()) {
                log.warn("未配置规则或输出文件");
                return;
            }
            //建立文件
            Map<String, BufferedOutputStream> fileStream = outputConfig.getFiles().keySet().stream()
                    .map(fileName -> Map.entry(fileName,
                            RuleUtil.createFile(outputConfig.getPath(), fileName, outputConfig.getFileHeader())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Map<RuleType, Set<BufferedOutputStream>> typeStreamMap = new ConcurrentHashMap<>();
            outputConfig.getFiles().forEach((fileName, types) ->
                    types.forEach(type -> RuleUtil.safePut(typeStreamMap, type, fileStream.get(fileName))));

            //并发处理规则源，等全部完成后收尾，保证文件写满且 README 统计的是最终结果
            List<CompletableFuture<Void>> tasks = new ArrayList<>();
            ruleConfig.getRuleMap().forEach((type, props) -> props.forEach(prop ->
                    tasks.add(CompletableFuture.runAsync(
                            () -> RuleHandler.getHandler(type).handle(prop, typeStreamMap), ruleExecutor))));
            CompletableFuture<?>[] futures = tasks.toArray(new CompletableFuture[0]);
            CompletableFuture.allOf(futures).join();

            //关闭输出流，避免句柄泄漏
            fileStream.values().forEach(stream -> IoUtil.close(stream));

            //同步 README 中的规则订阅与上游列表
            ReadmeSync.sync(outputConfig, ruleConfig);
            log.info("规则合并完成，共处理 {} 个规则源", tasks.size());
        };
    }
}
