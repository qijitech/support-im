package support.im.chats;

import support.im.R;
import support.ui.SupportFragment;

public class ChatsFragment extends SupportFragment {

  public static ChatsFragment create() {
    return new ChatsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.chats;
  }
}
