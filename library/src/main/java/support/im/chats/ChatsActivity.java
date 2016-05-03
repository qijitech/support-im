package support.im.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import support.im.data.ConversationType;
import support.im.data.SimpleUser;
import support.im.data.SupportUser;
import support.im.data.cache.CacheManager;
import support.im.database.SupportsDbHelper;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.utilities.AVExceptionHandler;
import support.im.utilities.ConversationHelper;
import support.im.utilities.SupportLog;
import support.ui.SupportSinglePaneActivity;
import support.ui.app.SupportApp;

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
      if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
        String conversationId = extras.getString(Constants.EXTRA_CONVERSATION_ID);
        updateConversation(AVIMClient.getInstance(ChatManager.getInstance().getClientId()).getConversation(conversationId));
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

  protected void updateConversation(AVIMConversation conversation) {
    if (null != conversation) {
      mAVIMConversation = conversation;
      mChatsFragment.setConversation(conversation);
      mChatsFragment.shouldShowDisplayName(ConversationHelper.typeOfConversation(conversation) != ConversationType.Single);
      setupActionBar(ConversationHelper.titleOfConversation(conversation));
    }
  }

  /**
   * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
   * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
   */
  private void getConversation(final String memberId) {
    SimpleUser simpleUser = CacheManager.getInstance().getCacheSimpleUser(memberId);
    ChatManager.getInstance().createSingleConversation(simpleUser, new AVIMConversationCreatedCallback() {
      @Override
      public void done(AVIMConversation avimConversation, AVIMException e) {
        if (AVExceptionHandler.handAVException(e)) {
          SupportsDbHelper.dbHelper(SupportApp.appContext(), ChatManager.getInstance().getClientId()).saveOrUpdate(avimConversation.getConversationId());
          updateConversation(avimConversation);
        }
      }
    });
  }
}
