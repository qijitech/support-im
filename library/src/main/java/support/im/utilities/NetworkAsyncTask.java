package support.im.utilities;

import android.content.Context;
import android.os.AsyncTask;

public abstract class NetworkAsyncTask extends AsyncTask<Void, Void, Void> {

  protected Context mContext;
  boolean mShowHud = true;
  Exception mException;

  protected NetworkAsyncTask(Context context) {
    mContext = context;
  }

  protected NetworkAsyncTask(Context context, boolean showHud) {
    mContext = context;
    mShowHud = showHud;
  }

  public NetworkAsyncTask shouldShowHud(boolean showHud) {
    mShowHud = showHud;
    return this;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    if (mShowHud) {
      HudUtils.showHud(mContext);
    }
  }

  @Override
  protected Void doInBackground(Void... params) {
    try {
      doInBackground();
    } catch (Exception e) {
      SupportLog.logException(e);
      mException = e;
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    super.onPostExecute(result);
    if (mShowHud) {
      HudUtils.dismissHud();
    }
    onPost(mException);
  }

  protected abstract void doInBackground() throws Exception;

  protected abstract void onPost(Exception e);
}
