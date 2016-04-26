package support.im.demo;

import com.avos.avoscloud.AVOSCloud;
import pl.tajchert.nammu.Nammu;
import support.im.data.SupportUser;
import support.im.leanclound.ChatManager;
import support.ui.app.SupportApp;

public class SupportImApp extends SupportApp {

  @Override public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(appContext(), BuildConfig.LeanCloundAppId, BuildConfig.LeanCloundAppKey);
    Nammu.init(appContext());

    SupportUser.alwaysUseSubUserClass(SupportUser.class);

    // 节省流量
    AVOSCloud.setLastModifyEnabled(true);

    ChatManager.getInstance().init(this);
    ChatManager.setDebugEnabled(BuildConfig.DEBUG);

    // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
    //AVIMClient tom = AVIMClient.getInstance("Tom");
    //// 与服务器连接
    //tom.open(new AVIMClientCallback() {
    //  @Override public void done(AVIMClient client, AVIMException e) {
    //    if (e == null) {
    //      // 创建与Jerry之间的对话
    //      client.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null,
    //          new AVIMConversationCreatedCallback() {
    //            @Override public void done(AVIMConversation conversation, AVIMException e) {
    //              if (e == null) {
    //                AVIMTextMessage msg = new AVIMTextMessage();
    //                msg.setText("耗子，起床！");
    //                // 发送消息
    //                conversation.sendMessage(msg, new AVIMConversationCallback() {
    //                  @Override public void done(AVIMException e) {
    //                    if (e == null) {
    //                      Log.d("Tom & Jerry", "发送成功！");
    //                    }
    //                  }
    //                });
    //              }
    //            }
    //          });
    //    }
    //  }
    //});
  }
}