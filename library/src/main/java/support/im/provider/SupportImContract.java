package support.im.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import support.ui.app.SupportApp;

public class SupportImContract {

  public static final String CONTENT_TYPE_APP_BASE = "supportim.";
  public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd." + CONTENT_TYPE_APP_BASE;
  public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd." + CONTENT_TYPE_APP_BASE;

  interface ConversationsColumns {
    String CLIENT_ID = "client_id";
    String CONVERSATION_ID = "conversation_id";
    String UNREAD_COUNT = "unread_count";
    String DISPLAY_NAME = "display_name";
    String LAST_MESSAGE_TIME = "last_message_time";
  }

  interface UsersColumns {
    String USER_ID = "user_id";
    String OBJECT_ID = "object_id";
    String DISPLAY_NAME = "display_name";
    String AVATAR = "avatar";
  }

  public static final String CONTENT_AUTHORITY = "support.im.demo";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  private static final String PATH_CONVERSATIONS = "conversations";
  private static final String PATH_USER = "users";

  public static String makeContentType(String id) {
    if (id != null) {
      return CONTENT_TYPE_BASE + id;
    } else {
      return null;
    }
  }

  public static String makeContentItemType(String id) {
    if (id != null) {
      return CONTENT_ITEM_TYPE_BASE + id;
    } else {
      return null;
    }
  }

  public static class Conversations implements ConversationsColumns, BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONVERSATIONS).build();

    public static final String CONTENT_TYPE_ID = "conversations";

    /** Build {@link Uri} for requested {@link #CONVERSATION_ID}. */
    public static Uri buildConversationUri(String conversationId) {
      return CONTENT_URI.buildUpon().appendPath(conversationId).build();
    }

    /** Read {@link #CONVERSATION_ID} from {@link Conversations} {@link Uri}. */
    public static String getConversationId(Uri uri) {
      return uri.getPathSegments().get(1);
    }
  }

  public static class Users implements UsersColumns, BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

    public static final String CONTENT_TYPE_ID = "users";

    /** Build {@link Uri} for requested {@link #USER_ID}. */
    public static Uri buildUserUri(String userId) {
      return CONTENT_URI.buildUpon().appendPath(userId).build();
    }

    /** Read {@link #USER_ID} from {@link Users} {@link Uri}. */
    public static String getUserId(Uri uri) {
      return uri.getPathSegments().get(1);
    }
  }

}
