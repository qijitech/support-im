
/**
 * 云旺的功能越来越多，DEMO为了全面展示云旺的功能也变得越来越复杂，开发者在DEMO中查找所需功能的示例代码的时候会比较吃力，为了解决以上问题，该文件横空出世！
 * 该文件主要介绍了DEMO中每个目录下的每个文件的具体功能，以便开发者可以通过阅读该文件快速定位DEMO中的示例代码，开发者查找所需功能时可以先在该文件查找关键字，然后找到对应的示例代码文件，最后到示例代码文件查看对应代码即可！
 *
 * com.taobao.openimui.common   该目录是DEMO中使用的一些常量和工具类，开发者无需关心；
 *
 * com.taobao.openimui.demo     该目录包含application文件，和DEMO的主界面，具体页面功能如下：
 *  {@link com.taobao.openimui.demo.DemoApplication}
 *      Application类，该类主要是执行IM的初始化，强烈建议开发者参考该类进行IM初始化
 *  {@link com.taobao.openimui.demo.FragmentTabs}
 *      Demo的主tab页面，该页面使用FragmentTabHost初始化四个tab：会话列表页面，联系人页面，群组页面，更多页面。同时，该页面还包含以下示例代码：
 *          添加消息未读数监听；
 *          添加群变更监听；
 *          添加发送消息生命周期回调监听
 *  {@link com.taobao.openimui.demo.ContactsFragment}
 *     联系人页面，云旺目前不支持好友功能，该页面中的联系人都是本地写死的，因此该页面对开发者而言参考价值不大；该页面存在的主要意义是：开发者可以从该页面点击联系人打开聊天窗口，体验云旺的聊天页面；开发者在集成过程中可以忽略该文件
 *  {@link com.taobao.openimui.demo.TribeFragment}
 *      群列表页面，该页面展示了群和讨论组列表、搜索加入群入口、创建群的入口，如果开发者需要群组列表可以参考该文件代码；
 *  {@link com.taobao.openimui.demo.MoreFragment}
 *      更多页面，该页面包含以下功能：设置新消息提醒(声音提醒，震动提醒)，登出IM，同步黑名单，如果开发者需要以上功能可以参考该文件代码；
 *
 * com.taobao.openimui.sample   该目录包含了云旺大部分功能的示例代码，具体如下：
 *  {@link com.taobao.openimui.sample.AtMsgListUISample}
 *      群成员选择页面UI定制示例，云旺发送群消息支持@功能，即用户发送群消息时可以@某一个或者多个群成员，具体操作路径是用户可以在群聊窗口的输入框内输入@，此时会跳转到群成员选择页面选择要@的成员。群成员选择页面由IMSDK提供，
 *      开发者可以对该页面进行定制以达到与自己的app风格一致的效果，具体定制方式可以参考该文件代码；
 *  {@link com.taobao.openimui.sample.ChattingFragmentSample}
 *      使用Fragment的方式打开聊天窗口示例，云旺的聊天窗口同时支持Activity和Fragment，如果用户需要使用Fragment的方式打开聊天窗口可以参考该文件代码；
 *  {@link com.taobao.openimui.sample.ChattingOperationCustomSample}
 *      自定义聊天窗口业务示例代码，该文件中包含里以下自定义功能：
 *          自定义语音消息播放模式(听筒模式/扬声器模式)  {@link com.taobao.openimui.sample.ChattingOperationCustomSample#useInCallMode(android.support.v4.app.Fragment, com.alibaba.mobileim.conversation.YWMessage)}
 *          自定义聊天窗口回复栏内的item列表及每个item的点击事件
 *          自定义地理位置消息的点击，长按和UI展示
 *          自定义消息的点击，长按和UI展示
 *          自定义不显示头像的自定义消息展示
 *          自定义url对应的商品信息详情
 *          自定义url点击事件和UI展示
 *          打开聊天窗口时自动发送一条消息给对方
 *          自定义聊天窗口顶部展示文案
 *  {@link com.taobao.openimui.sample.ChattingUICustomSample}
 *      自定义聊天窗口UI示例代码，该文件中包含以下自定义内容：
 *          自定义文本消息、图片消息、地理位置消息、自定义消息汽泡背景图
 *          自定义聊天窗口背景
 *          隐藏聊天窗口标题栏
 *          自定义聊天窗口标题栏view
 *          自定义图片预览页面标题栏右侧按钮点击事件
 *          自定义图片本地保存地址
 *          自定义用户默认头像
 *          自定义图片消息圆角角度
 *          自定义聊天窗口顶部自定义view
 *          自定义会话item view
 *  {@link com.taobao.openimui.sample.ContactsAdapterSample}
 *      联系人adapter示例代码，云旺目前不支持好友功能，开发者需要自己维护联系人，因此该文件没有参考价值，开发者可以忽略该文件；
 *  {@link com.taobao.openimui.sample.ContactSettingActivity}
 *      联系人设置页面，该页面包含以下功能：设置该联系人的消息接收状态，清除历史消息记录
 *  {@link com.taobao.openimui.sample.ConversationListOperationCustomSample}
 *      自定义会话列表页面业务示例代码，该文件中包含以下自定义功能：
 *          定制自定义会话的头像、名称、点击事件和长按事件
 *          定制普通会话的点击事件和长按事件
 *  {@link com.taobao.openimui.sample.ConversationListUICustomSample}
 *      自定义会话列表页面UI示例代码，该文件中包含以下自定义功能：
 *          自定义会话列表标题栏
 *          是否隐藏标题栏
 *          是否隐藏无网络提示view
 *          自定义置顶会话背景色
 *          是否开启搜索功能
 *          定制自定义会话item的view
 *          自定义会话列表页面activity生命周期
 *
 *  {@link com.taobao.openimui.sample.CustomSampleHelper}
 *      UI定制初始化示例代码，进行UI定制必须首先进行UI定制初始化，具体初始化方式可以参考该文件
 *  {@link com.taobao.openimui.sample.DemoSimpleKVStore}
 *      DEMO的持久化存储，开发者无需关心，可以忽略该文件
 *  {@link com.taobao.openimui.sample.ExpandableContactsActivitySample}
 *      群组折叠型联系人页面，开发者无需关心，可以忽略该文件
 *  {@link com.taobao.openimui.sample.FriendsCustomAdviceSample}
 *      自定义分组联系人页面示例代码，开发者无需关心，可以忽略该文件
 *  {@link com.taobao.openimui.sample.InitHelper}
 *      云旺初始化示例代码，请开发者务必参考该文件代码进行云旺初始化
 *  {@link com.taobao.openimui.sample.ISelectContactListener}
 *      发送名片消息时选择联系人监听接口，开发者无需关心，可以忽略该文件
 *  {@link com.taobao.openimui.sample.LoginSampleHelper}
 *      登录和登出示例代码，请开发者务必参考该代码文件进行云旺的登录和登出
 *  {@link com.taobao.openimui.sample.NotificationInitSampleHelper}
 *      自定义通知栏消息示例代码，该文件包含以下功能：
 *          自定义通知栏的title、图标、提示文案和跳转intent
 *          自定义消息提醒状态(声音、震动、通知栏)
 *  {@link com.taobao.openimui.sample.OpenConversationSampleHelper}
 *      打开会话列表页面示例代码，开发者打开会话列表时可以参考该文件
 *  {@link com.taobao.openimui.sample.OpenEServiceChattingSampleHelper}
 *      打开客服会话聊天窗口示例代码，开发者打开客服会话时可以参考该文件
 *  {@link com.taobao.openimui.sample.SelectContactToSendCardActivity}
 *      发送名片消息时选择联系人页面，开发者无需关心，可以忽略该文件
 *  {@link com.taobao.openimui.sample.SendAtMsgDetailUISample}
 *      自定义群@消息页面UI，该文件包含以下定制功能：定制群@消息标题栏
 *
 *  {@link com.taobao.openimui.sample.SelectTribeAtMemberSample}
 *      自定义群@成员列表选择界面的TitleBar,可以参考该文件
 *
 *  {@link com.taobao.openimui.sample.SmilyCustomSample}
 *      自定义表情示例代码，开发者自定义聊天窗口表情时请参考该文件
 *  {@link com.taobao.openimui.sample.TribeAdapterSample}
 *      群列表adapter，开发者需要群列表页面时请参考该adapter
 *  {@link com.taobao.openimui.sample.TribeMsgRecTypeSetActivity}
 *      设置群消息接收状态示例代码，开发者设置群消息接收状态时请参考该文件
 *  {@link com.taobao.openimui.sample.UserProfileSampleHelper}
 *      用户信息自定义示例代码，该文件包含以下功能：
 *          自定义用户头像、昵称
 *          自定义用户头像点击事件
 *  {@link com.taobao.openimui.sample.YWSDKGlobalConfigSample}
 *      云旺高级功能开关设置示例代码，该文件包含以下功能：
 *          是否开启自定登录功能
 *          是否开启黑名单功能
 *          是否开启对方输入状态提示功能
 *          是否开启消息已读未读功能
 *          是否开启头像压缩功能
 *          是否开启群@消息功能
 *
 * com.taobao.openimui.tribe    该目录是群功能示例代码，具体如下
 *  {@link com.taobao.openimui.tribe.EditMyTribeProfileActivity}
 *      修改我在本群的昵称
 *  {@link com.taobao.openimui.tribe.EditTribeInfoActivity}
 *      修改群名称、群公告
 *  {@link com.taobao.openimui.tribe.InviteTribeMemberActivity}
 *      邀请好友加入群
 *  {@link com.taobao.openimui.tribe.SearchTribeActivity}
 *      搜索群页面
 *  {@link com.taobao.openimui.tribe.TribeInfoActivity}
 *      群资料页面
 *  {@link com.taobao.openimui.tribe.TribeMembersActivity}
 *      群成员列表页面
 *  {@link com.taobao.openimui.tribe.TribeSystemMessageActivity}
 *      群系统消息页面，该页面用于展示群邀请消息
 *
 * com.taobao.openimui.imcore   该目录是云旺接口调用示例代码，具体如下：
 *  {@link com.taobao.openimui.imcore.ConversationSampleHelper}
 *      会话相关接口使用示例代码，该文件包含以下功能：
 *          获取会话管理器
 *          获取所有会话
 *          获取指定会话
 *          获取指定会话的最近一条消息
 *          获取所有会话的总未读数
 *          获取指定会话的未读数
 *          从会话对象中获取当前聊天的用户id
 *          从会话队形中获取群id
 *          创建跨appKey的会话
 *          创建客服会话
 *          添加会话列表更新监听
 *  {@link com.taobao.openimui.imcore.TribeSampleHelper}
 *      群功能接口使用示例代码，该文件包含以下功能：
 *          获取群管理器
 *          创建群
 *          从服务器获取当前用户所在的所有群
 *          从服务器获取单个群信息
 *          获取指定群的成员列表
 *          退出指定的群
 *  {@link com.taobao.openimui.imcore.ConversationListActivity}
 *      基于IMCore集成的最近联系人列表页面示例代码，使用IMCore集成的开发者在开发最近联系人页面的时候可以参考该文件
 *  {@link com.taobao.openimui.imcore.ChattingActivity}
 *      基于IMCore集成的聊天窗口页面示例代码，使用IMCore集成的开发者在开发聊天窗口页面的时候可以参考该文件
 *
 *
 *  其他包和文件与云旺功能本身无关，开发者无需关心
 */

