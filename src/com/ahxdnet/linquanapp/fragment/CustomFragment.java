package com.ahxdnet.linquanapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ahxdnet.linquanapp.R;
import com.ahxdnet.widget.ProgressWebView;
import com.ahxdnet.widget.LoadFailView.ReloadListener;

public class CustomFragment extends Fragment implements ReloadListener {
	public ProgressWebView mWebView;
	View v;
	String url;
	int index;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.custom_fragment, null);
		return v;
	}

	public static CustomFragment newInstance(String s,int index) {
		CustomFragment newFragment = new CustomFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", s);
		bundle.putInt("index", index);
		newFragment.setArguments(bundle);
		// bundle还可以在每个标签里传送数据
		return newFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mWebView = (ProgressWebView) v.findViewById(R.id.webview);
//		mWebView.getSettings().setUserAgentString("lqshapp");
		Bundle args = getArguments();
		url = args != null ? args.getString("url") : "";
		index = args != null ? args.getInt("index") : 0;
		initWebView();
		mWebView.loadUrl(url);
	}

	public void loadData(boolean isLoad) {
		if (mWebView != null&&isLoad)
			mWebView.loadUrl(url);
	}

	private void initWebView() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.e("ContentPagerFragment", "url=" + url);
//				Toast.makeText(getActivity(), "url="+url, Toast.LENGTH_SHORT).show();
				if (url.contains("mqqwpa://im/chat?chat_type=wpa&uin")) {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "无法启动QQ,请确认是否安装QQ并已登录", Toast.LENGTH_SHORT).show();
					}
				}
				return false;
		
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description,
					String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}

	@Override
	public void onReloadListener() {
		// TODO Auto-generated method stub
		if (mWebView != null)
			mWebView.loadUrl(url);
	}
}
