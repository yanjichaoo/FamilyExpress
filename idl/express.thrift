# 熙康Thrift
# 亲情快递接口协议
# v0.72
# Copyright ? 2012 Neusoft Xikang Healthcare Technology Co.,Ltd
# baoying@neusoft.com
# 2012/03/27
###############################################################

include "xkcm.thrift"

namespace java com.xikang.channel.familyexpress.rpc.thrift.express

// 快递内容类型
enum FEContentType {
    TEXT = 1,                               // 文本
    AUDIO = 2,                              // 音频
    IMAGE = 3                               // 图像
}

// 亲情快递类型：100+：医疗数据类型；200+：运动数据类型
enum ExpressType {
    NORMAL = 0,                             // 普通快递
    BLOODPRESSURE = 101,                    // 血压
    BLOODSUGAR = 102,                       // 血糖
    BLOODOXYGEN = 103,                      // 血氧
    SPORT = 201                             // 运动
}

// 亲情快状态
enum ExpressStatus {
    SENT = 0,                               // 已发
    FORWARDED = 1,                          // 送达
    UNREAD = 2,                             // 未读、未看、未听
    READ = 3,                               // 已读、已看、已听
}

// 支持文件类型
enum FEFormatType{
    TEXT = 0,                               // 文本格式
    SPX = 1,                                // 音频：spx
    OGG = 2,                                // 音频：ogg
    MP3 = 3,                                // 音频：mp3
    PNG = 4,                                // 图像：png
    JPG = 5,                                // 图像：jpg、jpeg
    GIF = 6,                                // 图像：gif
    _3GP = 7                                // 视频：3gp
}

// 亲情快递状态结构体
struct ExpressChangedStatus {
    1: string expressId,                   // 快递ID
    2: ExpressStatus expressStatus         // 修改后的快递状态
}

//快递信息(输入、输出共用)
struct ExpressInfo {
    1: string expressId,                  // 快递ID，输入时不需要填写
    2: string from,                       // 发出快递的用户ID
    3: string to,                         // 接收快递的用户ID
    4: xkcm.DeviceType fromTerminalType,  // 快递发出者的终端类型
    5: string time,                       // 快递发出的时间，输入时不需要填写，如2011-12-05 13:00:00
    6: ExpressType expressType,           // 快递类型，如：血压、血糖、血氧数据
    7: FEContentType contentType,         // 快递内容发送方式类型，如：音频、图像
    8: FEFormatType format,               // 资源格式，如：AUDIO:SPX,OGG,MP3；IMAGE:PNG, JPG, GIF；VIDEO:OGG,3GP
    9: i16 length,                        // 资源大小，文本和图像此项值为空
    10:string textContent,                // 文本快递内容，对于文本此项存具体的文本内容；对于音频和图像，此项输入时为空，输出时展现的是存储音频和图像快递的服务器地址url；对于图像，该URL是原图的URL，根据此URL可以生成缩略图的URL，生成规则参见熙康文件服务相关规范
    11:binary dataContent,                // 音频、图像内容的二进制流;输出时，该项为空
    12:ExpressStatus expressStatus,       // 快递推送或读取状态
    13:i64 updateTime                     // 快递修改时间，格式：1970年1月1日开始经历的毫秒数
}

//得到用户所有快递信息的结构体
struct ExpressInfoAll {
    1: i16 leftUnreadNum,                           // 本次快递列表取得后，服务器上剩余的未取的快递条数
    2: list<ExpressInfo> expressInfos               // 已读取的快递列表
}

// 在某间时段所有状态有变化的快递列表
struct ExpressChangedStatusAll {
    1: map<string,list<ExpressChangedStatus>> statusChangedMap,// 状态被修改的快递信息集合，map中的key为家人的UserID，value为发送给该家人的状态有变化的快递状态信息列表
    2: i64 serverTime                                          // 服务器当前时间，格式：1970年1月1日开始经历的毫秒数
}

