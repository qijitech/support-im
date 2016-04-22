package support.im.conversations;

import support.im.R;
import support.ui.SupportFragment;

public class ConversationsFragment extends SupportFragment {

  public static ConversationsFragment create() {
    return new ConversationsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.conversations;
  }

  @Override public void onResume() {
    super.onResume();
    getActivity().setTitle(R.string.support_im_conversations_title);
  }
}
