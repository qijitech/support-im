package support.im.leanclound;

import android.text.TextUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

public class SupportImClientManager {

  private static SupportImClientManager imClientManager;

  private AVIMClient mAVImClient;
  private String mClientId;

  public synchronized static SupportImClientManager getInstance() {
    if (null == imClientManager) {
      imClientManager = new SupportImClientManager();
    }
    return imClientManager;
  }

  private SupportImClientManager() {
  }

  public void open(String clientId, AVIMClientCallback callback) {
    mClientId = clientId;
    mAVImClient = AVIMClient.getInstance(clientId);
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
