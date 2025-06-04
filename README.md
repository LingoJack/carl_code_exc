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

2025-05-30
(1) 今日内容：
- 完成对udp_client的改造
- 参加周会
- 工单的需要的数据已经导出了一部分，其余的需要更改时间去捞取
(2) 明日计划：
- 放假
(3) 是否存在问题：
- 无

2025-06-03
(1) 今日内容：
1. 扩大了日志TDW的检索范围，但没有检索到新的数据
2. 从TDW之外再从CLS去捞取目标接口的日志
3. 测试了udp_client的CLS转发和cam_diff的获取北极星地址功能
4. 修改docker-entrypoint.sh，每次启动会固定往crontab写入新的任务，已修改为先创建空文件覆盖原有的任务
(2) 明日计划：
1. 跑通从udp_client到cam_diff服务转发的完整链路
2. 增加cam_diff的日志分级
(3) 是否存在问题：
1. 今天尝试把client和diff连起来的时候，发现client有发送成功的日志，但是diff没有接收成功的日志输出，转发的日志输出，已经排除的规则匹配的问题。之前把日志都删了，不好排查，做一下日志分级可能比直接删日志可能好些

2025-06-04
(1) 今日内容：
1. 跑通了client到diff的链路，同步新的变化到cam_diff说明文档：https://iwiki.woa.com/p/4014602929
2. 新增cam_diff的日志分级功能，优化输出格式、代码结构
(2) 明日计划：
1. 
(3) 是否存在问题：
1.




1. 修改Prompt，跑接口分类分级的文档数据
2. 修改cam_diff服务，增加北极星接入和多地址转发和某些字段的匹配逻辑，完善脚本
3. cam_diff服务增加一个udp_client，负责在开启时候从五分钟前开始读从cls读日志打成udp包发到cam_diff

CREATE DATASOURCE `interface_doc_db` WITH (
datasource=jdbc, 
url=`jdbc:supersql:datasource:url='jdbc:mysql://30.46.139.169:3306/interface_doc_db?characterEncoding=utf8&connectTimeout=30000';driver='com.mysql.jdbc.Driver';jarPath='./supersql-drivers.zip/mysql'`,
driver=`com.tencent.supersql.connector.SuperSqlDataSourceDriver`,
username=conf, 
password=`2j5O62RRV5aIeGLp`,
catalog=interface_doc_db,
type=mysql)




