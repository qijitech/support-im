package support.im.data.source.remote;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import java.util.List;
import support.im.data.source.AddRequestsDataSource;
import support.im.leanclound.contacts.AddRequest;
import support.im.leanclound.contacts.AddRequestManager;

public class AddRequestsRemoteDataSource implements AddRequestsDataSource {

  private static AddRequestsRemoteDataSource INSTANCE = null;

  public static AddRequestsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AddRequestsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override public void loadAddRequests(int skip, int limit, final LoadAddRequestsCallback callback) {
    AddRequestManager.getInstance().findAddRequests(skip, limit, new FindCallback<AddRequest>() {
      @Override public void done(List<AddRequest> addRequests, AVException e) {
        callback.onAddRequestsLoaded(addRequests);
      }
    });
  }
}
