<div align="center">
<h1 align="center">TheBestAdrules<br></h1>
  <p>TheBestAdrules，适用于Adguard Home的去广告dns规则，由关圣整理上游优秀的规则，合并去重而来，集百家之所长，取其精华去其糟粕。 
关圣DNS官网https://dns1.top 官方微信群及QQ群见官网公布。 
由于目前网络上发布的规则众多，逐个添加难免有重复的，而且不方便维护，因此创建此仓库，方便关圣云更新dns规则，也可以方便其他自建dns的小伙伴一键导入。
  </p>
  <p>本仓库是 <a href="https://github.com/guandasheng/TheBestAdrules">guandasheng/TheBestAdrules</a> 的维护分支，规则由 GitHub Actions 每 8 小时自动合并、去重并提交。 
  </p>
</h1>
<p>
  <a href="https://github.com/xiaoran0503/adguardhome">
    <img src="https://img.shields.io/github/last-commit/xiaoran0503/adguardhome?style=flat-square" alt="last update" />
  </a>
  <a href="https://github.com/xiaoran0503/adguardhome">
    <img src="https://img.shields.io/github/forks/xiaoran0503/adguardhome?style=flat-square" alt="forks" />
  </a>
  <a href="https://github.com/xiaoran0503/adguardhome">
    <img src="https://img.shields.io/github/stars/xiaoran0503/adguardhome?style=flat-square" alt="stars" />
  </a>
  <a href="https://github.com/xiaoran0503/adguardhome/issues/">
    <img src="https://img.shields.io/github/issues/xiaoran0503/adguardhome?style=flat-square" alt="open issues" />
  </a>
  <a href="https://github.com/xiaoran0503/adguardhome">
    <img src="https://img.shields.io/github/license/xiaoran0503/adguardhome?style=flat-square" alt="license" />
  </a>
</p>

<h4>
    <a href="#a">规则订阅</a>
  <span> · </span>
    <a href="#b">上游列表</a>
  <span> · </span>
    <a href="#c">拦截效果</a>
  <span> · </span>
    <a href="#d">更新日志</a>
  <span> · </span>
    <a href="#e">完善项目</a>
  </h4>

</div>

<h2 id="a">🎯 规则订阅</h2>

<!-- RULES:BEGIN -->
```
更新时间: 2026-11-12 18:21:56 （北京时间）

拦截规则数量: 636990
白名单规则数量: 1031
```

<details open>
<summary>规则列表</summary>
<ul>

