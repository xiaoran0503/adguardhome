package org.fordes.adg.rule.handler;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 规则源：内容流 + 该内容对应的字符集。
 * <p>
 * 字符集随返回值传递，而不是存放在 handler 的成员变量上，
 * 避免单例 handler 被线程池并发调用时字符集互相覆盖。
 *
 * @param stream  规则内容流
 * @param charset 该内容使用的字符集
 */
public record RuleSource(InputStream stream, Charset charset) {
}
