package support.im.conversations;

import java.util.Comparator;
import support.im.data.Conversation;

public class ConversationsComparator implements Comparator<Conversation> {

  @Override public int compare(Conversation lhs, Conversation rhs) {
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
