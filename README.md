# 背景
这是我在跟着代码随想录进行算法学习的代码笔记
分享出来，希望能够对大家有帮助~

# 相关网址
[代码随想录官网](https://programmercarl.com)

2025-05-20
(1) 今日内容：
- 通过智研流水线构建cam_diff的fearture/v0.0.2分支的镜像并部署到tke
  https://kubernetes.woa.com/v4/projects/prj22kwl/workloads/cls-qzr0esc6/ns-prj22kwl-1570084-dev-test/StatefulSetPlus/cam-diff-cache-test
- 通过智研流水线构建cam_diff_udp_client的镜像并部署到tke
  https://kubernetes.woa.com/v4/projects/prj22kwl/workloads/cls-qzr0esc6/ns-prj22kwl-1570084-dev-test/StatefulSetPlus/test-cam-diff-cache-client

(2) 明日计划：
- 拉取七彩石配置，进行对cam_diff测试

(3) 是否存在问题：
- 代码仓库的权限问题，已解决


2025-05-21
(1) 今日内容：
- 拉取七彩石配置，完成对cam_diff测试，说明及测试文档：https://iwiki.woa.com/p/4014602929
- 提交master分支和feature/v0.0.2的merge请求，根据AI评审提出的建议对代码进行了一些修改

(2) 明日计划：
- 推进内部接口分类分级项目进度：七彩石、工蜂仓库 -> 构建镜像 -> tke

(3) 是否存在问题：
- 无

2025-05-22
(1) 今日内容：
- 查看pcg_check_resource鉴权的定时任务，crontab配置了但是正常执行，将定时任务的逻辑修改为go代码内部实现
- 申请了跳板机权限，测试了scm获取临时密钥逻辑
- 测试feature/v0.0.2并入master分支后的cam_diff的基本功能，后续修改方向：
  - 配置改为多个Forward
  - 删除Uin==0拦截判断
  - 修改Action的匹配逻辑：根据request包（json），而不是外部的Action
  - 拆分entrypoint.sh和start.sh，增加保活逻辑
  - 增加match拦截的逻辑，仅允许(type, interface)组合(cam_auth, sigAndAuth),(cam_list, checkResource)
(2) 明日计划：
- 继续测试内部接口分类分级的代码
(3) 是否存在问题：
- 在测试内部接口分类分级的代码，目前的流程是：本地创建新的test分支 -> 修改代码 -> build -> 上传到tke运行看输出
  想知道有无更高效的做法

2025-05-23
(1) 今日内容：
- 跑通了CLS日志获取的链路和获取AI模型的demo
- 更新cam_diff的代码，优化日志输出
- 参加周会
(2) 明日计划：
- 针对demo发现的问题做出修改
 - Prompt中强制固定AI输出，对AI的返回结果增加一些有效性解析步骤
 - 针对目前只有两个模型供调用的情况，简化代码的逻辑
- 更新数据库配置信息，尝试把完整程序启动，开始写入数据
(3) 是否存在问题：
- 无

1. 回退五分钟的能力
2. Prompt的写法里面：可能，但是


--- 
2025-05-26
(1) 今日内容：
- 跑完了对内部接口分类分级数据的第一版数据（风险文档、接口文档）
  - 当前可以得到日志数据的接口数：2141
  - 高危接口数：294，敏感接口数：1562
- 编写程序对内部接口分类分级跑的数据去重、补充风险文档和接口文档间的关联关系
- 完善内部接口分类分级的代码，支持在调用CLS、LLM服务连续失败达到阈值时主动熔断
- 根据第一版数据的反馈，修改Prompt
(2) 明日计划：
- 根据第一版数据过滤出来风险、敏感接口，使用第二版Prompt提供给LLM，看看效果
(3) 是否存在问题：
- 无

2025-05-27
(1) 今日内容：
- 根据第一版数据过滤出来风险、敏感接口，修改Prompt，寻找较好的Prompt效果
  - 当前可以得到日志数据的接口数：2141
  - 原高危接口数：294
  - 现高危接口数：102
(2) 明日计划：
- 继续改Prompt
(3) 是否存在问题：
- 无

2025-05-28
(1) 今日内容：
- 修改Prompt后重跑了之前的全量接口，导出文档
- 修改cam_diff，使之接入北极星，支持多Forward转发
- 对于cam_diff, 拆分entrypoint.sh和start.sh，增加保活逻辑
- 对于cam_diff，修改Action的匹配逻辑：根据request包的InterfaceName，而不是外部的Action
(2) 明日计划：
- 测试修改后的cam_diff功能是否正常
- 处理找日志需求的工单
(3) 是否存在问题：
- 无

2025-05-29
(1) 今日内容：
- 针对工单的需求，查阅TDW相关文档，考虑实现方案：
  1. 使用pySql，注册数据源a（过滤接口的数据源）到wedata
  2. 注册数据源b到wedata（写入结果的目的数据源）
  3. 直接在wedata平台上执行，不再通过统一调度平台us跑tdw出库mysql的任务
  4. 在pySql中将查询结果解析，写入数据源b
- 修改之前的cam_diff测试项目中udp_client，增加“回退五分钟”的能力
(2) 明日计划：
- 针对当前工单解决方案编码实现
- 继续修改udp_client
(3) 是否存在问题：
- wedata注册数据源a需要连接信息（用户，密码，ip，port），这些信息是否方便给出去

2025-05-30
(1) 今日内容：
- 完成对udp_client的改造
- 参加周会
- 工单的需要的数据已经导出了一部分，其余的需要更改时间去捞取
(2) 明日计划：
- 放假
(3) 是否存在问题：
- 无

1. 修改Prompt，跑接口分类分级的文档数据
2. 修改cam_diff服务，增加北极星接入和多地址转发和某些字段的匹配逻辑，完善脚本
3. cam_diff服务增加一个udp_client，负责在开启时候从五分钟前开始读从cls读日志打成udp包发到cam_diff


