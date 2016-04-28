package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.leanclound.contacts.AddRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddRequestsRepository implements AddRequestsDataSource {

  private static AddRequestsRepository INSTANCE = null;
  private final AddRequestsDataSource mAddRequestsRemoteDataSource;

  // Prevent direct instantiation.
  private AddRequestsRepository(@NonNull AddRequestsDataSource addRequestsRemoteDataSource) {
    mAddRequestsRemoteDataSource = checkNotNull(addRequestsRemoteDataSource);
  }

  public static AddRequestsRepository getInstance(
      AddRequestsDataSource addRequestsRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new AddRequestsRepository(addRequestsRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override
  public void loadAddRequests(int skip, int limit, final LoadAddRequestsCallback callback) {
    mAddRequestsRemoteDataSource.loadAddRequests(skip, limit, new LoadAddRequestsCallback() {
      @Override public void onAddRequestsLoaded(List<AddRequest> addRequests) {
        if (addRequests == null || addRequests.size() == 0) {
          callback.onAddRequestsNotFound();
          return;
        }
        callback.onAddRequestsLoaded(addRequests);
      }

      @Override public void onAddRequestsNotFound() {
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  @Override public void agreeAddRequest(AddRequest addRequest, final AddRequestCallback callback) {
    mAddRequestsRemoteDataSource.agreeAddRequest(addRequest, new AddRequestCallback() {
      @Override public void onAddRequestLoaded(AddRequest addRequest) {
        callback.onAddRequestLoaded(addRequest);
      }
      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }
}
