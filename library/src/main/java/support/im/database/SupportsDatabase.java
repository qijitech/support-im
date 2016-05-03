package support.im.database;

import android.content.Context;
import com.yahoo.squidb.data.SquidDatabase;
import com.yahoo.squidb.data.adapter.SQLiteDatabaseWrapper;
import com.yahoo.squidb.sql.Index;
import com.yahoo.squidb.sql.Table;
import support.im.data.Conversation2;

/**
 * Implementation of SquidDatabase for this tasks app. Remember--instances of your SquidDatabase
 * subclass should always be singletons!
 */
public class SupportsDatabase extends SquidDatabase {

  private static final int VERSION = 1;
  public static final String DATABASE_NAME = "supports_im_";

  private String mClientId;

  public SupportsDatabase(Context context, String clientId) {
    super(context);
    mClientId = clientId;
  }

  @Override public String getName() {
    return DATABASE_NAME + mClientId + ".db";
  }

  @Override protected int getVersion() {
    return VERSION;
  }

  @Override protected Table[] getTables() {
    // conversation_id_index
    return new Table[] {
        Conversation2.TABLE
    };
  }

  @Override protected Index[] getIndexes() {
    return new Index[] {
        Conversation2.TABLE.index("conversation_id_index", Conversation2.CONVERSATION_ID),
    };
  }

  @Override protected boolean onUpgrade(SQLiteDatabaseWrapper db, int oldVersion, int newVersion) {
    switch (oldVersion) {
      case 1:
        tryCreateTable(Conversation2.TABLE);
        tryCreateIndex(Conversation2.TABLE.index("conversation_id_index", Conversation2.CONVERSATION_ID));
    }
    return false;
  }
}
