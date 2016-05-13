package com.ahxdnet.linquanapp;

import com.ahxdnet.linquanapp.fragment.CustomFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class CommonFragmentActivity extends FragmentActivity {
	String TAG = CommonFragmentActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Intent intent = getIntent();
			String url = intent.getStringExtra("url");
//			url = "mqqwpa://im/chat?chat_type=wpa&uin=909972273&version=1&src_type=web&web_src=null";
			Log.e(TAG, "url="+url);
			CustomFragment kf =  CustomFragment.newInstance(url, 3);
			ft.add(android.R.id.content, kf);
			ft.commit();
//			kf.loadData(true);
		}
	}
}
