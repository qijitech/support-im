package support.im;

import android.content.Context;
import android.support.annotation.NonNull;
import support.im.data.source.ConversationsDataSource;
import support.im.data.source.ConversationsRepository;
import support.im.data.source.MobileContactsRepository;
import support.im.data.source.local.ConversationsLocalDataSource;
import support.im.data.source.local.MobileContactsLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

  public static MobileContactsRepository provideMobileContactsRepository(@NonNull Context context) {
    checkNotNull(context);
    return MobileContactsRepository.getInstance(MobileContactsLocalDataSource.getInstance(context));
  }

  public static ConversationsRepository provideConversationsRepository(@NonNull Context context) {
    checkNotNull(context);
    return ConversationsRepository.getInstance(ConversationsLocalDataSource.getInstance(context));
  }
}
