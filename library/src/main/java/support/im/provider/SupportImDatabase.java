package support.im.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import support.im.provider.SupportImContract.ConversationsColumns;
import support.im.provider.SupportImContract.UsersColumns;

public class SupportImDatabase extends SQLiteOpenHelper {

  public static final int VER_2016_DEV = 1;

  private static final int CUR_DATABASE_VERSION = VER_2016_DEV;

  public static final String DATABASE_NAME = "support.im";

  public SupportImDatabase(Context context) {
    this(context, DATABASE_NAME, CUR_DATABASE_VERSION);
  }

  private SupportImDatabase(Context context, String name, int version) {
    super(context, name, null, version);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + Tables.CONVERSATIONS + " ("
        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + ConversationsColumns.CLIENT_ID + " TEXT NOT NULL,"
        + ConversationsColumns.CONVERSATION_ID + " TEXT NOT NULL,"
        + ConversationsColumns.UNREAD_COUNT + " INTEGER DEFAULT 0,"
        + ConversationsColumns.DISPLAY_NAME + " TEXT NOT NULL,"
        + ConversationsColumns.LAST_MESSAGE_TIME + " INTEGER DEFAULT 0,"
        + "UNIQUE (" + ConversationsColumns.CONVERSATION_ID + ") ON CONFLICT REPLACE)");

    db.execSQL("CREATE TABLE " + Tables.USERS + " ("
        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + UsersColumns.USER_ID + " TEXT NOT NULL,"
        + UsersColumns.OBJECT_ID + " TEXT NOT NULL,"
        + UsersColumns.DISPLAY_NAME + " TEXT,"
        + UsersColumns.AVATAR + " TEXT NOT NULL,"
        + "UNIQUE (" + UsersColumns.USER_ID + ") ON CONFLICT REPLACE)");
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Current DB version. We update this variable as we perform upgrades to reflect
    // the current version we are in.
    int version = oldVersion;

    if (version != CUR_DATABASE_VERSION) {
      db.execSQL("DROP TABLE IF EXISTS " + Tables.CONVERSATIONS);
      db.execSQL("DROP TABLE IF EXISTS " + Tables.USERS);
      onCreate(db);
    }
  }

  interface Tables {
    String CONVERSATIONS = "conversations";
    String USERS = "users";
  }

  static void deleteDatabase(Context context) {
    context.deleteDatabase(DATABASE_NAME);
  }

}
