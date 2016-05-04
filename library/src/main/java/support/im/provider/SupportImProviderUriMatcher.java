package support.im.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.util.SparseArray;

public class SupportImProviderUriMatcher {

  /**
   * All methods on a {@link UriMatcher} are thread safe, except {@code addURI}.
   */
  private UriMatcher mUriMatcher;

  private SparseArray<SupportImUriEnum> mEnumsMap = new SparseArray<>();

  /**
   * This constructor needs to be called from a thread-safe method as it isn't thread-safe itself.
   */
  public SupportImProviderUriMatcher(){
    mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    buildUriMatcher();
  }

  private void buildUriMatcher() {
    final String authority = SupportImContract.CONTENT_AUTHORITY;

    SupportImUriEnum[] uris = SupportImUriEnum.values();
    for (int i = 0; i < uris.length; i++) {
      mUriMatcher.addURI(authority, uris[i].path, uris[i].code);
    }
    buildEnumsMap();
  }

  private void buildEnumsMap() {
    SupportImUriEnum[] uris = SupportImUriEnum.values();
    for (int i = 0; i < uris.length; i++) {
      mEnumsMap.put(uris[i].code, uris[i]);
    }
  }

  /**
   * Matches a {@code uri} to a {@link SupportImUriEnum}.
   *
   * @return the {@link SupportImUriEnum}, or throws new UnsupportedOperationException if no match.
   */
  public SupportImUriEnum matchUri(Uri uri){
    final int code = mUriMatcher.match(uri);
    try {
      return matchCode(code);
    } catch (UnsupportedOperationException e){
      throw new UnsupportedOperationException("Unknown uri " + uri);
    }
  }

  /**
   * Matches a {@code code} to a {@link SupportImUriEnum}.
   *
   * @return the {@link SupportImUriEnum}, or throws new UnsupportedOperationException if no match.
   */
  public SupportImUriEnum matchCode(int code){
    SupportImUriEnum scheduleUriEnum = mEnumsMap.get(code);
    if (scheduleUriEnum != null){
      return scheduleUriEnum;
    } else {
      throw new UnsupportedOperationException("Unknown uri with code " + code);
    }
  }

}
