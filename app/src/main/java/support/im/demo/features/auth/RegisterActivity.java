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
import java.util.Random;
import support.im.data.SupportUser;
import support.im.demo.BaseActivity;
import support.im.demo.R;
import support.im.utilities.AVExceptionHandler;
import support.im.utilities.HudUtils;

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

  private static String[] avatarList = new String[] {
      "http://e.hiphotos.baidu.com/image/pic/item/9358d109b3de9c825d99da536981800a18d84369.jpg",
      "http://b.hiphotos.baidu.com/image/h%3D300/sign=b71da383abaf2eddcbf14fe9bd110102/bd3eb13533fa828b670a4066fa1f4134970a5a0e.jpg",
      "http://img1.imgtn.bdimg.com/it/u=2332039348,1866544533&fm=23&gp=0.jpg",
      "http://img3.imgtn.bdimg.com/it/u=180927360,2319445743&fm=23&gp=0.jpg",
      "http://img5.imgtn.bdimg.com/it/u=2159890592,3338424097&fm=11&gp=0.jpg",
      "http://imgsrc.baidu.com/baike/pic/item/f636afc379310a557c6ccd36b24543a9832610a3.jpg",
      "http://img1.imgtn.bdimg.com/it/u=262236517,3881457924&fm=23&gp=0.jpg",
      "http://img0.imgtn.bdimg.com/it/u=1710110559,3686468881&fm=23&gp=0.jpg",
      "http://img3.imgtn.bdimg.com/it/u=1312102704,3758859841&fm=23&gp=0.jpg",
      "http://img1.imgtn.bdimg.com/it/u=191339531,495729476&fm=23&gp=0.jpg"
  };

  private void doRegister() {
    final String username = mUsernameEditText.getText().toString();
    final String password = mPasswordEditText.getText().toString();
    final String nickname = mNicknameEditText.getText().toString();
    HudUtils.showHud(this, "正在注册");
    String avatar = avatarList[new Random().nextInt(avatarList.length)];
    SupportUser.register(username, password, nickname, avatar, new SignUpCallback() {
      @Override public void done(AVException e) {
        if (AVExceptionHandler.handAVException(e)) {

        }
        HudUtils.dismissHud();
      }
    });
  }
}
