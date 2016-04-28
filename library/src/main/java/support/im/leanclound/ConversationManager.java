package support.im.leanclound;

public class ConversationManager {

  private static ConversationManager conversationManager;

  public ConversationManager() {
  }

  public static synchronized ConversationManager getInstance() {
    if (conversationManager == null) {
      conversationManager = new ConversationManager();
    }
    return conversationManager;
  }

}
