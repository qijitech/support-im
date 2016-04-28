package support.im.data.source.remote;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import java.util.List;
import support.im.data.source.AddRequestsDataSource;
import support.im.leanclound.contacts.AddRequest;
import support.im.leanclound.contacts.AddRequestManager;
import support.im.utilities.AVExceptionHandler;

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

  @Override public void agreeAddRequest(final AddRequest addRequest, final AddRequestCallback callback) {
    AddRequestManager.getInstance().agreeAddRequest(addRequest, new SaveCallback() {
      @Override
      public void done(AVException e) {
        if (!AVExceptionHandler.handAVException(e, false)) {
          callback.onDataNotAvailable(e);
          return;
        }
        callback.onAddRequestLoaded(addRequest);
      }
    });
  }
}
