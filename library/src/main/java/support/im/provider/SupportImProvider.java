package support.im.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import support.im.provider.SupportImContract.Conversations;
import support.im.provider.SupportImContract.Users;
import support.im.provider.SupportImDatabase.Tables;

public class SupportImProvider extends ContentProvider {

  private SupportImDatabase mOpenHelper;
  private SupportImProviderUriMatcher mUriMatcher;

  @Override
  public boolean onCreate() {
    mOpenHelper = new SupportImDatabase(getContext());
    mUriMatcher = new SupportImProviderUriMatcher();
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String getType(Uri uri) {
    SupportImUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
    return matchingUriEnum.contentType;
  }

  /** {@inheritDoc} */
  @Nullable @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

    SupportImUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
    switch (matchingUriEnum) {
      default: {
        // Most cases are handled with simple SelectionBuilder.
        final SelectionBuilder builder = buildExpandedSelection(uri, matchingUriEnum.code);
        boolean distinct = SupportImContractHelper.isQueryDistinct(uri);
        Cursor cursor = builder
            .where(selection, selectionArgs)
            .query(db, distinct, projection, sortOrder, null);

        Context context = getContext();
        if (null != context) {
          cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
      }
    }
  }

  /** {@inheritDoc} */
  @Nullable @Override public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    SupportImUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
    if (matchingUriEnum.table != null) {
      db.insertOrThrow(matchingUriEnum.table, null, values);
      notifyChange(uri);
    }
    switch (matchingUriEnum) {
      case CONVERSATIONS: {
        return Conversations.buildConversationUri(values.getAsString(Conversations.CONVERSATION_ID));
      }
      case USERS: {
        return Users.buildUserUri(values.getAsString(Users.USER_ID));
      }
      default: {
        throw new UnsupportedOperationException("Unknown insert uri: " + uri);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    SupportImUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
    final SelectionBuilder builder = buildSimpleSelection(uri);
    int retVal = builder.where(selection, selectionArgs).update(db, values);
    notifyChange(uri);
    return retVal;
  }

  /** {@inheritDoc} */
  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    if (uri == SupportImContract.BASE_CONTENT_URI) {
      // Handle whole database deletes (e.g. when signing out)
      deleteDatabase();
      notifyChange(uri);
      return 1;
    }
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final SelectionBuilder builder = buildSimpleSelection(uri);
    SupportImUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

    int retVal = builder.where(selection, selectionArgs).delete(db);
    notifyChange(uri);
    return retVal;
  }

  /**
   * Apply the given set of {@link ContentProviderOperation}, executing inside
   * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
   * any single one fails.
   */
  @Override
  public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
      throws OperationApplicationException {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      final int numOperations = operations.size();
      final ContentProviderResult[] results = new ContentProviderResult[numOperations];
      for (int i = 0; i < numOperations; i++) {
        results[i] = operations.get(i).apply(this, results, i);
      }
      db.setTransactionSuccessful();
      return results;
    } finally {
      db.endTransaction();
    }
  }

  private void deleteDatabase() {
    // TODO: wait for content provider operations to finish, then tear down
    mOpenHelper.close();
    Context context = getContext();
    SupportImDatabase.deleteDatabase(context);
    mOpenHelper = new SupportImDatabase(getContext());
  }

  private void notifyChange(Uri uri) {
    Context context = getContext();
    if (context == null) {
      return;
    }
    context.getContentResolver().notifyChange(uri, null);
  }

  private SelectionBuilder buildSimpleSelection(Uri uri) {
    final SelectionBuilder builder = new SelectionBuilder();
    SupportImUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
    // The main Uris, corresponding to the root of each type of Uri, do not have any selection
    // criteria so the full table is used. The others apply a selection criteria.
    switch (matchingUriEnum) {
      case CONVERSATIONS:
      case USERS:
        return builder.table(matchingUriEnum.table);
      case CONVERSATIONS_ID: {
        final String conversationId = Conversations.getConversationId(uri);
        return builder.table(Tables.CONVERSATIONS)
            .where(Conversations.CONVERSATION_ID + "=?", conversationId);
      }
      case USERS_ID: {
        final String userId = Users.getUserId(uri);
        return builder.table(Tables.USERS)
            .where(Users.USER_ID + "=?", userId);
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri for " + uri);
      }
    }
  }

  private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
    final SelectionBuilder builder = new SelectionBuilder();
    SupportImUriEnum matchingUriEnum = mUriMatcher.matchCode(match);
    if (matchingUriEnum == null) {
      throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    switch (matchingUriEnum) {
      case CONVERSATIONS: {
        return builder.table(Tables.CONVERSATIONS);
      }
      case CONVERSATIONS_ID: {
        final String conversationId = Conversations.getConversationId(uri);
        return builder.table(Tables.CONVERSATIONS)
            .where(Conversations.CONVERSATION_ID + "=?", conversationId);
      }
      case USERS: {
        return builder.table(Tables.USERS);
      }
      case USERS_ID: {
        final String userId = Users.getUserId(uri);
        return builder.table(Tables.USERS)
            .where(Users.USER_ID + "=?", userId);
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }
  }
}
