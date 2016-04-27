package support.im.demo.features.auth;

import support.im.BasePresenter;
import support.im.BaseView;

public interface LoginContract {

  interface View extends BaseView<Presenter> {
    void showLoginMobileUi();
    void showRegisterUi();
  }

  interface Presenter extends BasePresenter {
    /**
     * 第三方登录
     */
    void loginWithPlatform(PlatformType platformType);

    /**
     * 手机号登录
     */
    void loginWithMobile();

    void register();
  }
}
