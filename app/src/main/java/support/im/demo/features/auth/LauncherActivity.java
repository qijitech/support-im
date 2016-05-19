package support.im.demo.features.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import support.im.data.SupportUser;
import support.im.demo.R;
import support.im.demo.features.main.MainActivity;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;
import support.ui.app.SupportActivity;

public class LauncherActivity extends SupportActivity {

  private static final int SPLASH_DURATION = 2000;
  private static final int GO_MAIN_MSG = 1;
  private static final int GO_LOGIN_MSG = 2;

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case GO_MAIN_MSG:
          imLogin();
          break;
        case GO_LOGIN_MSG:
          Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
          LauncherActivity.this.startActivity(intent);
          finish();
          break;
      }
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.launchers);
    if (SupportUser.getCurrentUser() != null) {
      SupportUser.getCurrentUser().updateUserInstallation();
      handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
    } else {
      handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
    }
  }

  private void imLogin() {
    ChatManager.getInstance().openClient(SupportUser.getCurrentUser().getObjectId(), new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (AVExceptionHandler.handAVException(e)) {
          Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      }
    });
  }

}
