package org.fordes.adg.rule.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.fordes.adg.rule.config.OutputConfig;
import org.fordes.adg.rule.config.RuleConfig;
import org.fordes.adg.rule.constant.Constants;
import org.fordes.adg.rule.enums.HandleType;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 同步 README 中的「规则订阅」与「上游列表」区块。
 * <p>
 * 每次运行结束都会按当前配置与规则输出文件重写标记之间的内容，CI 提交时一并更新，
 * 因此 README 里的更新时间、规则数量、订阅链接与上游列表始终与仓库实际内容一致。
 *
 * @author fordes123
 */
@Slf4j
public class ReadmeSync {

    private static final String README = Constants.ROOT_PATH + File.separator + "README.md";

    /**
     * 自动生成区块的边界标记，手工内容不会被覆盖
     */
    private static final String RULES_BEGIN = "<!-- RULES:BEGIN -->";
    private static final String RULES_END = "<!-- RULES:END -->";
    private static final String UPSTREAM_BEGIN = "<!-- UPSTREAM:BEGIN -->";
    private static final String UPSTREAM_END = "<!-- UPSTREAM:END -->";

    private static final String REPO_RAW_BASE =
            "https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/";

    private static final String PROXY_PREFIX = "https://gh-proxy.com/";

    private static final String WHITELIST_FILE = "private.txt";

    private static final Map<String, String> FILE_LABEL = Map.of(
            "all.txt", "全部规则",
            "dns.txt", "DNS 规则",
            "hosts.txt", "Hosts 规则",
            "regex.txt", "正则规则",
            "domain.txt", "域名规则",
            "modify.txt", "修饰规则");

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void sync(OutputConfig outputConfig, RuleConfig ruleConfig) {
        try {
            File readme = FileUtil.file(README);
            if (!FileUtil.exist(readme)) {
                log.warn("README 不存在，跳过同步 => {}", README);
                return;
            }
            String origin = FileUtil.readString(readme, StandardCharsets.UTF_8);
            String updated = replaceBlock(origin, RULES_BEGIN, RULES_END, buildRulesBlock(outputConfig));
            updated = replaceBlock(updated, UPSTREAM_BEGIN, UPSTREAM_END, buildUpstreamBlock(ruleConfig));

            if (!updated.equals(origin)) {
                FileUtil.writeString(updated, readme, StandardCharsets.UTF_8);
                log.info("README 已同步规则订阅与上游列表");
            }
        } catch (Exception e) {
            log.error("README 同步失败 => {}", e.getMessage());
        }
    }

    /**
     * 替换两个标记之间的内容，标记缺失时保持原样
     */
    private static String replaceBlock(String text, String begin, String end, String content) {
        int start = text.indexOf(begin);
        int stop = text.indexOf(end);
        if (start < 0 || stop < start) {
            log.warn("README 缺少标记 {} / {}，跳过该区块", begin, end);
            return text;
        }
        return text.substring(0, start + begin.length()) + "\n" + content + "\n" + text.substring(stop);
    }

    private static String buildRulesBlock(OutputConfig outputConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("```\n");
        sb.append("更新时间: ")
                .append(ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).format(TIME_FORMAT))
                .append(" （北京时间）\n\n");
        sb.append("拦截规则数量: ").append(countRules(outputConfig.getPath(), "all.txt")).append('\n');
        sb.append("白名单规则数量: ").append(countRules(Constants.LOCAL_RULE_SUFFIX, WHITELIST_FILE)).append('\n');
        sb.append("```\n\n");

        sb.append("<details open>\n<summary>规则列表</summary>\n<ul>\n\n");
        outputConfig.getFiles().keySet().forEach(fileName -> {
            String label = FILE_LABEL.getOrDefault(fileName, fileName);
            String url = REPO_RAW_BASE + fileName;
            sb.append("- **[").append(label).append("（GitHub）](").append(url).append(")**\n");
            sb.append("- **[").append(label).append("（gh-proxy 加速）](")
                    .append(PROXY_PREFIX).append(url).append(")**\n");
        });
        sb.append("\n</ul>\n</details>");
        return sb.toString();
    }

    private static String buildUpstreamBlock(RuleConfig ruleConfig) {
        Collection<RuleConfig.Prop> remote = ruleConfig.getRuleMap()
                .getOrDefault(HandleType.REMOTE, List.of());
        Collection<RuleConfig.Prop> local = ruleConfig.getRuleMap()
                .getOrDefault(HandleType.LOCAL, List.of());

        StringBuilder sb = new StringBuilder();
        sb.append("| 规则源 | 地址 |\n| --- | --- |\n");
        remote.forEach(prop -> sb.append("| ").append(prop.getName())
                .append(" | ").append(prop.getPath()).append(" |\n"));
        local.forEach(prop -> sb.append("| ").append(prop.getName())
                .append(" | `").append(relative(prop.getPath())).append("` |\n"));
        return sb.toString().stripTrailing();
    }

    /**
     * 本地规则的 path 在绑定配置时被补成绝对路径，展示时还原成仓库内相对路径
     */
    private static String relative(String path) {
        return StrUtil.removePrefix(path, Constants.ROOT_PATH + File.separator);
    }

    /**
     * 统计规则文件条目数，注释、文件头与分段标题不计入
     */
    private static long countRules(String dir, String fileName) {
        String parent = FileUtil.isAbsolutePath(dir) ? dir : Constants.ROOT_PATH + File.separator + dir;
        File file = new File(parent, fileName);
        if (!FileUtil.exist(file)) {
            return 0;
        }
        return FileUtil.readLines(file, StandardCharsets.UTF_8).stream()
                .filter(StrUtil::isNotBlank)
                // 文件头、注释与分段标题不计入规则数量
                .filter(line -> !ReUtil.contains(Constants.EFFICIENT_REGEX, line.trim()))
                .count();
    }
}
