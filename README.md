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
    <a href="#f">误伤防护</a>
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
更新时间: 2026-11-13 11:13:55 （北京时间）

拦截规则数量: 288175
白名单规则数量: 190
保护域名数量: 24
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
| AdGuard DNS filter | https://adguardteam.github.io/AdGuardSDNSFilter/Filters/filter.txt |
| anti-AD 中文规则 | https://anti-ad.net/easylist.txt |
| OISD small | https://small.oisd.nl/ |
| HaGeZi Multi PRO mini | https://raw.githubusercontent.com/hagezi/dns-blocklists/main/adblock/pro.mini.txt |
| AWAvenue 秋风广告规则 | https://raw.githubusercontent.com/TG-Twilight/AWAvenue-Ads-Rule/main/AWAvenue-Ads-Rule.txt |
| 本地私有白名单 | `rule/local-rule.txt` |
<!-- UPSTREAM:END -->

> 以上区块同样由程序自动重写，与 `application.yml` 中的规则源配置保持一致。

<h2 id="f">🛡 误伤防护</h2>

误伤治理分两层，都在合并阶段自动生效：

**一、保护域名（项目维护）** [`src/main/resources/protect-domains.txt`](src/main/resources/protect-domains.txt)

- 凡是**拦截规则**指向清单内域名（含其子域）的，一律在合并阶段丢弃，不计入规则总量
- 清单内域名同时以 `@@` 例外（allowlist）写进 `all.txt` / `dns.txt` 末尾，这样即使客户端另外还启用了 AdGuard DNS filter 之类的过滤器，核心业务也不会被挡

默认只保护**拦了会坏功能**的域名：抖音主站与资源分发（`douyin.com`、`douyinpic.com`、`douyinvod.com`、`douyinstatic.com`、`iesdouyin.com`）、小红书根域（`store`／`t2` 等商城与接口在其下）、以及 Apple／Microsoft 登录核心。

刻意**不**保护的：字节埋点根域（`snssdk.com`／`zijieapi.com`／`amemv.com`／`volces.com`／`ctobsnssdk.com`）、vivo 的 `*-stsdk.vivo.com.cn` 上报集群、淘宝 `h-adashx.ut.taobao.com`、`googleapis.com` 的 analytics/admob 子域——这些拦了只是少一批日志，不影响功能。实测核心源在这些根域下拦的也恰好全是 `log`/`mon`/`applog`/`mssdk`/`ads3`/`ads5` 这类上报子域，真正的业务接口（`i.snssdk.com`、`aweme.snssdk.com`、`ecom.snssdk.com`）本来就没被拦，所以根域整体保护反而会白白放过一批该拦的埋点。

**二、本地白名单（自行维护）** [`rule/local-rule.txt`](rule/local-rule.txt) 与 [`rule/private.txt`](rule/private.txt)

- 用 `@@||域名^` 写放行，合并时解析这些例外：指向它们的拦截规则同样被丢弃，例外行本身写进 `all.txt` / `dns.txt`
- 专门对付「广告和业务共用同一批域名」的场景：抖音、微信、淘宝这类 App 的埋点、广告位和业务 API 常常同域，整域拦掉就会出现白图、发不出语音、支付失败
- 已预置微信图片与登录链路、搜狗输入法、一键登录、各大电商主 API 网关、应用商店等，按需增删

自定义方式：一行一个域名（写 `snssdk.com` 即保护该域及其全部子域），`#` 开头为注释，提交后下一次 CI 编译即生效。

**为什么需要这一层**：DNS 规则量一大，最容易踩的坑是把 App 的业务 API 当广告拦掉。抖音的评论、商城接口都在 `snssdk.com` 域下，一旦被拦，表现不是「打不开」而是**加载极慢**——客户端在超时重试。这一层用「品牌根域 + 少量业务接口」兜底，宁可少拦几条统计域，也不牺牲可用性。

**遇到误伤怎么处理**：在 AdGuard Home 的查询日志里找到被拦的域名（拦截列有值），确认它属于业务主链路后，在 `rule/local-rule.txt` 加一行 `@@||该域名^`，提交等下一次 CI 即可，不需要回退整份规则。

<h2 id="c">🚫 拦截效果</h2>

