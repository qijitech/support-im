package support.im.data.source.local;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class ConversationsPersistenceContract {

  // To prevent someone from accidentally instantiating the contract class,
  // give it an empty constructor.
  public ConversationsPersistenceContract() {
  }

  /* Inner class that defines the table contents */
  public static abstract class Conversations implements BaseColumns {
    public static final String TABLE_NAME = "conversations";
    public static final String COLUMN_NAME_CONVERSATION_ID = "conversation_id";
    public static final String COLUMN_NAME_UNREAD_COUNT = "unread_count";

    public static final String CONVERSATION_ID_INDEX = "conversation_id_index";

    static class SQL {
      static final String CREATE_CONVERSATIONS =
          "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
              _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
              COLUMN_NAME_CONVERSATION_ID + " VARCHAR(63) UNIQUE NOT NULL, " +
              COLUMN_NAME_UNREAD_COUNT + " INTEGER DEFAULT 0" +
              " )";

      static final String CREATE_CONVERSATION_ID_INDEX =
          "CREATE UNIQUE INDEX IF NOT EXISTS " + CONVERSATION_ID_INDEX + " on " +
              TABLE_NAME + " ( " + COLUMN_NAME_CONVERSATION_ID +
              " ) ";

      static final String UPDATE_CONVERSATIONS_INCREASE_UNREAD_COUNT_WHERE_CONVERSATION_ID =
          "UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME_UNREAD_COUNT + " = " +
              COLUMN_NAME_UNREAD_COUNT + " + 1 WHERE " + COLUMN_NAME_CONVERSATION_ID + " =?";

      static final String DROP_CONVERSATIONS = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static void createTableAndIndex(SQLiteDatabase db) {
      db.execSQL(SQL.CREATE_CONVERSATIONS);
      db.execSQL(SQL.CREATE_CONVERSATION_ID_INDEX);
    }

    static void dropTable(SQLiteDatabase db) {
      db.execSQL(SQL.DROP_CONVERSATIONS);
    }
  }

}
