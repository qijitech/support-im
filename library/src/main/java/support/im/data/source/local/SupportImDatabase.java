package support.im.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SupportImDatabase extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;

  public static final String DATABASE_NAME = "support.im.";

  public SupportImDatabase(Context context, String userId) {
    this(context, DATABASE_NAME + userId, DATABASE_VERSION);
  }

  private SupportImDatabase(Context context, String name, int version) {
    super(context, name, null, version);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    SupportImPersistenceContract.Conversations.createTableAndIndex(db);
  }

  @Override public void onOpen(SQLiteDatabase db) {
    super.onOpen(db);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    SupportImPersistenceContract.Conversations.dropTable(db);
    SupportImPersistenceContract.Conversations.createTableAndIndex(db);
  }
}
