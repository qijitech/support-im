package support.im.demo;

import android.util.Log;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import java.util.Arrays;
import pl.tajchert.nammu.Nammu;
import support.ui.app.SupportApp;

public class SupportImApp extends SupportApp {

  @Override public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(appContext(), BuildConfig.LeanCloundAppId, BuildConfig.LeanCloundAppKey);
    Nammu.init(appContext());

    // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
    AVIMClient tom = AVIMClient.getInstance("Tom");
    // 与服务器连接
    tom.open(new AVIMClientCallback() {
      @Override public void done(AVIMClient client, AVIMException e) {
        if (e == null) {
          // 创建与Jerry之间的对话
          client.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null,
              new AVIMConversationCreatedCallback() {
                @Override public void done(AVIMConversation conversation, AVIMException e) {
                  if (e == null) {
                    AVIMTextMessage msg = new AVIMTextMessage();
                    msg.setText("耗子，起床！");
                    // 发送消息
                    conversation.sendMessage(msg, new AVIMConversationCallback() {
                      @Override public void done(AVIMException e) {
                        if (e == null) {
                          Log.d("Tom & Jerry", "发送成功！");
                        }
                      }
                    });
                  }
                }
              });
        }
      }
    });
  }
}