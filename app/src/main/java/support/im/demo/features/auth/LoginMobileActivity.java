package support.im.demo.features.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnClick;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import support.im.data.SupportUser;
import support.im.demo.BaseActivity;
import support.im.demo.R;
import support.im.demo.features.main.MainActivity;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;
import support.ui.utilities.HudUtils;
import support.ui.utilities.ToastUtils;

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

    if (TextUtils.isEmpty(username.trim())) {
      ToastUtils.toast(R.string.support_im_error_enter_username);
      return;
    }

    if (TextUtils.isEmpty(password.trim())) {
      ToastUtils.toast(R.string.support_im_error_enter_password);
      return;
    }

    HudUtils.showHud(this, "正在登录");
    SupportUser.logInInBackground(username, password, new LogInCallback<SupportUser>() {
      @Override public void done(SupportUser avUser, AVException e) {
        HudUtils.dismissHud();
        if (AVExceptionHandler.handAVException(e)) {
          ChatManager.getInstance().openClient(avUser.getObjectId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
              HudUtils.dismissHud();
              if (AVExceptionHandler.handAVException(e)) {
                Intent intent = new Intent(LoginMobileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
              }
            }
          });
        }
      }
    }, SupportUser.class);

  }
}