// 亲情快递应用使用时的初始信息
struct AppInitInfo {
    1: string guideAsstId //向导小助手的ID
}


// 亲情快递服务
// URL格式：http(s)://host(:port)/familyexpress/rpc/thrift/express-service.protocol
service ExpressService {

    // 接口名：查询亲情快递
    // 接口描述：查询由指定家人发出的接收者是当前用户，且状态为“发出”的快递列表，同时后台将状态改为“送达”
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // userIds: 家人ID的列表
    // 返回值：快递信息按家人查询结果的Map集合，Map中的key为家人的UserID，
    //         value为家人发给自己的快递列表，一次返回的所有家人的快递条数的总和最多为500条，
    //         当服务器上待取条数大于500条时：
    //             (1)对于各个家人，将优先取得userIds参数中靠前的家人的列表。
    //             (2)对于同一个家人，将优先取得较旧的快递。
    //             (3)各个家人本次未取的剩余快递的条数记录在返回值的ExpressInfoAll.leftUnreadNum中。
    // 返回值排序：Map中的值按快递的发送时间升序排序
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    map<string,ExpressInfoAll> getExpressList(1:xkcm.CommArgs commArgs,
                                              2:list<string> userIds)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：查询距上一次获取状态至今状态有变化的快递的最新状态
    // 接口描述：根据上次查询时间及家人ID的集合，将该时间之后的由当前用户发出给指定家人的且状态被修改过的所有快递的ID及状态进行返回
    //           同时返回服务器当前时间，供下次查询使用
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // userIds: 家人ID的列表
    // lastObtainTime: 上一次获取状态的服务器时间，格式：1970年1月1日开始经历的毫秒数
    // 返回值：所有当前用户发出的状态有变化的快递状态集合
    //         服务器当前时间，格式：1970年1月1日开始经历的毫秒数，第一次使用传0
    // 返回值排序：快递ID升序
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    ExpressChangedStatusAll getChangedStatusList(1:xkcm.CommArgs commArgs,
                                                 2:list<string> userIds,
                                                 3:i64 lastObtainTime)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：第一次使用亲情快递接口（注意：本接口只有在该用户第一次在该终端使用亲情快递产品时才需要调用。）
    // 接口描述：服务端收到该请求后，会向终端发送小助手的欢迎快递
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // 返回值：亲情快递应用使用时的初始信息
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    AppInitInfo firstlyUseExpress(1:xkcm.CommArgs commArgs)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：发送快递
    // 接口描述：将快递信息发出（如接收者是“小助手”，后台自动生成相应的回复信息）
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // expressInfo：预发送快递的具体信息，具体参见结构体ExpressInfo中的描述
    // 返回值：快递ID
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    string sendExpress(1:xkcm.CommArgs commArgs,
                       2:ExpressInfo expressInfo)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：批量更新快递状态，由送达到已读(文字、图像)或未听（音频）
    // 接口描述：根据最后一条送达的快递的修改时间将当前用户发送给相应家人的小于该时间的且状态为“送达”的所有快递的状态都改为“已读或未听”
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // lastExpressTime: 最后一条送达的快递的修改时间，格式：1970年1月1日开始经历的毫秒数
    // userId: 家人的用户ID
    // 返回值：无
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    void updateFromForwardedStatus(1:xkcm.CommArgs commArgs,
                                   2:i64 lastExpressTime,
                                   3:string userId)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be),


    // 接口名：更新快递状态:由未听到已听
    // 接口描述：根据快递ID及给定的状态对快递进行状态更新
    //
    // 是否SSL：否
    // 授权模式：DIGEST
    //
    // 参数：
    // commArgs：公共参数
    // expressId: 快递ID
    // 返回值：无
    //
    // 例外：
    // ae：鉴权例外，错误码参见例外类型定义。
    // be：业务例外，目前无错误码。
    void updateToReadStatus(1:xkcm.CommArgs commArgs,
                            2:string expressId)
             throws (1:xkcm.AuthException ae,
                     2:xkcm.BizException be)
}
