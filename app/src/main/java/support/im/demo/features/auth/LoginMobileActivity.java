package support.im.demo.features.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnClick;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import support.im.demo.BaseActivity;
import support.im.demo.R;
import support.im.demo.features.main.MainActivity;
import support.im.leanclound.ChatManager;
import support.ui.utilities.HudUtils;

public class LoginMobileActivity extends BaseActivity {

  @Bind(R.id.edit_login_mobile_username) EditText mUsernameEdit;
  @Bind(R.id.edit_login_mobile_password) EditText mPasswordEdit;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_mobile);
  }

  @OnClick(R.id.btn_login_mobile) public void onClick(View view) {
    final String username = mUsernameEdit.getText().toString();
    final String password = mPasswordEdit.getText().toString();

    HudUtils.showHud(this, "正在登录");
    ChatManager.getInstance().openClient(this, username, new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        HudUtils.dismissHud();
        if (null == e) {
          finish();
          Intent intent = new Intent(LoginMobileActivity.this, MainActivity.class);
          startActivity(intent);
        } else {
        }
      }
    });
  }
}
