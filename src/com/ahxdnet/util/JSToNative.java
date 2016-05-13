package com.ahxdnet.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class JSToNative {
	public void startCallPhone(Context context, String phoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
		context.startActivity(intent);
	}
	
	public void startCallQQ(Context context, String qq) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://wpa.qq.com/msgrd?v=3&uin="+qq+"&site=qq&menu=yes")));
	}
}
