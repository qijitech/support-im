package support.im.demo.features.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import butterknife.Bind;
import butterknife.OnClick;
import support.im.demo.R;
import support.im.demo.SupportFragment;
import support.im.demo.features.register.RegisterActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginFragment extends SupportFragment implements LoginContract.View {

  @Bind(R.id.btn_login_agreement) AppCompatButton mAgreementBtn;

  private LoginContract.Presenter mPresenter;

  public static LoginFragment create() {
    return new LoginFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.login;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAgreementBtn.setPaintFlags(mAgreementBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
  }

  @SuppressWarnings("unused") @OnClick({
      R.id.btn_login_mobile
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login_mobile:
        mPresenter.loginWithMobile();
        break;
    }
  }

  @Override public void setPresenter(LoginContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public void showLoginMobileUi() {
    Intent intent = new Intent(getContext(), RegisterActivity.class);
    startActivity(intent);
  }
}
