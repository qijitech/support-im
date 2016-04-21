package support.im.conversations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import support.im.R;

public class ConversationsFragment extends Fragment {

  public static ConversationsFragment create() {
    return new ConversationsFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.conversations, container, false);
  }
}
