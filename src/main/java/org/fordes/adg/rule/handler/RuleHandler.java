package org.fordes.adg.rule.handler;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fordes.adg.rule.config.FilterConfig;
import org.fordes.adg.rule.config.RuleConfig;
import org.fordes.adg.rule.enums.HandleType;
import org.fordes.adg.rule.enums.RuleType;
import org.fordes.adg.rule.util.RuleUtil;
import org.fordes.adg.rule.util.SpringUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 规则处理线程抽象
 *
 * @author ChengFengsheng on 2022/7/7
 */
@Slf4j
@AllArgsConstructor
public abstract class RuleHandler {

    private final FilterConfig config;

    private final BitSetBloomFilter bloomFilter;

    /**
     * 打开规则源
     *
     * @param path 规则地址或本地路径
     * @return 内容流及其字符集
     */
    abstract RuleSource open(String path);

    public void handle(RuleConfig.Prop prop, final Map<RuleType, Set<BufferedOutputStream>> typeStreamMap) {

        TimeInterval interval = DateUtil.timer();
        AtomicInteger invalid = new AtomicInteger();
        AtomicInteger repeat = new AtomicInteger();

        Map<RuleType, Set<String>> dataMap = MapUtil.newHashMap();
        final RuleSource source = openOrEmpty(prop);
        //按行读取并处理
        try (BufferedReader reader = IoUtil.getReader(source.stream(), source.charset())) {
            IoUtil.readLines(reader, (LineHandler) line -> {
                String content = RuleUtil.clearRule(line);
                if (StrUtil.isEmpty(content)) {
                    invalid.incrementAndGet();
                } else if (bloomFilter.contains(content)) {
                    repeat.incrementAndGet();
                } else {
                    bloomFilter.add(content);
                    if (content.length() < config.getWarnLimit()) {
                        log.warn("发现短规则: {} -> {}，来源: {}", line, content, prop.getName());
                    }

                    Arrays.stream(RuleType.values()).filter(ruleType -> RuleUtil.validRule(content, ruleType))
                            .findFirst().ifPresent(ruleType -> RuleUtil.safePut(dataMap, ruleType, line));

                    if (dataMap.values().stream().mapToInt(Set::size).sum() > config.getBatchSize()) {
                        flush(dataMap, typeStreamMap, prop, source.charset());
                    }
                }
            });
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        } finally {
            flush(dataMap, typeStreamMap, prop, source.charset());
            log.info("规则<{}> 耗时 => {} ms 无效数 => {} 重复数 => {}",
                    prop.getName(), interval.intervalMs(), invalid.get(), repeat.get());
        }
    }

    /**
     * 打开规则源，失败时记录原因并返回空流（如本地规则文件缺失），不让异常逃逸成异步方法的未捕获异常
     */
    private RuleSource openOrEmpty(RuleConfig.Prop prop) {
        try {
            return open(prop.getPath());
        } catch (Exception e) {
            log.error("规则<{}> 打开失败 => {}", prop.getName(), e.getMessage());
            return new RuleSource(IoUtil.toStream(new byte[0]), StandardCharsets.UTF_8);
        }
    }

    /**
     * 把缓冲区内容写入各输出文件并清空
     */
    private static void flush(Map<RuleType, Set<String>> dataMap,
                              Map<RuleType, Set<BufferedOutputStream>> typeStreamMap,
                              RuleConfig.Prop prop, Charset charset) {
        dataMap.forEach((type, data) -> typeStreamMap.getOrDefault(type, Collections.emptySet())
                .forEach(stream -> RuleUtil.writeToStream(stream, data, prop, charset)));
        dataMap.clear();
    }

    public static RuleHandler getHandler(HandleType handleType) {
        return switch (handleType) {
            case LOCAL -> SpringUtil.getBean(LocalRuleHandler.class);
            case REMOTE -> SpringUtil.getBean(RemoteRuleHandler.class);
        };
    }
}