- **[全部规则（GitHub）](https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/all.txt)**
- **[全部规则（gh-proxy 加速）](https://gh-proxy.com/https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/all.txt)**
- **[DNS 规则（GitHub）](https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/dns.txt)**
- **[DNS 规则（gh-proxy 加速）](https://gh-proxy.com/https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/dns.txt)**
- **[Hosts 规则（GitHub）](https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/hosts.txt)**
- **[Hosts 规则（gh-proxy 加速）](https://gh-proxy.com/https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/hosts.txt)**
- **[正则规则（GitHub）](https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/regex.txt)**
- **[正则规则（gh-proxy 加速）](https://gh-proxy.com/https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/regex.txt)**
- **[域名规则（GitHub）](https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/domain.txt)**
- **[域名规则（gh-proxy 加速）](https://gh-proxy.com/https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/domain.txt)**
- **[修饰规则（GitHub）](https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/modify.txt)**
- **[修饰规则（gh-proxy 加速）](https://gh-proxy.com/https://raw.githubusercontent.com/xiaoran0503/adguardhome/main/rule/modify.txt)**

</ul>
</details>
<!-- RULES:END -->

> 以上区块由程序在每次规则合并后自动重写，请勿手工修改。

<h2 id="b">📚 上游列表</h2>

<!-- UPSTREAM:BEGIN -->
| 规则源 | 地址 |
| --- | --- |
| GOODBYEADS DNS 规则 | https://raw.githubusercontent.com/8680/GOODBYEADS/master/data/rules/dns.txt |
| adrules.top DNS 规则 | https://adrules.top/dns.txt |
| anti-ad easylist | https://anti-ad.net/easylist.txt |
| Potterli20 加速 Hosts | https://github.com/Potterli20/file/releases/download/github-hosts/Accelerate-Hosts.txt |
| AWAvenue 秋风广告规则 | https://raw.githubusercontent.com/TG-Twilight/AWAvenue-Ads-Rule/main/AWAvenue-Ads-Rule.txt |
| 10007 专属规则 | https://raw.githubusercontent.com/lingeringsound/10007_auto/master/reward |
| 5whys AdGuardHome-rules | https://raw.githubusercontent.com/5whys-adblock/AdGuardHome-rules/main/rules/output_full.txt |
| 217heidai adblockfilters | https://raw.githubusercontent.com/217heidai/adblockfilters/main/rules/adblockdns.txt |
| 本地私有规则 | `rule/local-rule.txt` |
<!-- UPSTREAM:END -->

> 以上区块同样由程序自动重写，与 `application.yml` 中的规则源配置保持一致。

<h2 id="c">🚫 拦截效果</h2>

[AdBlock Tester](https://adblock-tester.com)

[Block Ads! Adblock test](https://blockads.fivefilters.org/)

[Ad Blocker Test](https://d3ward.github.io/toolz/adblock.html)

<h2 id="d">📝 更新日志</h2>

### 2026-11-12 技术栈现代化 & README 自动化

**构建环境**

- JDK 21 → **JDK 27**（Spring Boot 4.1.2 官方兼容上限即 Java 27），编译目标同步为 27
- GitHub Actions 全部升到最新：`checkout@v7`、`setup-java@v6`、`git-auto-commit-action@v7.2.1`、`github-push-action@v1.3.0`、`delete-workflow-runs@v2.1.0`

**依赖**

- Spring Boot 3.1.3 → **4.1.2**（最新稳定版，未采用 4.2.0-RC）
- hutool 5.8.24 → **5.8.48**
- lombok 钉到 **1.18.48**（Boot BOM 管理的版本在 JDK 21+ 的 javac 下不可用）

**构建插件**

- maven-compiler-plugin **3.16.0**、maven-resources-plugin **3.5.0**、maven-jar-plugin **3.5.1**、maven-surefire-plugin **3.6.1**
- 另显式管理 maven-clean-plugin **3.5.0**、maven-install-plugin **3.2.0**、maven-deploy-plugin **3.2.0**、maven-site-plugin **3.22.0**
- 编译参数由 `source/target + -parameters` 改为 `<release>` + `<parameters>`

**兼容性适配**

- hutool 5.8.4x 起 `BitSetBloomFilter` 限定 `hashFunctionNumber ∈ [1,8]`，本工程按 200 万/0.001 推算的理论值为 10，已对 k 做钳位
- Spring Framework 7 起弃用 `org.springframework.lang.NonNull`，相关注解已移除

**缺陷修复**

- 远程规则处理把字符集存在单例 handler 的成员变量上，并发处理多条规则时会互相覆盖 → 改为 `RuleSource`（流 + 字符集）随返回值传递
- 写文件锁在 `tryLock` 失败后 `sleep` 递归重试，存在递归风险 → 改为阻塞等待
- 规则输出流只 `flush` 不 `close` → 处理完成后统一关闭
- 规则源打不开（如缺少本地规则文件）时会抛到异步未捕获异常处理器 → 记录原因后跳过

**自动化**

- README 的「规则订阅」「上游列表」两个区块改由程序在每次运行结束时自动重写：更新时间、规则条数、订阅链接、上游源列表始终与仓库实际内容一致

**规则源**

- GOODBYEADS 上游仓库改版，由 `rules.txt` 改为引用 `data/rules/dns.txt`（其 README 标注为「AdGuard Home DNS拦截规则」）
- 各规则源名称按实际来源重命名（此前全部叫 GOODBYEADS，日志与统计无法区分）

### 更早

- 2024-01 及以前：沿用上游 `guandasheng/TheBestAdrules` 的规则与 README 结构

<h2 id="e">💬 完善项目</h2>

规则有问题可以在本仓库提交 Issue，或前往上游项目 <a href="https://github.com/8680/GOODBYEADS">GOODBYEADS</a>、<a href="https://github.com/guandasheng/TheBestAdrules">TheBestAdrules</a> 反馈。
审核之后会加入到规则，如果规则有误杀会尽快处理。

**提交范围**

- 漏拦截的广告
- 误杀的网站
