package support.im.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVException;
import support.im.Injection;
import support.im.R;
import support.im.chats.ChatsActivity;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.im.data.source.UsersDataSource;
import support.im.leanclound.Constants;
import support.im.leanclound.contacts.AddRequestManager;
import support.ui.SupportActivity;
import support.ui.components.SupportButton;

public class UserDetailActivity extends SupportActivity implements View.OnClickListener {

  User mUser;
  String mObjectId;

  private SupportButton mAddContactsBtn;
  private SupportButton mChatBtn;

  public static void startUserDetail(Context context, String objectId) {
    Intent intent = new Intent(context, UserDetailActivity.class);
    intent.putExtra(Constants.EXTRA_OBJECT_ID, objectId);
    context.startActivity(intent);
  }

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
    mObjectId = getIntent().getStringExtra(Constants.EXTRA_OBJECT_ID);
    Injection.provideUsersRepository(this).fetchUser(mObjectId, new UsersDataSource.GetUserCallback() {
      @Override public void onUserLoaded(User user) {
        mUser = user;
      }
      @Override public void onUserNotFound() {
      }
      @Override public void onDataNotAvailable(AVException exception) {
      }
    });
  }

  @Override public void onClick(View v) {
    final int viewId = v.getId();
    if (viewId == R.id.btn_support_user_detail_add_contacts) {
      AddRequestManager.getInstance().createAddRequestInBackground(this, mUser);
      return;
    }
    if (viewId == R.id.btn_support_user_detail_chat) {
      ChatsActivity.startChatsWithMemberId(this, mObjectId);
    }
  }
}
