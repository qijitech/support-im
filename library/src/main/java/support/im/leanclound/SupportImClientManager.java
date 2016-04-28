package support.im.leanclound;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

public class SupportImClientManager {

  private static SupportImClientManager imClientManager;

  private AVIMClient mAVImClient;
  private String mClientId;

  public synchronized static SupportImClientManager getInstance(@NonNull String clientId) {
    if (null == imClientManager) {
      imClientManager = new SupportImClientManager(clientId);
    }
    return imClientManager;
  }

  private SupportImClientManager(String clientId) {
    mClientId = clientId;
  }

  public void open(AVIMClientCallback callback) {
    mAVImClient = AVIMClient.getInstance(mClientId);
    mAVImClient.open(callback);
  }

  public AVIMClient getClient() {
    return mAVImClient;
  }

  public String getClientId() {
    if (TextUtils.isEmpty(mClientId)) {
      throw new IllegalStateException("Please call SupportImClientManager.open first");
    }
    return mClientId;
  }
}
