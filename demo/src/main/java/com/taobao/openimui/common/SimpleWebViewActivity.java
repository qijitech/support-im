package com.taobao.openimui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.openIMUIDemo.R;

import java.io.File;
import java.util.List;

/**
 * 简单的WebView
 */
public class SimpleWebViewActivity extends Activity {

    private static final String TAG = SimpleWebViewActivity.class
            .getSimpleName();

    public static final String URL = "url";
    public static final String TITLE = "title";
    public final static String[] DB_NAME_LIST = {"webview.db", "webviewCache.db", "webviewCookiesChromium.db"
            , "webviewCookiesChromiumPrivate.db"};

    private WebView mWebView;
    private String mUrl;
    private TextView mTitleView;
    private String mTitle;
    private ViewGroup parent;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //内嵌不能用，则使用第三方打开
        if (!isWebViewAvailable(this)) {
            YWLog.w(TAG, "web view disable");
            mUrl = getIntent().getStringExtra(URL);
            if (!TextUtils.isEmpty(mUrl)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                if (hasIntentHandler(this, intent)) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "未检测到浏览器", Toast.LENGTH_SHORT).show();
                }
            }
            finish();
            return;
        }
        setContentView(R.layout.simple_webview);
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        init();
    }

    private void init() {
        mUrl = getIntent().getStringExtra(URL);
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = getIntent().getDataString();
        }
        mTitle = getIntent().getStringExtra(TITLE);
        mTitleView = (TextView) findViewById(R.id.title_text);
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleView.setText(mTitle);
        }
        mWebView = (WebView) findViewById(R.id.simple_webview);
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            settings.setPluginState(WebSettings.PluginState.ON);
        }
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染优先级

        //进度条
        progressbar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setMax(100);
        progressbar.setProgressDrawable(this.getResources().getDrawable(
                R.drawable.demo_progress_bar_states));
        progressbar.setProgress(5); //先加载5%，以使用户觉得界面没有卡死
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 6);
        params.addRule(RelativeLayout.ABOVE, R.id.simple_webview);
        ViewParent parent = mWebView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).addView(progressbar, params);
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressbar.setVisibility(View.GONE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress >= 5){
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.loadUrl(mUrl);
    }

    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    /**
     * 能否处理intent
     *
     * @param context
     * @param intent
     * @return
     */
    public boolean hasIntentHandler(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 对webView的DB进行访问，如果有异常，则判断当前进程不能使用WebView
     *
     * @param context
     */
    private boolean isWebViewAvailable(final Context context) {

        boolean checkExisted = false;

        boolean bEnable = true;
        for (String dbName : DB_NAME_LIST) {
            // 构造文件，判断有无存在
            File dbFile = new File(context.getFilesDir().getParentFile().getPath()
                    + "/databases/" + "local_" + dbName);
            if (dbFile.exists()) {
                if (!isOpenSuccess(context, dbName)) {
                    bEnable = false;
                }
                checkExisted = true;
            }
        }

        if (!checkExisted) {
            final String dumyDb = "dumyDb";
            // 加强一下
            bEnable = isOpenSuccess(context, dumyDb);
            if (!bEnable) {
                try {
                    context.deleteDatabase(dumyDb);
                } catch (Throwable e) {
                }
            }
        }
        return bEnable;
    }

    private static boolean isOpenSuccess(Context context, String dbName) {
        if (TextUtils.isEmpty(dbName) || context == null) {
            return false;
        }
        boolean isOpenSuccess = true;
        SQLiteDatabase dataBase = null;
        try {
            dataBase = context.openOrCreateDatabase(dbName, 0, null);
            dataBase.getVersion();

        } catch (Throwable t) {
            isOpenSuccess = false;
        } finally {
            try {
                if (dataBase != null) {
                    dataBase.close();
                }
            } catch (Exception e) {
                YWLog.w(TAG, e);
            }
        }
        return isOpenSuccess;
    }
}
