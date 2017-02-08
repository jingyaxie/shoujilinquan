package com.ahxdnet.linquanapp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ahxdnet.linquanapp.fragment.CustomFragment;
import com.ahxdnet.linquanapp.fragment.HomeFragment;
import com.ahxdnet.util.DownLoadManager;
import com.ahxdnet.util.HttpUtils;
import com.ahxdnet.util.URLConfig;
import com.ahxdnet.util.UpdateVersion;

public class HomeActivity extends FragmentActivity implements OnClickListener {
	public String TAG = HomeActivity.class.getSimpleName();
	RadioGroup mRadioGroup;
	FragmentTransaction ft;
	private FragmentManager mFragmentManager;
	HomeFragment sy;
	HomeFragment gj;
	CustomFragment kf;
	HomeFragment gwc;
	HomeFragment grzx;
	private int current_index;
	public final static int SCANNIN_GREQUEST_CODE = 1;
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		int index = intent.getIntExtra("index", -1);
		switch (index) {
		case 1:
			mRadioGroup.check(R.id.home_bottom_tab_index_1);
			current_index = R.id.home_bottom_tab_index_1;
			if (ft == null) {
				ft.add(R.id.home_fragment_body_1, sy);
			} else {
				ft.show(sy);
				sy.loadData(true);
			}
			break;
		case 2:
			mRadioGroup.check(R.id.home_bottom_tab_index_2);
			current_index = R.id.home_bottom_tab_index_2;
			if (gj == null) {
				gj = HomeFragment.newInstance(
						"http://www.99tx.com/wap/special.html?special_id=4", 2);
				ft.add(R.id.home_fragment_body_2, gj);
			} else {
				ft.show(gj);
				gj.loadData(true);
			}
			break;
		case 3:
			mRadioGroup.check(R.id.home_bottom_tab_index_3);
			current_index = R.id.home_bottom_tab_index_3;
			if (kf == null) {
				// kf = CustomFragment.newInstance(
				// "http://wpa.qq.com/msgrd?v=3&uin=909972273&site=qq&menu=yes",
				// 3);
				kf = CustomFragment.newInstance(
						"http://www.99tx.com/wap/kf.html", 3);
				ft.add(R.id.home_fragment_body_3, kf);
			} else {
				ft.show(kf);
				kf.loadData(true);
			}
			break;
		case 4:
			mRadioGroup.check(R.id.home_bottom_tab_index_4);
			current_index = R.id.home_bottom_tab_index_4;
			if (gwc == null) {
				gwc = HomeFragment.newInstance(
						"http://www.99tx.com/wap/tmpl/cart_list.html", 4);
				ft.add(R.id.home_fragment_body_4, gwc);
			} else {
				ft.show(gwc);
				gwc.loadData(true);
			}
			break;
		case 5:
			mRadioGroup.check(R.id.home_bottom_tab_index_5);
			current_index = R.id.home_bottom_tab_index_5;
			if (grzx == null) {
				grzx = HomeFragment
						.newInstance(
								"http://www.99tx.com/wap/tmpl/member/member.html?act=member",
								5);
				ft.add(R.id.home_fragment_body_5, grzx);
			} else {
				ft.show(grzx);
				grzx.loadData(true);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFragmentManager = getSupportFragmentManager();
		if (mFragmentManager.findFragmentByTag(TAG) == null) {
			ft = mFragmentManager.beginTransaction();
			sy = HomeFragment.newInstance("http://www.99tx.com/wap/", 1);
			ft.add(R.id.home_fragment_body_1, sy);
			ft.commit();
			current_index = R.id.home_bottom_tab_index_1;
		}
		mRadioGroup = (RadioGroup) findViewById(R.id.home_bottom_tab_body);
		mRadioGroup.check(R.id.home_bottom_tab_index_1);
		findViewById(R.id.home_bottom_tab_index_1).setOnClickListener(this);
		findViewById(R.id.home_bottom_tab_index_2).setOnClickListener(this);
		findViewById(R.id.home_bottom_tab_index_3).setOnClickListener(this);
		findViewById(R.id.home_bottom_tab_index_4).setOnClickListener(this);
		findViewById(R.id.home_bottom_tab_index_5).setOnClickListener(this);

		Log.e("jingya", getSign(this));
		new UpdateVersion(this).checkVersion();
	}

	private void hideFragment(FragmentTransaction transaction) {
		if (grzx != null) {
			transaction.hide(grzx);
		}
		if (kf != null) {
			transaction.hide(kf);
		}
		if (gj != null) {
			transaction.hide(gj);
		}
		if (sy != null) {
			transaction.hide(sy);
		}
		if (gwc != null) {
			transaction.hide(gwc);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				switch (current_index) {
				case R.id.home_bottom_tab_index_1:
					if (sy.mWebView.canGoBack()) {
						sy.mWebView.goBack();
						return true;
					} else {
						onBackPressed();
					}
					break;
				case R.id.home_bottom_tab_index_2:
					if (gj.mWebView.canGoBack()) {
						gj.mWebView.goBack();
						return true;
					} else {
						onBackPressed();
					}
					break;
				case R.id.home_bottom_tab_index_3:
					// if (kf.mWebView.canGoBack()) {
					// kf.mWebView.goBack();
					// return true;
					// } else {
					onBackPressed();
					// }
					break;
				case R.id.home_bottom_tab_index_4:
					if (gwc.mWebView.canGoBack()) {
						gwc.mWebView.goBack();
						return true;
					} else {
						onBackPressed();
					}
					break;
				case R.id.home_bottom_tab_index_5:
					if (grzx.mWebView.canGoBack()) {
						grzx.mWebView.goBack();
						return true;
					} else {
						onBackPressed();
					}
					break;
				default:
					onBackPressed();
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("确认退出吗？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						HomeActivity.this.finish();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		ft = getSupportFragmentManager().beginTransaction();
		hideFragment(ft);
		switch (id) {
		case R.id.home_bottom_tab_index_1:
			current_index = R.id.home_bottom_tab_index_1;
			if (ft == null) {
				ft.add(R.id.home_fragment_body_1, sy);
			} else {
				ft.show(sy);
				sy.loadData(true);
			}
			break;
		case R.id.home_bottom_tab_index_2:
			current_index = R.id.home_bottom_tab_index_2;
			if (gj == null) {
				gj = HomeFragment.newInstance(
						"http://www.99tx.com/wap/special.html?special_id=4", 2);
				ft.add(R.id.home_fragment_body_2, gj);
			} else {
				ft.show(gj);
				gj.loadData(true);
			}
			break;
		case R.id.home_bottom_tab_index_3:
			current_index = R.id.home_bottom_tab_index_3;
			if (kf == null) {
				// kf = CustomFragment.newInstance(
				// "http://wpa.qq.com/msgrd?v=3&uin=909972273&site=qq&menu=yes",
				// 3);
				kf = CustomFragment.newInstance(
						"http://www.99tx.com/wap/kf.html", 3);
				ft.add(R.id.home_fragment_body_3, kf);
			} else {
				ft.show(kf);
				kf.loadData(true);
			}
			break;
		case R.id.home_bottom_tab_index_4:
			current_index = R.id.home_bottom_tab_index_4;
			if (gwc == null) {
				gwc = HomeFragment.newInstance(
						"http://www.99tx.com/wap/tmpl/cart_list.html", 4);
				ft.add(R.id.home_fragment_body_4, gwc);
			} else {
				ft.show(gwc);
				gwc.loadData(true);
			}
			break;
		case R.id.home_bottom_tab_index_5:
			current_index = R.id.home_bottom_tab_index_5;
			if (grzx == null) {
				grzx = HomeFragment
						.newInstance(
								"http://www.99tx.com/wap/tmpl/member/member.html?act=member",
								5);
				ft.add(R.id.home_fragment_body_5, grzx);
			} else {
				ft.show(grzx);
				grzx.loadData(true);
			}
			break;
		default:
			break;
		}
		ft.commit();
	}

	private String getSign(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sigin = info.signatures[0].toCharsString();

		return sigin;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				final String result = bundle.getString("result");
				String title = "";
				if(result.contains("http://")){
					title = "确认访问该网址";
					Intent intent = new Intent(HomeActivity.this,CommonFragmentActivity.class);      
					intent.putExtra("url", result);
			        startActivity(intent);
			        return;
				}else{
					title = "扫描结果";
				}
				
				final Builder mBuilder = new AlertDialog.Builder(this);
				final AlertDialog mAlertDialog =mBuilder.create();
				
				mBuilder.setTitle(title)
				.setMessage(result)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						if(result.contains("://")){
							Intent intent = new Intent(HomeActivity.this,CommonFragmentActivity.class);      
							intent.putExtra("url", result);
					        startActivity(intent);
						}else{
							mAlertDialog.dismiss();
						}
						
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
			
			}
			break;
		}
    }
	
	

}
