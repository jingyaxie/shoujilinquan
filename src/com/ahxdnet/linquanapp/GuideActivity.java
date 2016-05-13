package com.ahxdnet.linquanapp;

import java.util.ArrayList;

import com.ahxdnet.linquanapp.R;
import com.ahxdnet.util.MyBitmapUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity implements OnPageChangeListener {
	private ViewPager viewPager;
	private ArrayList<View> views;
	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ImageView image_1 = new ImageView(getApplicationContext());
		ImageView image_2 = new ImageView(getApplicationContext());
		ImageView image_3 =new ImageView(getApplicationContext());
		ImageView image_4 =new ImageView(getApplicationContext());
		image_1.setLayoutParams(param);
		image_2.setLayoutParams(param);
		image_3.setLayoutParams(param);
		image_4.setLayoutParams(param);
		image_1.setScaleType(ImageView.ScaleType.FIT_XY );
		image_2.setScaleType(ImageView.ScaleType.FIT_XY );
		image_3.setScaleType(ImageView.ScaleType.FIT_XY );
		image_4.setScaleType(ImageView.ScaleType.FIT_XY );
		MyBitmapUtil.LoaderBitmapFromResourceThread(getResources(),
				R.drawable.android_guide_step_1, image_1, 720, 1280);
		MyBitmapUtil.LoaderBitmapFromResourceThread(getResources(),
				R.drawable.android_guide_step_2, image_2, 720, 1280);
		MyBitmapUtil.LoaderBitmapFromResourceThread(getResources(),
				R.drawable.android_guide_step_3, image_3, 720, 1280);
		MyBitmapUtil.LoaderBitmapFromResourceThread(getResources(),
				R.drawable.android_guide_step_4, image_4, 720, 1280);
		views = new ArrayList<View>();
		views.add(image_1);
		views.add(image_2);
		views.add(image_3);
		views.add(image_4);
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(new ViewPagerAdapter(views));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 3) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					WelcomeActivity.sp.edit().putInt("VERSION", WelcomeActivity.VERSION).commit();
					startActivity(new Intent(GuideActivity.this, HomeActivity.class));
					finish();
				}
			}, 500);
		}
	}
}
