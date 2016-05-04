package support.im.provider;

import android.net.Uri;
import android.text.TextUtils;

public class SupportImContractHelper {

  public static final String QUERY_PARAMETER_DISTINCT = "distinct";

  public static boolean isQueryDistinct(Uri uri){
    return !TextUtils.isEmpty(uri.getQueryParameter(QUERY_PARAMETER_DISTINCT));
  }

}
