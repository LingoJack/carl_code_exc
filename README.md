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

2025-05-26
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