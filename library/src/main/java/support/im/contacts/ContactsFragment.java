package support.im.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVException;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import java.util.ArrayList;
import java.util.List;
import support.im.Injection;
import support.im.R;
import support.im.SupportRecyclerViewFragment;
import support.im.addcontact.AddContactActivity;
import support.im.data.Contact;
import support.im.detail.UserDetailActivity;
import support.im.events.InvitationEvent;
import support.im.leanclound.ChatManager;
import support.im.leanclound.contacts.AddRequestManager;
import support.im.newcontacts.NewContactsActivity;
import support.im.widget.SideBar;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsFragment extends SupportRecyclerViewFragment implements ContactsContract.View {

  ContactsContract.Presenter mPresenter;

  protected ContactsAdapter mAdapter;
  private ContactsDummy mNewContacts;
  private ContactsDummy mGroup;
  private SideBar mSideBar;
  private TextView mBubble;

  public static ContactsFragment create() {
    return new ContactsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.contacts;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mNewContacts = ContactsDummy.newContacts();
    mGroup = ContactsDummy.group();

    mAdapter = new ContactsAdapter(getContext());
    mAdapter.setOnClickListener(this);
    mAdapter.bind(ContactsDummy.class, ContactsDummyViewHolder.class);
    mAdapter.bind(ContactsTotal.class, ContactsTotalViewHolder.class);
    mAdapter.bind(Contact.class, ContactsViewHolder.class);

    if (ChatManager.getInstance().isLogin()) {
      new ContactsPresenter(ChatManager.getInstance().getClientId(),
          Injection.provideContactsRepository(getContext()), this);
    }

    setHasOptionsMenu(true);
  }

  protected void setAdapter() {
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onResume() {
    super.onResume();
    getActivity().setTitle(R.string.support_im_contacts_title);
    if (mPresenter != null) {
      mPresenter.start();
    } else {
      showNotLoggedIn();
    }
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mSideBar = ButterKnife.findById(view, R.id.support_ui_sidebar);
    mBubble = ButterKnife.findById(view, R.id.support_ui_view_bubble);
    mSideBar.setBubble(mBubble);
    ArrayList<String> charList = new ArrayList<>();
    List<Object> contactList = mAdapter.getItems();
    if (contactList != null) {
      for (int i = 0; i < contactList.size(); i++) {
        Object o = contactList.get(i);
        if (o instanceof Contact) {
          if (!charList.contains(((Contact) o).getSortLetters())) {
            charList.add(((Contact) o).getSortLetters());
          }
        }
      }
    }
    mSideBar.setUpCharList(charList);
    final StickyRecyclerHeadersDecoration headersDecor =
        new StickyRecyclerHeadersDecoration(mAdapter);
    mRecyclerView.addItemDecoration(headersDecor);
    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        headersDecor.invalidateHeaders();
      }
    });
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.contacts_menu, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    if (itemId == R.id.menu_support_im_contacts_add) {
      showMorePopUpMenu();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showMorePopUpMenu() {
    PopupMenu popup =
        new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_support_im_contacts_add));
    popup.getMenuInflater().inflate(R.menu.contacts_menu_add, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        final int menuId = item.getItemId();
        if (menuId == R.id.menu_support_im_add_contact) {
          startActivity(new Intent(getContext(), AddContactActivity.class));
          return true;
        }
        return false;
      }
    });

    popup.show();
  }

  @Override public void onItemClick(int position, View view) {
    Object object = mAdapter.get(position);
    if (object instanceof Contact) {
      Contact contact = (Contact) object;
      UserDetailActivity.startUserDetail(getContext(), contact.getFriend().getObjectId());
      return;
    }
    if (object instanceof ContactsDummy) {
      ContactsDummy contactsDummy = (ContactsDummy) object;
      switch (contactsDummy.mTag) {
        case ContactsDummy.NEW_CONTACTS:
          startActivity(new Intent(getContext(), NewContactsActivity.class));
          break;
        case ContactsDummy.GROUP:
          break;
      }
    }
  }

  @Override public void setLoadingIndicator(boolean active) {
    contentPresenter.displayLoadView();
  }

  @Override public void showContacts(List<Contact> contacts) {
    mAdapter.addAll(contacts);
    addCommonData(contacts.size());
    contentPresenter.displayContentView();
  }

  @Override public void showNotLoggedIn() {
    mAdapter.clear();
    addCommonData(0);
    contentPresenter.displayContentView();
  }

  @Override public void onDataNotAvailable(AVException exception) {
    mAdapter.clear();
    addCommonData(0);
    contentPresenter.displayContentView();
  }

  @Override public void showNoContacts() {
    mAdapter.clear();
    addCommonData(0);
    contentPresenter.displayContentView();
  }

  private void addCommonData(int size) {
    mAdapter.add(mNewContacts, 0);
    mAdapter.add(mGroup, 1);
    mAdapter.add(new ContactsTotal(size));
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void setPresenter(ContactsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  public void onEvent(InvitationEvent event) {
    AddRequestManager.getInstance().unreadRequestsIncrement();
    // TODO
    //updateNewRequestBadge();
  }
}