[AdBlock Tester](https://adblock-tester.com)

[Block Ads! Adblock test](https://blockads.fivefilters.org/)

[Ad Blocker Test](https://d3ward.github.io/toolz/adblock.html)

<h2 id="d">📝 更新日志</h2>

### 2026-11-13 白名单接入 & 上游补强

**上游（三源结构）**

- 核心源：**217heidai/adblockfilters** 的成品 DNS 列表（GitHub Releases 分发，约 25.6 万条，其中 200 条是它自己维护的例外）。它已经把 AdGuard DNS filter、OISD、HaGeZi PRO mini、EasyList / EasyPrivacy、CJX、jiekouAD 等合并去重，并做过域名库校验与父域收敛，等于替我们完成了多源交叉验证；它淘汰掉的那几个（anti-AD 的 `adguard.txt`、yhosts、1Hosts Lite、Hblock）也是因为误杀记录，这部分判断直接沿用
- 叠加 **anti-AD**（`anti-ad-easylist.txt`）：中文站点命中率仍是最强的一档，保留不删
- 叠加 **AWAvenue 秋风广告规则**：补国内 App 与网页广告位
- 移除独立挂载的 AdGuard DNS filter / OISD / HaGeZi：内容已在核心源内，重复挂载只增加体积、不提高命中

**白名单接入（本轮重点）**

- `@@` 例外提升为独立规则类型 `WHITELIST`：此前它会被当成 REGEX/MODIFY 写进 `regex.txt`、`modify.txt`，导致 `dns.txt` 里的例外残缺、`$important` 结尾的例外根本进不了 `dns.txt`；现在例外只写 `all.txt` / `dns.txt`，纯列表文件保持干净
- 新增 `rule/local-rule.txt`（本地私有白名单）：预置微信图片与登录链路、搜狗输入法、一键登录、淘宝/京东/支付宝/拼多多/B站/快手/美团/高德主 API 网关、Google Play 与 App Store 等业务域名；核心源里命中的 `wxs.qq.com`、`apd-pcdnwx*`、`mobileservice.cn`、`shouji.sogou.com` 等全由它放行
- 首次接入仓库里原有的 `rule/private.txt`（1031 行）：它一直存在但**从未参与合并**，README 里「白名单规则数量」统计的是它，实际却不起作用；现在它与 `local-rule.txt` 一并被解析
- 新增 `AllowList`：合并阶段把指向白名单域名的拦截规则直接丢弃，输出里不再出现「一边拦一边放」的自相矛盾条目，日志新增「白名单丢弃 => N」
- 保护清单按「只保护拦了会坏功能的域」重建：移除 `snssdk.com`、`zijieapi.com`、`amemv.com`、`volces.com`、`stsdk.vivo.com.cn`、`h-adashx.ut.taobao.com`、`weixin.qq.com`、`xhscdn.com`、`googleapis.com`、`gstatic.com`、`android.com`、`mzstatic.com`、`g.live.com` 等根域级放行；只保留抖音站点与 CDN、小红书根域、Apple/Microsoft 登录核心
- 依据：核心源在 `snssdk.com` 下拦的 29 条、`zijieapi.com` 下 62 条，全是 `log`/`mon`/`applog`/`mssdk`/`ads3`/`ads5` 埋点子域；`i.snssdk.com`、`aweme.snssdk.com`、`ecom.snssdk.com` 这些真正的业务接口并未被拦。根域级保护等于把这些该拦的埋点也一并放行
- 历史白名单里 4 条品牌级放行（`@@||uc.cn^`、`@@||quark.cn^`、`@@||ele.me^`、`@@||app-monitor.ele.me^`）一并删除：它们会让 UC／夸克／饿了么的全部广告位失守
- 保护清单补充 `douyin.com`、`iesdouyin.com`、`douyinstatic.com`；`douyinstatic.com` 下的 `lf3-webcastcdn-tos-sign` 是直播播放地址签名，拦了会直播卡住

### 2026-11-13 规则源重构 & 误伤治理

**问题**

- 实际使用中抖音出现「评论、商品页加载缓慢」：合并结果里存在大量指向业务 API 与遥测端点的规则（如 `i.snssdk.com`、`gecko-hl.snssdk.com`、`mon.snssdk.com`、`stsdk.vivo.com.cn`），被拦后 SDK 反复重试，表现为加载慢而非打不开

**规则源重构**

- 移除 6 个源：`5whys AdGuardHome-rules`（单源 37.7 万条，占旧集合 59%）、`217heidai adblockfilters`、`adrules.top`、`GOODBYEADS`、`10007 专属规则`、`Potterli20 加速 Hosts`（这项其实是 GitHub 加速 hosts，不是广告规则，混进来会把域名解析改到第三方 IP）
  - 其中 `217heidai` 后来以它的成品 DNS 列表重新作为核心源，见上一条更新日志
- 改为 4 个维护活跃、误伤控制严格的源：**AdGuard DNS filter**（官方 DNS 专用）、**anti-AD**（中文命中率高）、**OISD small**（官方口号即「Block. Don't break.」）、**AWAvenue 秋风广告规则**
- 合并总量由 63.7 万降到约 21.9 万，去掉了约三分之二的冗余与可疑条目，AdGuard Home 加载更快、内存占用更小

**误伤防护**

- 新增 `src/main/resources/protect-domains.txt` 保护清单：合并阶段丢弃指向受保护域名的拦截规则，并把它们以 `@@` 例外写进 `all.txt` / `dns.txt`
- 每次运行日志会打印「保护域名丢弃」条数，README 的规则订阅区块新增「保护域名数量」

**配套**

- 规则文件首行改为 `!` 注释，AdGuard Home 不再把文件头算作 1 条无效规则

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

规则有问题可以在本仓库提交 Issue，或前往上游项目 <a href="https://github.com/AdguardTeam/AdguardSDNSFilter">AdGuard DNS filter</a>、<a href="https://github.com/privacy-protection-tools/anti-AD">anti-AD</a>、<a href="https://oisd.nl">OISD</a>、<a href="https://github.com/hagezi/dns-blocklists">HaGeZi DNS blocklists</a> 反馈。
审核之后会加入到规则，如果规则有误杀会尽快处理。

**提交范围**

- 漏拦截的广告
- 误杀的网站
