package support.im.utilities;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.avos.avoscloud.okhttp.Call;
import com.avos.avoscloud.okhttp.OkHttpClient;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.Response;
import com.avos.avoscloud.okhttp.ResponseBody;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangh
 *
 * todo:此处调用的AVOS的默认OkHttp，可能存在问题，后续观察修复
 */
public class LocalCacheUtils {

  /**
   * 用于记录 DownLoadCallback，如果对于同一个 url 有多个请求，则下载完后应该执行所有回调
   * 此变量就是用于记录这些请求
   */
  private static Map<String, ArrayList<DownLoadCallback>> downloadCallBackMap;

  /**
   * 判断当前 url 是否正在下载，如果已经在下载，则没有必要再去做请求
   */
  private static Set<String> isDownloadingFile;

  private static OkHttpClient httpClient;

  static {
    downloadCallBackMap = new HashMap<String, ArrayList<DownLoadCallback>>();
    isDownloadingFile = new HashSet<String>();
  }

  private static void addDownloadCallback(String path, DownLoadCallback callback) {
    if (null != callback) {
      ArrayList<DownLoadCallback> callbacks;
      if (downloadCallBackMap.containsKey(path)) {
        callbacks = downloadCallBackMap.get(path);
      } else {
        callbacks = new ArrayList<DownLoadCallback>();
      }
      callbacks.add(callback);
    }
  }

  private static void executeDownloadCallBack(String path, Exception e) {
    if (downloadCallBackMap.containsKey(path)) {
      ArrayList<DownLoadCallback> callbacks = downloadCallBackMap.get(path);
      for (DownLoadCallback callback : callbacks) {
        callback.done(e);
      }
    }
  }

  private synchronized static OkHttpClient getDefaultHttpClient() {
    if (httpClient == null) {
      httpClient = new OkHttpClient();
    }
    return httpClient;
  }

  public static void downloadFileAsync(final String url, final String localPath) {
    downloadFileAsync(url, localPath, false);
  }

  public static void downloadFileAsync(final String url, final String localPath, boolean overlay) {
    downloadFile(url, localPath, overlay, null);
  }

  public static void downloadFile(final String url, final String localPath, boolean overlay,
      final DownLoadCallback callback) {
    if (TextUtils.isEmpty(url) || TextUtils.isEmpty(localPath)) {
      throw new IllegalArgumentException("url or localPath can not be null");
    } else if (!overlay && isFileExist(localPath)) {
      if (null != callback) {
        callback.done(null);
      }
    } else {
      addDownloadCallback(url, callback);
      if (!isDownloadingFile.contains(url)) {
        new AsyncTask<Void, Void, Exception>() {
          @Override protected Exception doInBackground(Void... params) {
            return downloadFile(url, localPath);
          }

          @Override protected void onPostExecute(Exception e) {
            executeDownloadCallBack(url, e);
            isDownloadingFile.remove(url);
          }
        }.execute();
      }
    }
  }

  private static Exception downloadFile(String url, String localPath) {

    File file = new File(localPath);
    Exception result = null;
    FileOutputStream outputStream = null;
    InputStream inputStream = null;
    try {
      Request request = new Request.Builder().url(url).build();
      Call call = getDefaultHttpClient().newCall(request);
      Response response = call.execute();
      ResponseBody responseBody = response.body();
      inputStream = responseBody.byteStream();
      outputStream = new FileOutputStream(file);
      byte[] buffer = new byte[4096];
      int len;
      while ((len = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }
    } catch (Exception e) {
      result = e;
      if (file.exists()) {
        file.delete();
      }
    } finally {
      closeQuietly(inputStream);
      closeQuietly(outputStream);
    }
    return result;
  }

  private static void closeQuietly(Closeable closeable) {
    try {
      closeable.close();
    } catch (Exception e) {
    }
  }

  private static boolean isFileExist(String localPath) {
    File file = new File(localPath);
    return file.exists();
  }

  public static class DownLoadCallback {
    public void done(Exception e) {
    }
  }
}