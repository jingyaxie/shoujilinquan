package com.alipay.sdk.pay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ahxdnet.util.listener.PayBackListenerManager;
import com.alipay.sdk.app.PayTask;

public class AliPay {
	// 商户PID
	public static final String PARTNER = "2088121179748958";
	// 商户收款账号
	public static final String SELLER = "3144603626@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJaE9vAO7iXgrvRYmlIAygMCbUSoczR/V4SBKJUcIcYnM52/tEhBbPbOFPYmyRyNaukxVnDKLWuhflcIPQIpULfqWsz/f3TZByxP5axKFHvQVxaPCrwkK2zjRyjbnVJ1w1MsMo/glJNlQu6kDNkYVkaH3i0qu80ZfvGNZ94Yo9/VAgMBAAECgYA2b8Q3Mc8jV80uhdTaD3r/96K5EAsKU3u9alDZDKGSXp8mUCzNbQwRfprKI2DcuLCPwc43YIdow7FDnLEj1Plh3JvWjlfHZv63ES60/+n/aasgwHC7gFYppGZqorKYzBb32iQI8N+q7WzCATaaPeRCefkSIL3w++FyBUnehbdxiQJBAMX/nb1w5r7/d2PkbZoT0bSjvyzDgAhjIutXXkDSYwNaMDd9Ggup98ZDRJa/LTGO4nlOSTnLXb/7tYF6xMXlVdcCQQDCnMajx1Znjor+kCMeAi8RwgwmfaDNrL9HzAeYbMIN/NO1nKZnXk7WXesCFex8GuB6tE889E36KKOCla3yRaozAkBzD7HWNjcRxZnXBhcfU+co54Ogetf7h8jyJKqUVu7NMusUQ/IP1y0zHUJNrDmSNCuuPO1W1bTcbWU0ImkZa8WvAkBVUFkcTzSnrSttsU5Tx21PAZxF+2jVAVfRZ7YksfjYHO/Ke+PPCN7SxtVRUr8bIMUrxYO/GvJcKhiUFH3u6W9VAkEAvUbecSmIWJh4LmAWCVknmvj4nH/KaHP4H0W7lzJuQk/ohonWXnMocnDF3m7MD/NLshAl+Y73RPC9KWt3qVMJuA==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWhPbwDu4l4K70WJpSAMoDAm1EqHM0f1eEgSiVHCHGJzOdv7RIQWz2zhT2JskcjWrpMVZwyi1roX5XCD0CKVC36lrM/3902QcsT+WsShR70FcWjwq8JCts40co251SdcNTLDKP4JSTZULupAzZGFZGh94tKrvNGX7xjWfeGKPf1QIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	private Activity context;

	public AliPay(Activity context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				resultInfo = "index.jsp?"+resultInfo.replace("\"", "");
//				Log.d("jingya", "resultInfo=" + resultInfo);
				String resultStatus = payResult.getResultStatus();
				String out_trade_no = "";
				try {
					Map<String,String> map = URLRequest(resultInfo);
					out_trade_no = map.get("out_trade_no");
//					Log.d("jingya", "map.size=" + map.size());
//					Log.d("jingya", "out_trade_no=" + out_trade_no);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					PayBackListenerManager.getInstance().noticeListener(
							"alipay", out_trade_no, 0 + "");
				} else {
					PayBackListenerManager.getInstance().noticeListener(
							"alipay", out_trade_no, resultStatus);
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					// if (TextUtils.equals(resultStatus, "8000")) {
					// Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT)
					// .show();
					//
					// } else {
					// // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					// Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT)
					// .show();
					//
					// }
				}
				break;
			}
			default:
				break;
			}
		};
	};

	// {\"name\":\"21寸液晶显示器\", \"orderNum\":\"num123456\", \"price\":820,
	// \"description\":\"测试用\", \"notifyURL\":\"给个url\"}
	public void pay(String json) {
		Log.e("jingya", "1");
		if (TextUtils.isEmpty(json))
			return;
		Log.e("jingya", "2");
		JSONObject obj;
		String orderInfo;
		String sign;
		try {
			Log.e("jingya", "3");
			obj = new JSONObject(json);
			String subject = obj.optString("name");
			String body = obj.optString("description");
			String price = obj.optString("price");
			String orderNum = obj.optString("orderNum");
			String notifyURL = obj.optString("notifyURL");
			orderInfo = getOrderInfo(subject, body, price, orderNum, notifyURL);
			/**
			 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
			 */
			sign = sign(orderInfo);
			Log.e("jingya", "4 sign=" + sign);
			try {
				/**
				 * 仅需对sign 做URL编码
				 */
				sign = URLEncoder.encode(sign, "UTF-8");
//				Log.e("jingya", "5 sign=" + sign);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Log.e("jingya", "5");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("jingya", "JSONException=" + e.getLocalizedMessage());
			return;
		}
		Log.e("jingya", "6");
		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
//		Log.e("jingya", "payInfo=" + payInfo);
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
		Log.e("jingya", "payThread.start()");

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(context);
		String version = payTask.getVersion();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(String url) {
		if (TextUtils.isEmpty(url))
			return;
		Intent intent = new Intent(context, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		// url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		context.startActivity(intent);
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price,
			String orderNum, String notifyURL) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNum + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notifyURL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
//		Log.e("jingya", "content=" + content);
//		Log.e("jingya", "RSA_PRIVATE=" + RSA_PRIVATE);
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * 
	 * @param URL
	 *            url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> URLRequest(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		String strUrlParam = TruncateUrlPage(URL);
		if (strUrlParam == null) {
			return mapRequest;
		}
		// 每个键值为一组 www.2cto.com
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * 
	 * @param strURL
	 *            url地址
	 * @return url请求参数部分
	 */
	private static String TruncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;

		strURL = strURL.trim().toLowerCase();

		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}

		return strAllParam;
	}

}
