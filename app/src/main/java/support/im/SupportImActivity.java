package support.im;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * Created by YuGang Yang on 04 01, 2016.
 * Copyright 20015-2016 honc.tech. All rights reserved.
 */
public class SupportImActivity extends AppCompatActivity {

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }
}
