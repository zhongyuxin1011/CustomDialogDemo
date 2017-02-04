package com.zyx1011.customdialogdemo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_show)
    Button mBtnShow;
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadUrl();
    }

    private void loadUrl() {
        mWebView.addJavascriptInterface(this, "zyx1011");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                // super.onPageFinished(view, url);
            }
        });
    }

    @OnClick(R.id.btn_show)
    @JavascriptInterface
    public void onClick() {
        final Dialog dialog = new Dialog(this);
        // 去掉标题栏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = dialog.getWindow();
        // 设置背景透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams attributes = window.getAttributes();
        // 底部
        attributes.gravity = Gravity.BOTTOM;
        // 隐藏显示动画效果
        attributes.windowAnimations = R.style.MyDialogAnimation;
        // 背影效果，0-1，透明度
        attributes.dimAmount = 0.5f;

        View view = View.inflate(getApplicationContext(), R.layout.dialog_setting, null);
        ((Button) view.findViewById(R.id.btn_01)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mWebView.reload();
                        dialog.dismiss();
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.btn_04)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏对话框
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);

        // 点击对话框以外关闭对话框
        dialog.setCanceledOnTouchOutside(true);
        // 显示对话框
        dialog.show();
    }
}
