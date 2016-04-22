package support.im.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import support.im.R;
import support.im.addcontact.AddContactActivity;
import support.ui.SupportFragment;

public class ContactsFragment extends SupportFragment {

  public static ContactsFragment create() {
    return new ContactsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.contacts;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onResume() {
    super.onResume();
    getActivity().setTitle(R.string.support_im_contacts_title);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.contact_menus, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    if (itemId == R.id.menu_contact_add) {
      startActivity(new Intent(getContext(), AddContactActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
