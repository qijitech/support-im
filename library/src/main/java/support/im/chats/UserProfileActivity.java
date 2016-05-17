package support.im.chats;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.avos.avoscloud.AVUser;
import com.google.common.eventbus.Subscribe;
import com.raizlabs.android.dbflow.annotation.NotNull;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
import support.im.R;
import support.im.choose.CheckContactActivity;
import support.im.data.IconItem;
import support.im.data.SupportUser;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;
import support.ui.app.SupportCellsActivity;
import support.ui.cells.CellModel;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class UserProfileActivity extends SupportCellsActivity
    implements EasyViewHolder.OnItemClickListener {
  public final static String TAG = UserProfileActivity.class.getSimpleName();
  public final static int TOP_CONVERSATION = 1;
  public final static int NO_NOTIFY = 2;
  public final static int MSG_FILE_CACHE = 3;
  public final static int QUERY_MSG_LOG = 4;

  public static final String FLAG_USER_ID = "flag_user_id";
  public static final String FLAG_CONVERSATION_ID = "flag_conversation_id";
  public static final String FLAG_MEMBER_ID = "flag_member_id";

  private String mUserId;
  private String mConversationId;
  private String mMemberId;

  // TODO: 2016-5-17-0017 Just Support intent from contact Activity 
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    getData();
    getAdapter().bind(User.class, UserHeadCell.class);
    getAdapter().bind(IconItem.class, IconItemCell.class);
    addItem(CacheManager.getCacheUser(mUserId));
    addItem(new IconItem());
    addItem(CellModel.shadowCell().build());
    appendAll(buildData());
  }

  private void getData() {
    mUserId = getIntent().getStringExtra(FLAG_USER_ID);
    mConversationId = getIntent().getStringExtra(FLAG_CONVERSATION_ID);
    mMemberId = getIntent().getStringExtra(FLAG_MEMBER_ID);
    if (TextUtils.isEmpty(mUserId) || (TextUtils.isEmpty(mMemberId) && TextUtils.isEmpty(
        mConversationId))) {
      throw new IllegalArgumentException(
          "必须传递以flag_user_id为键的值，并且flag_conversation_id、flag_member_id至少一个不为空");
    }
  }

  @Override protected void onItemClick(Object object) {
    if (object instanceof CellModel) {
      switch (((CellModel) object).tag) {
        case TOP_CONVERSATION:
          Log.d(TAG, "置顶会话");
          break;
        case NO_NOTIFY:
          Log.d(TAG, "消息免打扰");
          break;
        case MSG_FILE_CACHE:
          Log.d(TAG, "聊天文件和图片");
          break;
        case QUERY_MSG_LOG:
          Log.d(TAG, "查询聊天记录");
          break;
      }
    } else if (object instanceof User) {
      Log.d(TAG, "头像点击");
    }
  }

  private ArrayList<CellModel> buildData() {
    ArrayList<CellModel> items = new ArrayList<>();
    Resources res = SupportApp.appResources();
    items.add(
        CellModel.checkCell(res.getString(R.string.support_im_label_chats_single_top_conversation))
            .tag(TOP_CONVERSATION)
            .needDivider(true)
            .checked(false)
            .build());
    items.add(
        CellModel.checkCell(res.getString(R.string.support_im_label_chats_single_msg_no_notify))
            .tag(NO_NOTIFY)
            .checked(false)
            .build());

    items.add(CellModel.shadowCell().build());
    items.add(CellModel.settingCell(res.getString(R.string.support_im_label_chats_single_msg_cache))
        .tag(MSG_FILE_CACHE)
        .needDivider(true)
        .build());
    items.add(CellModel.settingCell(res.getString(R.string.support_im_label_chats_single_chat_log))
        .tag(QUERY_MSG_LOG)
        .build());
    items.add(CellModel.shadowBottomCell().build());
    return items;
  }

  @Subscribe public void onEvent(IconItem item) {
    //check null for memberId and conversationId
    if (!TextUtils.isEmpty(mConversationId)) {

      support.im.data.Conversation conversation =
          CacheManager.getCacheConversation(mConversationId);
      conversation.getMembers();
      //convert string to list
      String srcMember = conversation.getMembers();
      String[] arrayMember = convertString2Array(srcMember);
      ArrayList<String> memberList = new ArrayList<>();
      String currentUser = AVUser.getCurrentUser(SupportUser.class).getObjectId();
      for (int i = 0; i < arrayMember.length; i++) {
        if (!currentUser.equals(arrayMember[i])) {
          memberList.add(arrayMember[i]);
        }
      }
      CheckContactActivity.startCheckList(this, null, memberList);
    } else if (!TextUtils.isEmpty(mMemberId)) {
      CheckContactActivity.startCheckList(this, null,
          new ArrayList<String>(Arrays.asList(mMemberId)));
    } else {
      throw new IllegalArgumentException("memberId and Conversation Id are null");
    }
  }

  /**
   * just for memberId
   */
  public String[] convertString2Array(String str) {
    String[] arrayMember = str.split("\",\"");
    arrayMember[0] = arrayMember[0].substring(2);
    arrayMember[arrayMember.length - 1] = arrayMember[arrayMember.length - 1].substring(0,
        arrayMember[arrayMember.length - 1].length() - 2);
    return arrayMember;
  }

  public static void startUserProfile(@NotNull Context context, @NotNull String userId,
      @Nullable String conversationId, @Nullable ArrayList<String> memberId) {
    Intent intent = new Intent(context, UserProfileActivity.class);
    intent.putExtra(FLAG_USER_ID, userId);
    intent.putExtra(FLAG_CONVERSATION_ID, conversationId);
    intent.putStringArrayListExtra(FLAG_MEMBER_ID, memberId);
    context.startActivity(intent);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
