# 熙康Thrift
# 用户相关服务定义
# v0.6
# Copyright © 2012 Neusoft Xikang Healthcare Technology Co.,Ltd
# liaor@neusoft.com
# 2012/03/25
###############################################################

include "xkcm.thrift"

namespace java com.xikang.channel.base.rpc.thrift.user

// 用户信息
struct UserInfo {
    1: string userId,              // 用户ID
	2: string email,               // 电子邮箱
	3: string mobileNum,           // 手机号
    4: string userName,            // 用户名
    5: string figureUrl            // 用户头像的URL
}

// 用户相关服务
// URL格式：http(s)://host(:port)/base/rpc/thrift/user-service.protocol
service UserService {

    // 接口名：查询用户
    // 接口描述：根据指定的查询条件查询用户信息。
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    //     commArgs：公共参数
    //     condition：查询条件
    // 返回值：查询结果的用户信息列表，查询结果不存在时返回空列表。
    // 返回值排序：用户名升序
    //
    // 例外：
    //     ae：鉴权例外，错误码参见例外类型定义。
    //     be：业务例外，目前无错误码。
    list<UserInfo> searchUsers(1: xkcm.CommArgs commArgs,
                               2: string condition)
                   throws(1: xkcm.AuthException ae,
                          2: xkcm.BizException be)

}
