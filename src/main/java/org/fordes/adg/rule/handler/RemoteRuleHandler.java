package org.fordes.adg.rule.handler;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.fordes.adg.rule.config.FilterConfig;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RemoteRuleHandler extends RuleHandler {

    public RemoteRuleHandler(FilterConfig config, BitSetBloomFilter filter) {
        super(config, filter);
    }

    @Override
    RuleSource open(String ruleUrl) {
        try {
            HttpResponse response = HttpRequest.get(ruleUrl)
                    .setFollowRedirects(true)
                    .timeout(20000)
                    .execute();
            if (response.isOk()) {
                return new RuleSource(response.bodyStream(), Charset.forName(response.charset()));
            }
            log.error(" 规则<{}> 获取失败 => response status: {}", ruleUrl, response.getStatus());
        } catch (Exception e) {
            log.error(" 规则<{}> 获取失败 => {}", ruleUrl, e.getMessage());
            log.debug(ExceptionUtil.stacktraceToString(e));
        }
        return new RuleSource(IoUtil.toStream(new byte[0]), StandardCharsets.UTF_8);
    }
}
