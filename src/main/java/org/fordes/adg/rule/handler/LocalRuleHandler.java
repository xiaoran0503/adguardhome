package org.fordes.adg.rule.handler;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.core.io.FileUtil;
import org.fordes.adg.rule.config.FilterConfig;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 本地规则处理
 *
 * @author ChengFengsheng on 2022/7/7
 */
@Component
public class LocalRuleHandler extends RuleHandler {

    public LocalRuleHandler(FilterConfig config, BitSetBloomFilter filter) {
        super(config, filter);
    }

    @Override
    RuleSource open(String rulePath) {
        return new RuleSource(FileUtil.getInputStream(rulePath), Charset.defaultCharset());
    }
}
