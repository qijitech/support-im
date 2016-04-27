package support.im.demo.features.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import support.im.data.SupportUser;
import support.im.demo.BaseActivity;
import support.im.demo.R;
import support.im.utilities.AVExceptionHandler;
import support.ui.utilities.HudUtils;

public class RegisterActivity extends BaseActivity {

  @Bind(R.id.edit_register_username) EditText mUsernameEditText;
  @Bind(R.id.edit_register_password) EditText mPasswordEditText;
  @Bind(R.id.edit_register_nickname) EditText mNicknameEditText;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register);
    ButterKnife.bind(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @OnClick({
      R.id.btn_login_mobile_next
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login_mobile_next:
        doRegister();
        break;
    }
  }

  private void doRegister() {
    final String username = mUsernameEditText.getText().toString();
    final String password = mPasswordEditText.getText().toString();
    final String nickname = mNicknameEditText.getText().toString();
    HudUtils.showHud(this, "正在注册");
    SupportUser.register(username, password, nickname, new SignUpCallback() {
      @Override public void done(AVException e) {
        if (AVExceptionHandler.handAVException(RegisterActivity.this, e)) {

        }
        HudUtils.dismissHud();
      }
    });
  }
}
