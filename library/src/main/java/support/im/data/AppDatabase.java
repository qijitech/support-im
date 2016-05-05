package support.im.data;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = AppDatabase.NAME,
    version = AppDatabase.VERSION,
    foreignKeysSupported = true)
public class AppDatabase {

  public static final String NAME = "support_im_db";

  public static final int VERSION = 1;
}