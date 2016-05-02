package support.im;

import android.content.Context;
import android.support.annotation.NonNull;
import support.im.data.source.AddRequestsRepository;
import support.im.data.source.ChatsRepository;
import support.im.data.source.ContactsRepository;
import support.im.data.source.ConversationsRepository;
import support.im.data.source.MobileContactsRepository;
import support.im.data.source.UsersRepository;
import support.im.data.source.local.ConversationsLocalDataSource;
import support.im.data.source.local.MobileContactsLocalDataSource;
import support.im.data.source.local.UsersLocalDataSource;
import support.im.data.source.remote.AddRequestsRemoteDataSource;
import support.im.data.source.remote.ChatsRemoteDataSource;
import support.im.data.source.remote.ContactsRemoteDataSource;
import support.im.data.source.remote.ConversationsRemoteDataSource;
import support.im.data.source.remote.UsersRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

  public static MobileContactsRepository provideMobileContactsRepository(@NonNull Context context) {
    checkNotNull(context);
    return MobileContactsRepository.getInstance(MobileContactsLocalDataSource.getInstance(context));
  }

  public static ConversationsRepository provideConversationsRepository(@NonNull Context context) {
    checkNotNull(context);
    return ConversationsRepository.getInstance(ConversationsLocalDataSource.getInstance(context),
        ConversationsRemoteDataSource.getInstance());
  }

  public static UsersRepository provideUsersRepository(@NonNull Context context) {
    checkNotNull(context);
    return UsersRepository.getInstance(UsersLocalDataSource.getInstance(),
        UsersRemoteDataSource.getInstance());
  }

  public static AddRequestsRepository provideAddRequestsRepository(@NonNull Context context) {
    checkNotNull(context);
    return AddRequestsRepository.getInstance(AddRequestsRemoteDataSource.getInstance());
  }

  public static ContactsRepository provideContactsRepository(@NonNull Context context) {
    checkNotNull(context);
    return ContactsRepository.getInstance(ContactsRemoteDataSource.getInstance(),
        provideUsersRepository(context));
  }

  public static ChatsRepository provideChatsRepository() {
    return ChatsRepository.getInstance(ChatsRemoteDataSource.getInstance());
  }
}
