package support.im.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVException;
import com.google.common.eventbus.Subscribe;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;
import support.im.Injection;
import support.im.R;
import support.im.chats.ChatsActivity;
import support.im.picker.adapter.CheckedContactsAdapter;
import support.im.picker.adapter.ContactsAdapter;
import support.im.picker.event.CheckEvent;
import support.im.data.Contact;
import support.im.data.User;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;
import support.im.leanclound.ChatManager;
import support.ui.components.SupportButton;

/**
 * Created by wangh on 2016-5-16-0016.
 */
public class PickerContactActivity extends AppCompatActivity
    implements ContactsDataSource.LoadContactsCallback, View.OnClickListener {

  public static final String FLAG_CLIENT_ID = "flag_client_id";
  public static final String FLAG_MEMBER_LIST = "flag_member_list";
  private RecyclerView mRVContactsList;
  private RecyclerView mRVChooseList;
  private FrameLayout mChooseContainer;
  private SupportButton mButton;

  private String mCurrentClientId;
  private List<User> mContactList;
  private ContactsAdapter mAdapter;
  private List<String> mMemberList;
  private List<User> mCheckList;
  private CheckedContactsAdapter mCheckedContactsAdapter;
  private List<User> existUserList;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_contacts);
    ButterKnife.bind(this);
    EventBus.getDefault().register(this);
    getData();
    initView();
    initContactsRecyclerView();
    initChooseRecyclerView();
  }

  private void initView() {
    mRVContactsList = ButterKnife.findById(this, R.id.support_ui_contacts_list);
    mRVChooseList = ButterKnife.findById(this, R.id.support_ui_contacts_checklist);
    mChooseContainer = ButterKnife.findById(this, R.id.support_ui_contacts_checked_list_container);
    mButton = ButterKnife.findById(this, R.id.support_ui_contacts_button);
    mButton.setClickable(true);
    mButton.setOnClickListener(this);
  }

  private void initChooseRecyclerView() {
    mCheckList = new ArrayList<>();
    mCheckedContactsAdapter = new CheckedContactsAdapter(this, mCheckList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    mRVChooseList.setAdapter(mCheckedContactsAdapter);
    mRVChooseList.setLayoutManager(linearLayoutManager);
  }

  private void initContactsRecyclerView() {
    mContactList = new ArrayList<>();
    ContactsRepository contactsRepository = Injection.provideContactsRepository(this);
    contactsRepository.getContacts(ChatManager.getInstance().getClientId(), this);
  }

  private void getData() {
    mMemberList = getIntent().getStringArrayListExtra(FLAG_MEMBER_LIST);
    existUserList = new ArrayList<>();
  }

  public static void startCheckList(Context context, String currentClientId,
      ArrayList<String> memberList) {
    Intent intent = new Intent(context, PickerContactActivity.class);
    intent.putExtra(FLAG_CLIENT_ID, currentClientId);
    intent.putStringArrayListExtra(FLAG_MEMBER_LIST, memberList);
    context.startActivity(intent);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
    EventBus.getDefault().unregister(this);
  }

  @Override public void onContactsLoaded(List<Contact> contacts) {
    for (Contact contact : contacts) {
      mContactList.add(contact.getFriend());
      for (String item : mMemberList) {
        if (contact.getFriend().getObjectId().equals(item)) {
          existUserList.add(contact.getFriend());
        }
      }
    }
    mContactList.removeAll(existUserList);
    mAdapter = new ContactsAdapter(this, mContactList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRVContactsList.setLayoutManager(linearLayoutManager);
    mRVContactsList.setAdapter(mAdapter);
  }

  @Override public void onContactsNotFound() {
    throw new RuntimeException("can not find current user contacts!");
  }

  @Override public void notLoggedIn() {
    throw new RuntimeException("you must login before load contacts!");
  }

  @Override public void onDataNotAvailable(AVException exception) {
    // TODO: 2016-5-17-0017  some tips
  }

  @SuppressLint("SetTextI18n") @Subscribe public void onEvent(CheckEvent event) {
    if (event != null) {
      if (event.isChecked) {
        mCheckList.add(mContactList.get(event.position));
        mCheckedContactsAdapter.notifyDataSetChanged();
        mButton.setText("确定(" + mCheckList.size() + ")");
        if (!mChooseContainer.isShown()) {
          mChooseContainer.setVisibility(View.VISIBLE);
        }
      } else {
        mCheckList.remove(mContactList.get(event.position));
        mCheckedContactsAdapter.notifyDataSetChanged();
        if (mCheckList.size() > 0) {
          mButton.setText("确定(" + mCheckList.size() + ")");
        } else {
          mChooseContainer.setVisibility(View.GONE);
        }
      }
    }
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.support_ui_contacts_button) {
      createGroupConversation();
    }
  }

  private void createGroupConversation() {
    ArrayList<String> groupMemberList = new ArrayList<>();
    for (String item : mMemberList) {
      groupMemberList.add(item);
    }
    for (User user : mCheckList) {
      groupMemberList.add(user.getObjectId());
    }
    ChatsActivity.startChatsWithMemberIdList(this, groupMemberList);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
