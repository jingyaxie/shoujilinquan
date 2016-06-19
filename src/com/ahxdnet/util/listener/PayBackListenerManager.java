package com.ahxdnet.util.listener;

import java.util.HashMap;
import java.util.Map;

public class PayBackListenerManager {

	private static PayBackListenerManager mPayBackListenerManager;

	public static PayBackListenerManager getInstance() {
		if (mPayBackListenerManager == null) {
			mPayBackListenerManager = new PayBackListenerManager();
		}
		return mPayBackListenerManager;
	}

	private Map<String, PayBackListener> listenersMap = new HashMap<String, PayBackListener>();

	public void addListener(PayBackListener mPayBackListener) {
		String key = mPayBackListener.getClass().getSimpleName();
		if (listenersMap.containsKey(key))
			return;
		listenersMap.put(key, mPayBackListener);
	}

	public void removeListener(PayBackListener mPayBackListener) {
		String key = mPayBackListener.getClass().getSimpleName();
		if (listenersMap.containsKey(key)) {
			listenersMap.remove(key);
		}

	}

	public interface PayBackListener {
		public void onPayBack(String type, String prepayId, String state);
	}

	public void noticeListener(String type, String prepayId, String resultStatus) {
		if (listenersMap.size() <= 0)
			return;

		for (String key : listenersMap.keySet()) {
			listenersMap.get(key).onPayBack(type, prepayId, resultStatus);
		}

	}

}
