package support.im.provider;

public enum SupportImUriEnum {

  CONVERSATIONS(100, "conversations", SupportImContract.Conversations.CONTENT_TYPE_ID, false, SupportImDatabase.Tables.CONVERSATIONS),
  CONVERSATIONS_ID(101, "conversations/*", SupportImContract.Conversations.CONTENT_TYPE_ID, false, null),
  USERS(200, "users", SupportImContract.Users.CONTENT_TYPE_ID, false, SupportImDatabase.Tables.USERS),
  USERS_ID(101, "users/*", SupportImContract.Users.CONTENT_TYPE_ID, false, null);

  public int code;

  /**
   * The path to the {@link android.content.UriMatcher} will use to match. * may be used as a
   * wild card for any text, and # may be used as a wild card for numbers.
   */
  public String path;

  public String contentType;

  public String table;

  SupportImUriEnum(int code, String path, String contentTypeId, boolean item, String table) {
    this.code = code;
    this.path = path;
    this.contentType = item ? SupportImContract.makeContentItemType(contentTypeId)
        : SupportImContract.makeContentType(contentTypeId);
    this.table = table;
  }

}
