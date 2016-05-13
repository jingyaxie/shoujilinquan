package com.ahxdnet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxdnet.linquanapp.R;

public class LoadFailView extends RelativeLayout {
	TextView reload;

	public LoadFailView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public LoadFailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		View view = View.inflate(getContext(), R.layout.no_data_layout, null);
		this.addView(view);
		reload = (TextView) findViewById(R.id.reload_text);
		reload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mReloadListener != null) {
					mReloadListener.onReloadListener();
				}
			}
		});
	}

	ReloadListener mReloadListener;

	public void setOnReloadListener(ReloadListener mReloadListener) {
		this.mReloadListener = mReloadListener;
	}

	public interface ReloadListener {
		public void onReloadListener();
	}
}
