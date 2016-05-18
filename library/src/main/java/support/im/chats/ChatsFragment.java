package support.im.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.ArrayList;
import java.util.List;
import support.im.Injection;
import support.im.R;
import support.im.data.ConversationType;
import support.im.data.SupportUser;
import support.im.data.cache.CacheManager;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.location.Location;
import support.im.utilities.ConversationHelper;
import support.im.utilities.NotificationUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsFragment extends BaseChatsFragment implements ChatsContract.View {

  ChatsContract.Presenter mPresenter;
  protected String mCurrentClientId;
  protected String mUserObjectId;
  protected String mConversationId;
  protected List<String> mMemberIdList;

  public static ChatsFragment create() {
    return new ChatsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initialize();
    // TODO: may cause some bugs 
    if (!TextUtils.isEmpty(mUserObjectId) && mMemberIdList == null) {
      new ChatsPresenter(mConversationId, mUserObjectId, Injection.provideChatsRepository(),
          Injection.provideConversationsRepository(mCurrentClientId), this);
    } else if (TextUtils.isEmpty(mUserObjectId) && mMemberIdList != null) {
      new ChatsPresenter(mConversationId, mMemberIdList, Injection.provideChatsRepository(),
          Injection.provideConversationsRepository(mCurrentClientId), this);
    } else { //from conversation
      new ChatsPresenter(mConversationId, mUserObjectId, Injection.provideChatsRepository(),
          Injection.provideConversationsRepository(mCurrentClientId), this);
    }
    setHasOptionsMenu(true);
  }

  private void initialize() {
    mCurrentClientId = ChatManager.getInstance().getClientId();
    Bundle extras = getArguments();
    if (extras == null) {
      return;
    }
    if (extras.containsKey(Constants.EXTRA_MEMBER_ID)) {
      mUserObjectId = extras.getString(Constants.EXTRA_MEMBER_ID);
      return;
    }

    if (extras.containsKey(Constants.EXTRA_MEMBER_LIST_ID)) {
      mMemberIdList = extras.getStringArrayList(Constants.EXTRA_MEMBER_LIST_ID);
      return;
    }

    // 来源conversations列表,直接读取Conversation
    if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
      mConversationId = extras.getString(Constants.EXTRA_CONVERSATION_ID);
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mPresenter != null) {
      mPresenter.start();
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (null != mConversationId) {
      NotificationUtils.addTag(mConversationId);
    }
  }

  @Override public void onPause() {
    super.onPause();
    if (null != mConversationId) {
      NotificationUtils.removeTag(mConversationId);
    }
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (!TextUtils.isEmpty(mConversationId)) {
      if (CacheManager.getCacheConversation(mConversationId) != null) {
        if (CacheManager.getCacheConversation(mConversationId).getType() == 0) {
          inflater.inflate(R.menu.single_chats_menu, menu);
        } else if (CacheManager.getCacheConversation(mConversationId).getType() == 1) {
          inflater.inflate(R.menu.group_chats_menu, menu);
        } else {
          inflater.inflate(R.menu.group_chats_menu, menu);
        }
      }
    } else {
      if (mMemberIdList != null && TextUtils.isEmpty(mUserObjectId)) {
        inflater.inflate(R.menu.group_chats_menu, menu);
      } else if (!TextUtils.isEmpty(mUserObjectId) && mMemberIdList == null) {
        inflater.inflate(R.menu.single_chats_menu, menu);
      } else {
        inflater.inflate(R.menu.group_chats_menu, menu);
      }
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    if (itemId == R.id.menu_support_im_chats_single) {
      ArrayList<String> memberList = new ArrayList<>();
      if (TextUtils.isEmpty(mUserObjectId)) {
        String[] arrayMember = convertString2Array(
            CacheManager.getCacheConversation(mPresenter.getConversationId()).getMembers());
        String currentUser = AVUser.getCurrentUser(SupportUser.class).getObjectId();
        for (int i = 0; i < arrayMember.length; i++) {
          if (!currentUser.equals(arrayMember[i])) {
            memberList.add(arrayMember[i]);
          }
        }
      } else {
        memberList.add(mUserObjectId);
      }
      UserProfileActivity.startUserProfile(getActivity(), memberList.get(0),
          mPresenter.getConversationId(), memberList);
      return true;
    } else if (itemId == R.id.menu_support_im_chats_group) {

      ArrayList<String> memberList = new ArrayList<>();
      if (TextUtils.isEmpty(mUserObjectId)) {
        String[] arrayMember = convertString2Array(
            CacheManager.getCacheConversation(mPresenter.getConversationId()).getMembers());
        String currentUser = AVUser.getCurrentUser(SupportUser.class).getObjectId();
        for (int i = 0; i < arrayMember.length; i++) {
          if (!currentUser.equals(arrayMember[i])) {
            memberList.add(arrayMember[i]);
          }
        }
      } else {
        memberList.add(mUserObjectId);
      }
      GroupProfileActivity.startGroupProfile(getActivity(), memberList, mConversationId, null);
    }
    return super.onOptionsItemSelected(item);
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

  private void shouldShowDisplayName(boolean shouldShow) {
    mAdapter.shouldShowDisplayName(shouldShow);
  }

  @Override protected void onSendImage(String imageUri) {
    if (!TextUtils.isEmpty(imageUri)) {
      mPresenter.sendTextMessage("[img]" + imageUri);
    }
  }

  @Override protected void sendAudio(String audioPath) {
    if (!TextUtils.isEmpty(audioPath)) {
      mPresenter.sendAudioMessage(audioPath);
    }
  }

  @Override protected void sendImage(String imagePath) {
    mPresenter.sendImageMessage(imagePath);
  }

  @Override protected void sendLocation(Location location) {
    mPresenter.sendLocationMessage(location);
  }

  @Override protected void onSendBtnClick(String message) {
    mPresenter.sendTextMessage(message);
    //mPresenter.sendImageMessage("/storage/emulated/0/XhsEmoticonsKeyboard/Emoticons/wxemoticons/icon_002_cover.png");
  }

  ///////////////// ChatsContact View

  @Override public void updateUI(AVIMConversation avimConversation) {
    final String title = ConversationHelper.titleOfConversation(avimConversation);
    if (!TextUtils.isEmpty(title)) {
      getActivity().setTitle(title);
    }
    shouldShowDisplayName(
        ConversationHelper.typeOfConversation(avimConversation) == ConversationType.Group);
  }

  @Override public void setLoadingIndicator(boolean active) {
    mContentPresenter.displayLoadView();
  }

  @Override public void notifyItemInserted(AVIMMessage message) {
    mAdapter.add(message);
    scrollToBottom();
    mContentPresenter.displayContentView();
  }

  @Override public void notifyDataSetChanged() {
    mAdapter.notifyDataSetChanged();
    mContentPresenter.displayContentView();
  }

  @Override public void showMessages(List<AVIMMessage> messages) {
    mAdapter.addAll(messages);
    mContentPresenter.displayContentView();
    scrollToBottom();
  }

  @Override public void appendMessages(List<AVIMMessage> messages) {
    mAdapter.add(messages);
  }

  @Override public void showNoMessages() {
    if (mAdapter.isEmpty()) {
      mContentPresenter.displayEmptyView();
    }
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void onDataNotAvailable(String error, AVException exception) {
    mAdapter.notifyDataSetChanged();
    if (mAdapter.isEmpty()) {
      mContentPresenter.displayEmptyView();
    }
  }

  @Override public void setPresenter(ChatsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}
