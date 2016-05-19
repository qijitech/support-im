package support.im.aop.custom;

import android.support.v4.app.Fragment;
import support.im.aop.BaseAdvice;
import support.im.aop.Pointcut;
import support.im.data.Contact;

public class IMContactsOperation extends BaseAdvice {

  public IMContactsOperation(Pointcut pointcut) {
    super(pointcut);
  }

  public boolean onListItemClick(Fragment fragment, Contact contact) {
    return false;
  }

  public boolean onListItemLongClick(Fragment fragment, Contact contact) {
    return false;
  }
}
