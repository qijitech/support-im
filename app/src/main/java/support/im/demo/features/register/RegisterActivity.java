package support.im.demo.features.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import support.im.demo.R;

public class RegisterActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_mobile);
    ButterKnife.bind(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @OnClick({
      R.id.container_login_mobile_code
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.container_login_mobile_code:
        break;
    }
  }
}
