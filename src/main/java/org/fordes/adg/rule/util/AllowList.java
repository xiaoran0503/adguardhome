package org.fordes.adg.rule.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 本地白名单（误杀放行清单）。
 * <p>
 * 数据来自项目 {@code rule} 目录下的 {@code local-rule.txt} 与 {@code private.txt}：只取其中
 * 的 {@code @@||xxx^} 例外行，解析成域名清单。合并阶段凡是指向这些域名的<b>拦截规则</b>都会被
 * 丢弃，同时例外行本身照常写进 all.txt / dns.txt，客户端即使还开着别的过滤器也不会误伤。
 * <p>
 * 与 {@link ProtectDomains} 的区别：保护清单是「整域级别、由项目维护」的硬性放行，
 * 白名单则是「用户自己维护」的放行，两者判断方式一致（主机名边界匹配）。
 *
 * @author fordes123
 */
@Slf4j
public class AllowList {

    private static volatile Set<String> domains = Collections.emptySet();

    private AllowList() {
    }

    /**
     * 加载白名单文件，文件不存在时忽略
     *
     * @param dir       白名单文件所在目录
     * @param fileNames 文件名，按顺序读取
     */
    public static void load(String dir, String... fileNames) {
        Set<String> set = new LinkedHashSet<>();
        for (String fileName : fileNames) {
            File file = new File(dir, fileName);
            if (!FileUtil.exist(file)) {
                log.info("白名单文件 {} 不存在，跳过", fileName);
                continue;
            }
            int before = set.size();
            set.addAll(parse(file));
            log.info("白名单文件 {} 载入 {} 条", fileName, set.size() - before);
        }
        domains = Collections.unmodifiableSet(set);
        log.info("已加载本地白名单域名 {} 个", domains.size());
    }

    /**
     * 原始规则行是否命中本地白名单
     *
     * @param line    原始规则行，{@code @@} 开头的是例外本身，不参与判断
     * @param content 清理掉基础修饰符后的规则内容
     * @return true 表示应当丢弃该拦截规则
     */
    public static boolean isWhitelisted(String line, String content) {
        if (domains.isEmpty()) {
            return false;
        }
        String raw = line == null ? "" : line.trim();
        if (raw.startsWith("@@")) {
            return false;
        }
        return ProtectDomains.matchesAny(domains, content);
    }

    /**
     * 白名单域名清单
     */
    public static Set<String> entries() {
        return domains;
    }

    /**
     * 解析文件中的例外规则，形如 {@code @@||xxx.com^} / {@code @@||xxx.com^$important}
     * <p>带路径、带通配的例外 AdGuard Home 并不支持，直接跳过。
     */
    private static Set<String> parse(File file) {
        Set<String> set = new LinkedHashSet<>();
        for (String raw : FileUtil.readLines(file, StandardCharsets.UTF_8)) {
            String line = raw.trim();
            if (!line.startsWith("@@")) {
                continue;
            }
            String domain = line.substring(2);
            if (domain.startsWith("||")) {
                domain = domain.substring(2);
            }
            int end = domain.length();
            for (int i = 0; i < domain.length(); i++) {
                char c = domain.charAt(i);
                if (c == '^' || c == '$' || c == '/' || c == ' ' || c == '|') {
                    end = i;
                    break;
                }
            }
            domain = domain.substring(0, end).trim().toLowerCase(Locale.ROOT);
            if (!domain.isEmpty() && domain.indexOf('*') < 0 && domain.indexOf('.') > 0) {
                set.add(domain);
            }
        }
        return set;
    }
}
