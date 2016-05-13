package com.ahxdnet.linquanapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.ahxdnet.linquanapp.R;
import com.ahxdnet.util.MyBitmapUtil;

public class WelcomeActivity extends Activity implements Runnable {
	public static final int VERSION = 1;
	public static SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("Y_Setting", Context.MODE_PRIVATE);
		if (sp.getInt("VERSION", 0) == VERSION) {
			LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			ImageView image_1 = new ImageView(getApplicationContext());
			image_1.setLayoutParams(param);
			image_1.setScaleType(ImageView.ScaleType.FIT_XY);
			setContentView(image_1);
			MyBitmapUtil.LoaderBitmapFromResourceThread(getResources(), R.drawable.lanch_bg,
					image_1, 720, 1280);
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			if (sp.getInt("VERSION", 0) != VERSION) {
				startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
			} else {
				Thread.sleep(2000);
				startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
			}
			finish();
		} catch (Exception e) {
		}
	}
}
