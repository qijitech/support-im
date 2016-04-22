package support.im.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import support.im.demo.data.source.AuthDataSource;
import support.im.demo.data.source.AuthRepository;
import support.im.demo.data.source.local.AuthDataSourceImpl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by YuGang Yang on 04 22, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class Injection {

  public static AuthDataSource provideAuthRepository(@NonNull Context context) {
    checkNotNull(context);
    return AuthRepository.getInstance(AuthDataSourceImpl.getInstance());
  }
}
