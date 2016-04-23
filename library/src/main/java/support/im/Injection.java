package support.im;

import android.content.Context;
import android.support.annotation.NonNull;
import support.im.data.source.MobileContactsDataSource;
import support.im.data.source.MobileContactsRepository;
import support.im.data.source.local.MobileContactsLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

  public static MobileContactsDataSource provideMobileContactsRepository(@NonNull Context context) {
    checkNotNull(context);
    return MobileContactsRepository.getInstance(MobileContactsLocalDataSource.getInstance(context));
  }
}
