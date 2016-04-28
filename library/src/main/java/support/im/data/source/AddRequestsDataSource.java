package support.im.data.source;

import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.leanclound.contacts.AddRequest;

public interface AddRequestsDataSource {

  interface LoadAddRequestsCallback {
    void onAddRequestsLoaded(List<AddRequest> addRequests);

    void onAddRequestsNotFound();

    void onDataNotAvailable(AVException exception);
  }

  void loadAddRequests(int skip, int limit, LoadAddRequestsCallback callback);
}
