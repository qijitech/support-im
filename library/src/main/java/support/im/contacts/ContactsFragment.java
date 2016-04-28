package support.im.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import support.im.R;
import support.im.addcontact.AddContactActivity;
import support.im.newcontacts.NewContactsActivity;
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

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.findById(view, R.id.test).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(getContext(), NewContactsActivity.class));
      }
    });
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.contact_menus, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    if (itemId == R.id.menu_add_contacts) {
      startActivity(new Intent(getContext(), AddContactActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
