package com.ahxdnet.linquanapp.fragment;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ahxdnet.linquanapp.R;
import com.ahxdnet.linquanapp.wxapi.PaymentBackListenerContainer;
import com.ahxdnet.util.FileUtils;
import com.ahxdnet.util.NetUtil;
import com.ahxdnet.util.listener.PayBackListenerManager;
import com.ahxdnet.util.listener.PayBackListenerManager.PayBackListener;
import com.ahxdnet.widget.LoadFailView;
import com.ahxdnet.widget.LoadFailView.ReloadListener;
import com.ahxdnet.widget.ProgressWebView;

public class HomeFragment extends Fragment implements ReloadListener,
		PayBackListener {
	public ProgressWebView mWebView;
	View v;
	String url;
	LoadFailView mLoadFailView;
	String APP_CACAHE_DIRNAME = "/linquan/";
	int index;
	private SwipeRefreshLayout swipeLayout;
	private String current_url;
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mUploadMessage5up;
	private final static int FILECHOOSER_RESULTCODE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.webviewfragment, null);
		mLoadFailView = (LoadFailView) v.findViewById(R.id.no_data_layout);
		return v;
	}

	public static HomeFragment newInstance(String s, int index) {
		HomeFragment newFragment = new HomeFragment();
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
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
		webSettings.setUserAgentString(mWebView.getSettings()
				.getUserAgentString() + "_" + "lqshandroid");
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new XHSWebChromeClient());
		Bundle args = getArguments();
		url = args != null ? args.getString("url") : "";
		index = args != null ? args.getInt("index") : 0;
		initWebView();
		mWebView.loadUrl(url);
		mLoadFailView.setOnReloadListener(this);
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		swipeLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						// 重新刷新页面
						mWebView.loadUrl(current_url);
					}
				});
		swipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		PayBackListenerManager.getInstance().addListener(this);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		PayBackListenerManager.getInstance().removeListener(this);
	}

	public void loadData(boolean isLoad) {
		// if (mWebView != null && isLoad)
		// mWebView.loadUrl(url);
		// swipeLayout.setRefreshing(true);
		mWebView.loadUrl(url);
	}

	private void initWebView() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// Log.e("jingya", "url=" + url);
				// Toast.makeText(getActivity(), "url=" + url,
				// Toast.LENGTH_SHORT).show();
				if (url.startsWith("weixin://")) {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(url)));
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "无法启动微信,请确认是否安装微信并已登录",
								Toast.LENGTH_SHORT).show();
					}
					return true;
				}
				if (url.startsWith("mqqwpa://")) {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(url)));
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "无法启动QQ,请确认是否安装QQ并已登录",
								Toast.LENGTH_SHORT).show();
					}
					return true;
				}
				if (url.trim().startsWith("tel:")) {
					try {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse(url));
						startActivity(intent);
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "无法启动电话",
								Toast.LENGTH_SHORT).show();
					}
					return true;
				}
				// else {
				// HitTestResult hit = view.getHitTestResult();
				// if (hit != null) {
				// Intent intent = new Intent();
				// intent.setClass(getActivity(), CommonFragmentActivity.class);
				// intent.putExtra("url", url);
				// intent.putExtra("index", index);
				// startActivity(intent);
				// return true;
				// } else {
				// return false;
				// }
				// }
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mWebView.getSettings().setBlockNetworkImage(true);
				if (!NetUtil.isNetworkAvailable(getActivity())) {
					mLoadFailView.setVisibility(View.VISIBLE);
				}
				current_url = url;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (!NetUtil.isNetworkAvailable(getActivity())
						|| url.contains("data:text/html")) {
					mLoadFailView.setVisibility(View.VISIBLE);
				} else {
					mLoadFailView.setVisibility(View.GONE);
				}
				mWebView.getSettings().setBlockNetworkImage(false);
				// 隐藏进度条
				swipeLayout.setRefreshing(false);
				current_url = url;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				mLoadFailView.setVisibility(View.VISIBLE);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				if (null == mUploadMessage5up)
					return;
			} else {
				if (null == mUploadMessage)
					return;
			}
			Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
					: intent.getData();
			if (result == null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					mUploadMessage5up.onReceiveValue(null);
					mUploadMessage5up = null;
				} else {
					mUploadMessage.onReceiveValue(null);
					mUploadMessage = null;
				}
				return;
			}
			Log.i("UPFILE", "onActivityResult" + result.toString());
			String path = FileUtils.getPath(getActivity(), result);
			if (TextUtils.isEmpty(path)) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					mUploadMessage5up.onReceiveValue(null);
					mUploadMessage5up = null;
				} else {
					mUploadMessage.onReceiveValue(null);
					mUploadMessage = null;
				}
				return;
			}
			Uri uri = Uri.fromFile(new File(path));
			Log.i("UPFILE",
					"onActivityResult after parser uri:" + uri.toString());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				if (mUploadMessage5up != null)
					mUploadMessage5up.onReceiveValue(new Uri[] { uri });
			} else {
				mUploadMessage.onReceiveValue(uri);
			}
			mUploadMessage = null;
			mUploadMessage5up = null;
		}
	}

	public class XHSWebChromeClient extends WebChromeClient {
		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			Log.i("UPFILE", "in openFile Uri Callback");
			if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(null);
			}
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);
		}

		// For Android 3.0+
		public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			Log.i("UPFILE", "in openFile Uri Callback has accept Type"
					+ acceptType);
			if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(null);
			}
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			String type = TextUtils.isEmpty(acceptType) ? "image/*"
					: acceptType;
			i.setType(type);
			startActivityForResult(Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);
		}

		// For Android 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			Log.i("UPFILE", "in openFile Uri Callback has accept Type"
					+ acceptType + "has capture" + capture);
			if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(null);
			}
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			String type = TextUtils.isEmpty(acceptType) ? "image/*"
					: acceptType;
			i.setType(type);
			startActivityForResult(Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);
		}

		// Android 5.0+
		@Override
		@SuppressLint("NewApi")
		public boolean onShowFileChooser(WebView webView,
				ValueCallback<Uri[]> filePathCallback,
				FileChooserParams fileChooserParams) {
			if (mUploadMessage5up != null) {
				mUploadMessage5up.onReceiveValue(null);
			}
			Log.i("UPFILE",
					"file chooser params：" + fileChooserParams.toString());
			mUploadMessage5up = filePathCallback;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			// if (fileChooserParams != null &&
			// fileChooserParams.getAcceptTypes() != null
			// && fileChooserParams.getAcceptTypes().length > 0) {
			// i.setType(fileChooserParams.getAcceptTypes()[0]);
			// } else {
			//
			// }
			startActivityForResult(Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);
			return true;
		}

		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mWebView.progressbar.setVisibility(View.GONE);
			} else {
				if (mWebView.progressbar.getVisibility() == View.GONE)
					mWebView.progressbar.setVisibility(View.VISIBLE);
				mWebView.progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	payBacklListener mpayBacklListener;

	public interface payBacklListener {
		public void payBack(int code);
	}

	@Override
	public void onPayBack(String type, String prepayId, int state) {
		// TODO Auto-generated method stub
		String js = null;
		if ("weixin".endsWith(type)) {
			js = "javascript:payback('" + prepayId + "|" + state + "')";
		}
		if ("alipay".endsWith(type)) {
			js = "javascript:payback('" + prepayId + "|" + state + "')";
		}
		if (!TextUtils.isEmpty(js))
			mWebView.loadUrl(js);
	}
}
