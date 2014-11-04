# 熙康Thrift
# 帐户相关服务定义
# v0.74
# Copyright © 2012 Neusoft Xikang Healthcare Technology Co.,Ltd
# liaor@neusoft.com
# 2012/03/25
###############################################################

include "xkcm.thrift"

namespace java com.xikang.channel.base.rpc.thrift.account

// 用户信息
struct AccountInfo {
    1: string userId,              // 用户ID
	2: string email,               // 电子邮箱
	3: string mobileNum,           // 手机号
    4: string userName,            // 用户名
    5: string figureUrl            // 用户头像的URL
}

// 帐户相关服务
// URL格式：http(s)://host(:port)/base/rpc/thrift/account-service.protocol
service AccountService {

    // 接口名：注册帐户
    // 接口描述：提交注册信息以注册帐户。
    //
    // 注意：
    //     本方法的名字不能采用“register”，因为它是thrift的保留字。
    //
    // 是否SSL：是
    // 授权模式：NONE
    //
    // 参数：
    //     commArgs：公共参数
    //     email：电子邮箱
    //     mobileNum：手机号
    //     password：用户口令
    //     userName：用户名
    // 返回值：所注册帐户的用户信息
    //
    // 例外：
    //     ae：鉴权例外，错误码参见例外类型定义。
    //     be：业务例外，错误码如下：
    //         1：电子邮箱格式不合法
    //         2：电子邮箱已经被注册
    //         3：手机号格式不合法
    //         4：手机号已经被注册
    //         5：用户口令格式不合法
    //         6：用户名格式不合法
    AccountInfo registerAccount(1: xkcm.CommArgs commArgs,
                         2: string email,
                         3: string mobileNum,
                         4: string password,
                         5: string userName)
         throws(1: xkcm.AuthException ae,
                2: xkcm.BizException be),

    // 接口名：保存头像
    // 接口描述：保存头像，支持格式：jpg, jpeg, gif, png
    //
    // 是否SSL：是
    // 授权模式：DIGEST
    //
    // 参数：
    //     commArgs：公共参数
    //     selfUserId：头像所属者的id
    //     formatType：头像格式
    //     dataContent：头像数据
    //
    // 例外：
    //     ae：鉴权例外，错误码参见例外类型定义。
    //     be：业务例外，错误码无
    void saveAvatar(1: xkcm.CommArgs commArgs,
                    2: string selfUserId,
                    3: string formatType,
                    4: binary dataContent)
             throws(1: xkcm.AuthException ae,
                    2: xkcm.BizException be),

    // 接口名：验证帐户
    // 接口描述：验证所提出的用户帐户和用户口令是否存在和匹配，如果验证通过则取得基本用户信息。
    //
    // 是否SSL：是
    // 授权模式：DIGEST
    //
    // 参数：
    //     commArgs：公共参数
    //     userAccount：用户帐户
    //     password：用户口令
    // 返回值：所验证帐户的用户信息
    //
    // 例外：
    //     ae：鉴权例外，错误码参见例外类型定义。
    //     be：业务例外，错误码如下：
    //         1：用户帐户或用户口令错误
    AccountInfo validateAccount(1: xkcm.CommArgs commArgs,
                             2: string userAccount,
                             3: string password)
             throws(1: xkcm.AuthException ae,
                    2: xkcm.BizException be)

}
