package support.im.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.SimpleUser;
import support.im.data.cache.CacheManager;
import support.im.leanclound.Constants;
import support.im.leanclound.contacts.AddRequestManager;
import support.ui.SupportActivity;
import support.ui.components.SupportButton;

public class UserDetailActivity extends SupportActivity implements View.OnClickListener {

  SimpleUser mUser;
  String mUserId;

  private SupportButton mAddContactsBtn;
  private SupportButton mChatBtn;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_detail);
    setupViews();
    setupListeners();
    initialize();
  }

  private void setupViews() {
    mAddContactsBtn = ButterKnife.findById(this, R.id.btn_support_user_detail_add_contacts);
    mChatBtn = ButterKnife.findById(this, R.id.btn_support_user_detail_chat);
  }

  private void setupListeners() {
    mAddContactsBtn.setOnClickListener(this);
    mChatBtn.setOnClickListener(this);
  }

  private void initialize() {
    mUserId = getIntent().getStringExtra(Constants.EXTRA_USER_ID);
    mUser = CacheManager.getInstance().getCacheSimpleUser(mUserId);
  }

  @Override public void onClick(View v) {
    final int viewId = v.getId();
    if (viewId == R.id.btn_support_user_detail_add_contacts) {
      AddRequestManager.getInstance().createAddRequestInBackground(this, mUser);
      return;
    }
    if (viewId == R.id.btn_support_user_detail_chat) {
      return;
    }
  }
}
