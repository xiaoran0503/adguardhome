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
     * <p>不只看 {@code ||xxx^} 这种纯域名规则：正则（{@code /...zijieapi\.com/}）、通配
     * （{@code ||ads*-normal-*.zijieapi.com^}）、hosts（{@code 0.0.0.0 xxx.snssdk.com}）都要覆盖，
     * 否则误伤会从这几类写法绕过来。判断按「域名出现在主机名边界上」进行，
     * 因此 {@code ctobsnssdk.com} 不会因为包含 {@code snssdk.com} 而被误判。
     *
     * @param line    原始规则行，用于识别 {@code @@} 例外（例外本身是白名单，不能丢）
     * @param content 清理掉基础修饰符后的规则内容
     * @return true 表示属于误伤，应当丢弃
     */
    public static boolean blocksProtected(String line, String content) {
        if (DOMAINS.isEmpty() || content == null || content.isEmpty()) {
            return false;
        }
        String raw = line == null ? "" : line.trim();
        if (raw.startsWith("@@")) {
            return false;
        }
        //正则里的 \. 会拆开域名，去掉反斜杠后再比
        String text = content.toLowerCase(Locale.ROOT).replace("\\", "");
        for (String domain : DOMAINS) {
            int from = 0;
            while (true) {
                int idx = text.indexOf(domain, from);
                if (idx < 0) {
                    break;
                }
                int end = idx + domain.length();
                if ((idx == 0 || !isHostChar(text.charAt(idx - 1)))
                        && (end >= text.length() || !isHostChar(text.charAt(end)))) {
                    return true;
                }
                from = idx + 1;
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

    private static boolean isHostChar(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_';
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
