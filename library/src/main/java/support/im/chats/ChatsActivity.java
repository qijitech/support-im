package support.im.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import support.im.Injection;
import support.im.R;
import support.im.data.User;
import support.im.data.source.UsersDataSource;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.utilities.AVExceptionHandler;
import support.im.utilities.DatabaseUtils;
import support.im.utilities.SupportLog;
import support.ui.SupportSinglePaneActivity;

public class ChatsActivity extends SupportSinglePaneActivity {

  protected ChatsFragment mChatsFragment;
  protected AVIMConversation mAVIMConversation;

  public static void startChatsWithMemberId(Context context, String memberId) {
    Intent intent = new Intent(context, ChatsActivity.class);
    intent.putExtra(Constants.EXTRA_MEMBER_ID, memberId);
    context.startActivity(intent);
  }

  public static void startChatsWithConversationId(Context context, String conversationId) {
    Intent intent = new Intent(context, ChatsActivity.class);
    intent.putExtra(Constants.EXTRA_CONVERSATION_ID, conversationId);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeWithIntent(getIntent());
  }

  @Override protected Fragment onCreatePane() {
    return mChatsFragment = ChatsFragment.create();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    initializeWithIntent(intent);
  }

  private void initializeWithIntent(Intent intent) {
    Bundle extras = intent.getExtras();
    if (extras != null) {
      if (extras.containsKey(Constants.EXTRA_MEMBER_ID)) {
        getConversation(extras.getString(Constants.EXTRA_MEMBER_ID));
        return;
      }
    }
  }

  protected void setupActionBar(String title) {
    setTitle(title);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayUseLogoEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(true);
    } else {
      SupportLog.d("action bar is null, so no title, please set an ActionBar style for activity");
    }
  }

  /**
   * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
   * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
   */
  private void getConversation(final String memberId) {
    Injection.provideUsersRepository(this)
        .fetchUser(memberId, new UsersDataSource.GetUserCallback() {
          @Override public void onUserLoaded(User user) {
            ChatManager.getInstance()
                .createSingleConversation(user, new AVIMConversationCreatedCallback() {
                  @Override public void done(AVIMConversation avimConversation, AVIMException e) {
                    if (AVExceptionHandler.handAVException(e)) {
                      DatabaseUtils.saveConversation(avimConversation,
                          ChatManager.getInstance().getClientId());
                      //updateConversation(avimConversation);
                    }
                  }
                });
          }

          @Override public void onUserNotFound() {
          }

          @Override public void onDataNotAvailable(AVException exception) {
          }
        });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.single_chats_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    if (itemId == R.id.menu_support_im_chats_single) {
      startActivity(new Intent(this, UserProfileActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
