package com.ahxdnet.linquanapp.wxapi;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付完成监听
 * 
 * @author xiejingya
 *
 */
public class PaymentBackListenerContainer {
	static PaymentBackListenerContainer mPaymentBackListener;
	private Map<String, PaymentBackListener> listener = new HashMap<String, PaymentBackListener>();

	public static PaymentBackListenerContainer getIntance() {
		if (mPaymentBackListener == null) {
			mPaymentBackListener = new PaymentBackListenerContainer();
		}
		return mPaymentBackListener;
	}

	private PaymentBackListenerContainer() {
	}

	public void addListener(PaymentBackListener mPaymentBackListener) {
		if (mPaymentBackListener != null)
			listener.put(mPaymentBackListener.getClass().getSimpleName(), mPaymentBackListener);
	}

	public void removeListener(PaymentBackListener mPaymentBackListener) {
		if (mPaymentBackListener != null)
			listener.remove(mPaymentBackListener.getClass().getSimpleName());
	}

	public void clearListener() {
		listener.clear();
	}

	public void notifaAllListener(String prepayId, int state) {
		if (listener.size() > 0) {
			for (String key : listener.keySet()) {
				listener.get(key).onPayBack(prepayId, state);
			}
		}
	}
	public interface PaymentBackListener {
		public void onPayBack(String prepayId, int state);
	}
}
