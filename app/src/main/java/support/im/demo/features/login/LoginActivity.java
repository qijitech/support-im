package support.im.demo.features.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import support.im.demo.R;
import support.im.demo.features.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

  @Bind(R.id.btn_login_agreement) AppCompatButton mAgreementBtn;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    ButterKnife.bind(this);
    setTitle(R.string.label_login);
    mAgreementBtn.setPaintFlags(mAgreementBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
  }

  @OnClick({
      R.id.btn_login_mobile
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login_mobile:
        startActivity(new Intent(this, RegisterActivity.class));
        break;
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }
}
