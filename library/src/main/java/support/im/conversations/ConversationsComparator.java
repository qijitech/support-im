package support.im.conversations;

import java.util.Comparator;
import support.im.data.ConversationModel;

public class ConversationsComparator implements Comparator<ConversationModel> {

  @Override public int compare(ConversationModel lhs, ConversationModel rhs) {
    long value = lhs.getLastModifyTime() - rhs.getLastModifyTime();
    if (value > 0) {
      return -1;
    }
    if (value < 0) {
      return 1;
    }
    return 0;
  }
}
