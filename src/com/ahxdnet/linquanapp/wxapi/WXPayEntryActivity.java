package com.ahxdnet.linquanapp.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ahxdnet.linquanapp.R;
import com.ahxdnet.util.listener.PayBackListenerManager;
import com.ahxdnet.widget.JavaScriptObject;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("提示");
			// builder.setMessage(getString(R.string.pay_result_callback_msg,
			// String.valueOf(resp.errCode)));
			// builder.show(); 
			// this.finish();
//			if (resp.errCode == 0) {
//				Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
//			} else {
//				Toast.makeText(getApplicationContext(), "支付失败-"+resp.errCode, Toast.LENGTH_SHORT).show();
//			}
			PayBackListenerManager.getInstance().noticeListener("weixin", JavaScriptObject.prepayId, resp.errCode+"");
//			PaymentBackListenerContainer.getIntance().notifaAllListener(JavaScriptObject.prepayId, resp.errCode);
			this.finish();
		}
	}
}