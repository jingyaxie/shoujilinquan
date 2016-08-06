package com.ahxdnet.widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ProgressWebView extends WebView {
	public ProgressBar progressbar;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 12, 0, 0));
		addView(progressbar);
		// setWebChromeClient(new WebChromeClient());
		setPageCacheCapacity(getSettings());
		addJavascriptInterface(new JavaScriptObject(getContext()), "linquanapp");
		
	}

	// public class WebChromeClient extends android.webkit.WebChromeClient {
	// @Override
	// public void onProgressChanged(WebView view, int newProgress) {
	// if (newProgress == 100) {
	// progressbar.setVisibility(GONE);
	// } else {
	// if (progressbar.getVisibility() == GONE)
	// progressbar.setVisibility(VISIBLE);
	// progressbar.setProgress(newProgress);
	// }
	// super.onProgressChanged(view, newProgress);
	// }
	//
	// }
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * 这个函数是用来处理 当进行goBack的时候 使用前一个页面的缓存 避免每次都从新载入
	 * 
	 * @param webSettings
	 *            webView的settings
	 */
	protected void setPageCacheCapacity(WebSettings webSettings) {
		try {
			Class<?> c = Class.forName("android.webkit.WebSettingsClassic");
			Method tt = c.getMethod("setPageCacheCapacity", new Class[] { int.class });
			tt.invoke(webSettings, 100);
		} catch (ClassNotFoundException e) {
			System.out.println("No such class: " + e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}