# 熙康Thrift
# 家人接口协议
# v0.75
# Copyright ? 2012 Neusoft Xikang Healthcare Technology Co.,Ltd
# baoying@neusoft.com
# 2012/03/27
###############################################################

include "xkcm.thrift"
include "express.thrift"

namespace java com.xikang.channel.familyexpress.rpc.thrift.family

// 性别
enum Gender {
    MAN = 1,                                // 男
    WOMAN = 2,                              // 女
    NOKNOWN = 0,                            // 未知的性别
    UNTOLD = 9                              // 未说明的性别
}

// 家人许可邀请的状态
enum AllowStatus {
    ADDED = 1,                             // 已添加
    INVITED = 2,                           // 已邀请
    BEINVITED = 3                          // 被TA邀请
}

// 家人辈分
enum RelativeCode {
    ELDER = 1,                              // 长辈
    FELLOW = 2,                             // 平辈
    YOUNGER = 3                             // 晚辈
}

// 家人关系代码表（返回的结构体，供添加家人页面的"辈分、称呼"两个下拉框使用）
struct MemberRelation {
    1: RelativeCode relativeCode,           // 辈分代码
    2: string titleCode,                    // 称呼编码
    3: string title,                        // 对家人的称呼，例如：爸爸、妈妈
    4: string reTitleCodeM,                 // 家人对自己的称呼编码 男，例如：儿子
    5: string reTitleCodeW                  // 家人对自己的称呼编码 女，例如：女儿
}

// 家人及最后一条快递信息（返回的结构体）
struct MemberInfo {
    1: string userId,                             // 用户ID
    2: string userName,                           // 用户名
    3: string nikeName,                           // 对家人的昵称，如：老爷子
    4: string relation,                           // 对家人的称呼，如：奶奶、爸爸"
    5: i16  unReadNum,                            // 未读快递的数目
    6: string lastSendTime,                       // 最后一条快递发送时间，如：2011-12-05 13:00:00
    7: express.FEContentType lastContentType,     // 最后一条快递内容类型，如：音频快递
    8: express.ExpressType lastExpressType,       // 最后一条快递类型，如：运动、血压
    9: string lastTextContent,                    // 最后一条快递的文本内容，对于文本，显示文本内容，对于音频和图像，该项为空值
    10: AllowStatus allowStatus,                  // 家人许可邀请的状态
    11:string figureUrl                           // 家人头像url
}

// 用户基本信息（返回的结构体）
struct UserBaseInfo {
    1: string userId,                             // 用户ID
    2: string userName,                           // 用户名
    3: string nikeName,                           // 对家人的昵称
    4: string relation,                           // 对家人的称呼，如：奶奶、爸爸"
    5: string mobile,                             // 用户手机
    6: string email                               // 电子邮件
    7: AllowStatus allowStatus,                   // 家人许可邀请的状态
    8: string figureUrl                           // 家人头像url
}

// 家人相关的服务
// URL格式：http(s)://host(:port)/familyexpress/rpc/thrift/family-service.protocol
service FamilyService {

    // 接口名：查询家人辈分及称呼关系代码表
    // 接口描述：查询家人辈分及称呼关系代码的列表，供添加家人页面的"辈分、称呼"两个下拉框的初始化使用
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数:
    // commArgs：公共参数
    // 返回值：家人辈分及称呼关系的列表
    // 返回值排序：无
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    list<MemberRelation> getMemberRelationCodeList(1:xkcm.CommArgs commArgs)
                    throws (1:xkcm.AuthException ae,
                            2:xkcm.BizException be),


    // 接口名：查询用户的家人快递列表
    // 接口描述：根据当前登录人ID查询其家人及最后一条快递信息(包含小助手，对于彼此未发送过快递的家人不显示)
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // 返回值：查询结果的家人列表
    // 返回值排序：按如下由上到下的顺序
    //            1) 添加成功的家人。按照添加时间由新到旧排序
    //            2) 亲情快递助手
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    list<MemberInfo> getFamilyMemberList(1:xkcm.CommArgs commArgs)
                    throws (1:xkcm.AuthException ae,
                            2:xkcm.BizException be),
                            

    // 接口名：查询用户所有家人的详细信息列表
    // 接口描述：根据当前登录人ID查询其所有家人的详细信息(包含小助手)
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // 返回值：查询结果的家人列表
    // 返回值排序：按如下由上到下的顺序
    //            1) 添加成功的家人。按照添加时间由新到旧排序
    //            2) 亲情快递助手
    //            3) 等待确认邀请的家人。按照邀请时间由新到旧排序
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    list<UserBaseInfo> getFamilyMemberDetailList(1:xkcm.CommArgs commArgs)
                    throws (1:xkcm.AuthException ae,
                            2:xkcm.BizException be),


    // 接口名：添加家人
    // 接口描述：通过家人的用户ID及与自己的关系信息，添加家人
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // userId：家人的用户ID
    // titleCode：对家人的称呼对应的码值，如家人是自己的父亲，则传入父亲对应的码值（辈分字段忽略）
    // nickname：对家人的昵称
    // reRelationCode：家人对自己的称呼，如儿子，女儿；目前只做性别区分，如是男性，传入Gender.MAN；女性传入Gender.WOMAN
    // directlyAdd：是否直接添加：true：直接添加；false：邀请方式添加。默认为true
    // 返回值: 无
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，错误码如下：
    //         1：称呼不能为空
    //         2：不能添加自己为家人
    //         3：你选择的用户信息不存在
    //            已经是家人关系
    //            家人邀请已经发送，请耐心等待对方的回复
    //            对方已向您发送该类请求，请在通知中确认
    //           （说明：例外码为3的，返回的messeage为上述4种的一种）
    void addFamilyMember(1:xkcm.CommArgs commArgs,
                         2:string userId,
                         3:string titleCode,
                         4:string nickname,
                         5:Gender reRelationCode,
                         6:bool directlyAdd)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：修改家人信息
    // 接口描述：根据家人的用户ID，修改对家人的称呼，目前只能修改"对他的称呼"字段
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // userId：家人的用户ID
    // nickname：对家人的昵称
    // 返回值：无
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    void updateFamilyMember(1:xkcm.CommArgs commArgs,
                            2:string userId,
                            3:string nickname)
              throws (1:xkcm.AuthException ae,
                      2:xkcm.BizException be),


    // 接口名：删除家人
    // 接口描述：根据家人的用户ID，将家人从自己的家人列表中删除
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // userId：预删除的家人的用户ID
    // 返回值：无
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    void deleteFamilyMember(1:xkcm.CommArgs commArgs,
                            2:string userId)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：获得短信内容
    // 接口描述：获得"通知家人安装亲情快递"的短信内容
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // 返回值：预发送的短信内容字符串
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    string getNoteMessage(1:xkcm.CommArgs commArgs)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be)

}
