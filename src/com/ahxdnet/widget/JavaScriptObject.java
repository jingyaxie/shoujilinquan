package com.ahxdnet.widget;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ahxdnet.linquanapp.wxapi.Constants;
import com.alipay.sdk.pay.demo.AliPay;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class JavaScriptObject {
	Context mContxt;
	private IWXAPI api;
	public static String prepayId = "";

	public JavaScriptObject(Context mContxt) {
		this.mContxt = mContxt;
		api = WXAPIFactory.createWXAPI(mContxt, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
	}

	@JavascriptInterface
	public void startCall(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			try {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				mContxt.startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(mContxt, "无法启动电话", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@JavascriptInterface
	public void startQQ(String qq) {
		if (!TextUtils.isEmpty(qq)) {
			try {
				mContxt.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq)));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(mContxt, "无法启动QQ,请确认是否安装QQ并已登录",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@JavascriptInterface
	public void lanchNativeAppByUri(String uri) {
		if (!TextUtils.isEmpty(uri)) {
			try {
				mContxt.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(uri)));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(mContxt, "启动本地应用出错", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@JavascriptInterface
	public void startWeixinPay(String obj) {
		// Toast.makeText(mContxt, "调用微信支付="+obj, Toast.LENGTH_SHORT).show();
		Log.e("jingya", "obj=" + obj);
		if (!TextUtils.isEmpty(obj)) {
			try {
				JSONObject json = new JSONObject(obj);
				if (null != json && !json.has("retcode")) {
					PayReq req = new PayReq();
					// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					prepayId = req.prepayId;
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					// Toast.makeText(mContxt, "正常调起支付",
					// Toast.LENGTH_SHORT).show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					api.sendReq(req);
				} else {
					Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
					Toast.makeText(mContxt, "返回错误" + json.getString("retmsg"),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(mContxt, "无法启动微信支付,请确认是否安装微信并已登录",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@JavascriptInterface
	public void startAliPay(String obj) {
		AliPay pay = new AliPay((Activity) mContxt);
		pay.pay(obj);
	}
}
