package support.im.demo.features.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import support.im.demo.BaseActivity;
import support.im.demo.R;
import support.im.demo.features.main.MainActivity;
import support.im.leanclound.ChatManager;

public class RegisterActivity extends BaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_mobile);
    ButterKnife.bind(this);

    ChatManager.getInstance().openClient(this, "Tom", new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
      }
    });
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
        startActivity(new Intent(this, MainActivity.class));
        break;
    }
  }
}
