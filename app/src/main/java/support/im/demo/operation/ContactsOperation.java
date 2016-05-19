package support.im.demo.operation;

import android.support.v4.app.Fragment;
import support.im.aop.Pointcut;
import support.im.aop.custom.IMContactsOperation;
import support.im.data.Contact;

public class ContactsOperation extends IMContactsOperation {

  public ContactsOperation(Pointcut pointcut) {
    super(pointcut);
  }

  @Override public boolean onListItemClick(Fragment fragment, Contact contact) {
    return super.onListItemClick(fragment, contact);
  }

  @Override public boolean onListItemLongClick(Fragment fragment, Contact contact) {
    return super.onListItemLongClick(fragment, contact);
  }
}
