package org.fordes.adg.rule.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author fordes123 on 2022/9/19
 */
@Getter
@AllArgsConstructor
public enum RuleType {

    /**
     * 例外（白名单）规则，形如 @@||xxx.com^
     * <p>AdGuard Home 只认这一种放行写法，且必须出现在 all.txt / dns.txt 里才有意义；
     * 单独成类是为了别把它混进 regex.txt / domain.txt / modify.txt，污染纯列表文件。
     */
    WHITELIST("例外规则", true, List.of("@@"),
            List.of("^@@\\|\\|[^*/]+\\^?$"), Collections.emptyList()),

    /**
     * 域名规则，形如 xxx.com、xx.oo.com
     */
    DOMAIN("域名规则", true, List.of(StrUtil.DOT),
            List.of("^[\\u4e00-\\u9fa5,a-z,A-Z,0-9][\\u4e00-\\u9fa5,a-z,A-Z,0-9,\\-,\\.]*[\\u4e00-\\u9fa5,a-z,A-Z,0-9,\\^]$"), Collections.emptyList()),
    /**
     * Hosts规则
     */
    HOSTS("Hosts规则", true,
            Collections.emptyList(), List.of("^\\d+\\.\\d+\\.\\d+\\.\\d+\\s+.*$"), Collections.emptyList()),

    /**
     * 正则规则，包含修饰规则
     */
    REGEX("正则规则", true, Collections.emptyList(),
            Collections.emptyList(), List.of("[/,#,&,=,:]", "^[\\*,@,\\-,_,\\.,&,\\?]", "[\\$][^\\s]", "[\\^][^\\s]")),


    /**
     * 修饰规则，不被adGuardHome支持
     */
    MODIFY("修饰规则", false, Collections.emptyList(),
            Collections.emptyList(), List.of("^@@"));


    /**
     * 描述
     */
    private final String desc;

    /**
     * 支持性，true则adGuardHome支持
     */
    private final boolean usually;

    /**
     * 识别标识，包含即通过
     */
    private final Collection<String> identify;

    /**
     * 正向 正则，匹配一个即为通过
     */
    private final Collection<String> match;

    /**
     * 排除 正则，全部不匹配即为通过
     */
    private final Collection<String> exclude;
}
