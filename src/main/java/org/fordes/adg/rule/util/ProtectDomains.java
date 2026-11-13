package org.fordes.adg.rule.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 保护域名（安全白名单）。
 * <p>
 * 清单来自 classpath 下的 {@code protect-domains.txt}，一行一个域名，写 {@code snssdk.com}
 * 即保护该域及其全部子域。合并阶段凡是命中清单的<b>拦截规则</b>都会被丢弃，这些域名随后还会
 * 以 {@code @@} 例外写入 AGH 可用的输出文件，避免客户端上的其它过滤器把它们挡掉。
 * <p>
 * 目的很明确：DNS 层最容易踩的坑是把 App 的业务 API 当广告拦掉，用户看到的现象不是打不开，
 * 而是加载极慢（客户端在超时重试）。宁可少拦几条统计域，也不牺牲可用性。
 *
 * @author fordes123
 */
@Slf4j
public class ProtectDomains {

    private static final String RESOURCE = "protect-domains.txt";

    private static final Set<String> DOMAINS = load();

    private ProtectDomains() {
    }

    /**
     * 原始规则行是否指向受保护域名
     *
     * @param line    原始规则行，用于识别 {@code @@} 例外（例外本身是白名单，不能丢）
     * @param content 清理掉基础修饰符后的规则内容
     * @return true 表示属于误伤，应当丢弃
     */
    public static boolean blocksProtected(String line, String content) {
        if (DOMAINS.isEmpty()) {
            return false;
        }
        String raw = line == null ? "" : line.trim();
        if (raw.startsWith("@@")) {
            return false;
        }
        String host = hostOf(content);
        if (host.isEmpty() || host.indexOf('*') >= 0) {
            return false;
        }
        for (String domain : DOMAINS) {
            if (host.equals(domain) || host.endsWith("." + domain)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 受保护域名清单
     */
    public static Set<String> entries() {
        return DOMAINS;
    }

    /**
     * 从规则内容里取域名：规则可能带 {@code ^}、{@code $修饰}、路径或 hosts 的地址部分
     */
    private static String hostOf(String content) {
        if (content == null) {
            return "";
        }
        int end = content.length();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '^' || c == '$' || c == '/' || c == ' ' || c == '|' || c == '#') {
                end = i;
                break;
            }
        }
        return content.substring(0, end).trim().toLowerCase(Locale.ROOT);
    }

    private static Set<String> load() {
        Set<String> domains = new LinkedHashSet<>();
        try {
            byte[] bytes = IoUtil.readBytes(new ClassPathResource(RESOURCE).getStream());
            String text = new String(bytes, StandardCharsets.UTF_8);
            for (String raw : text.split("\n")) {
                String item = raw.trim();
                if (item.isEmpty() || item.startsWith("#")) {
                    continue;
                }
                domains.add(item.toLowerCase(Locale.ROOT));
            }
            log.info("已加载保护域名 {} 个", domains.size());
        } catch (Exception e) {
            log.warn("未找到或无法读取保护域名清单 {}，本次不做误伤过滤 => {}", RESOURCE, e.getMessage());
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(domains);
    }
}