请用python。现在是这样，我希望从数据库表tdw_record中查询出数据是否大于10（根据product和interface），如果小于10，那么就拿product和interface去数仓查数据，然后插入到表中。
这是mysql数据库tdw_record的建表sql：
CREATE TABLE `tdw_record` (
                            `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `uin` bigint DEFAULT NULL COMMENT '用户ID',
                            `client_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户IP',
                            `product` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品',
                            `interface` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接口',
                            `request` text COLLATE utf8mb4_unicode_ci COMMENT '请求数据',
                            `response` text COLLATE utf8mb4_unicode_ci COMMENT '响应数据',
                            `tdbank_impl_date` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导入日期',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_tdbank_impl_date` (`tdbank_impl_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='tdw记录表';
，代码我已经写了一些，可以给你参考：
def TDW_PL(tdw, argv):
    for product, interface in fetch_interface_list():
        sql = """
            select uin, cientip, product, interface, request, response, mode
            from csig_cam::sig_auth_comm_log
            where tdbank_imp_date > '2025050110' 
              and tdbank_imp_date < '20250503018'
              and product = '%s'
              and interface = '%s'
            limit 10
        """
        sql = sql % (product, interface)
        tdw.WriteLog(sql)
        res = tdw.execute(sql)
        tdw.WriteLog(res)

def select_interface(product, interface):
    sql = """
        select count(1) from interface_doc_db.`tdw_record` where product = '%s' and interface = '%s'
    """
     # sql = sql % (product, interface)
    res = tdw.execute(sql)
    tdw.WriteLog(res)

def fetch_interface_list():
    non_cloud_apis = """
        cvm:ResetInstances
        cvm:CheckSecurityGroupPolicyRedundancy
        cvm:DescribeInstancesOfferingsV3
        cvm:DescribeInstanceTypeQuotaV3
        cvm:DescribeZonesV3
        cvm:InquiryInstancePriceHour
        cvm:InquiryPriceAllocateHostsV3
        cvm:DescribeInstanceMainInfo
        cvm:DescribeInstanceStatisticsV3
        cvm:DescribeKeyPairsV3
        cvm:InquiryInstanceBandwidthConfig
        cvm:InquiryInstanceOrOSConfig
        cvm:InquiryPriceResetInstancesTypeV3
        cvm:DescribeDiskConfigQuota
        cvm:DescribeDiskFeatures
        cvm:DescribeDiskInitializationUserData
        cvm:DescribeDisksDeniedActions
        cvm:DescribeSnapshotGroupsDeniedActions
        cvm:DescribeSnapshotsDeniedActions
        cvm:GetSnapOverview
        cvm:DescribeInstanceTypeSalesConfig
        cvm:InquirePriceModifyDiskBackupQuota
        cvm:InquirePriceModifyDiskExtraPerformance
        cvm:InquirePriceRefundDisks
        cvm:InquiryPriceCreateDisks
        cvm:InquiryPriceModifyDiskAttributes
        cvm:InquiryPriceModifyDisksChargeType
        cvm:InquiryPriceRenewDisks
        cvm:InquiryPriceResizeDisk
        cvm:ModifyInstanceFamiliesAttribute
        cvm:SwitchParameterCreateDisks
        cvm:SwitchParameterModifyDiskAttributes
        cvm:SwitchParameterModifyDiskChargeType
        cvm:SwitchParameterModifyDiskExtraPerformance
        cvm:SwitchParameterRenewDisks
        cvm:SwitchParameterResizeDisk
        cvm:DescribeCbsOperationLogs
        cvm:ModifyInstanceTypeSalesConfig
        cvm:ModifyDiskRollbackType
    """

    cloud_apis = """
        cvm:DescribeInstanceBanInfo
        cvm:DescribeAddresses
        cvm:ReleaseAddresses
        cvm:AssociateAddress
        cvm:DisassociateAddress
        cvm:RenewAddresses
        cvm:ModifyAddressesBandwidth
        cvm:StopInstances
        cvm:TerminateInstances
        cvm:DescribeInstances
        cvm:DescribeInstancesStatus
        cvm:ResetInstance
        cvm:DescribeImages
        cvm:AllocateAddresses
    """

    return parse_apis(non_cloud_apis) + parse_apis(cloud_apis)


def parse_apis(api_string):
    """Parse API string and return formatted list of (product, interface) tuples"""
    apis = []
    for line in api_string.strip().split('\n'):
        line = line.strip()
        if line:
            product, interface = line.split(':')
            apis.append((product.strip(), interface.strip()))  # Using tuple instead of list
    return apis
我告诉你，tdw的返回数据格式是tsv的，比如我查询select count(1) from interface_doc_db.`tdw_record` where product = '%s' and interface = '%s'
这是res的打印结果：['1\t2710703044\t21.7.221.99\tcvm\tDescribeAddresses\t{"version":0,"eventId":1748563162,"timestamp":1748563162,"interface":{"interfaceName":"logic.cam.sigAndAuth","para":{"mode":2,"new_check_resource":1,"resource":"qcs::cvm:bj:uin\\/2710703044:eip\\/*","ownerUin":"2710703044","sub_condition":[],"uin":"2710703044","action":"cvm:DescribeAddresses"}}}\t{"version":"0","componentName":"mall_logic","timestamp":"0","eventId":1748563162,"returnValue":0,"returnCode":0,"returnMessage":"permission verify","data":{"ownerUin":2710703044,"uin":2710703044,"ownerAppid":0,"ownerUinStr":"2710703044","uinStr":"2710703044","ownerAppidStr":"0","keyType":0,"keySource":0,"formatString":"","stringToSign":"","permissionDetail":[],"transferDetail":[],"debugInfo":[],"accountArea":0}}\tNULL\t2025-05-30 18:33:53\t2025-05-30 18:33:53']，请你完成我的需求