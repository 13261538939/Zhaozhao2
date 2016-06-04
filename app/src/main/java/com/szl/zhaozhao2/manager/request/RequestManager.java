package com.szl.zhaozhao2.manager.request;

import org.json.JSONObject;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;

public class RequestManager {

	private final String TAG = getClass().getSimpleName();

	private static RequestManager mBaseRequestManager;
	private RequestQueue mRequestQueue;
	private int versionCode;

	public static RequestManager getInstance() {
		if (mBaseRequestManager == null) {
			mBaseRequestManager = new RequestManager();
		}
		return mBaseRequestManager;
	}

	private RequestManager() {
		init();
	}

	private void init() {
		versionCode = CommonUtil.getVersionCode();
		mRequestQueue = Volley.newRequestQueue(DApplication.getApplication());
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			init();
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> request) {
		addToRequestQueue(request, TAG);
	}

	public <T> void addToRequestQueue(Request<T> request, String tag) {
		request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(request);
	}

	public void cancelRequests(String tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * @param listener
	 *            成功返回数据的监听
	 * @param errorListener
	 *            错误返回的监听
	 */
	public void getData(int requestType, JSONObject parameters,
			Listener<JSONObject> listener, ErrorListener errorListener,
			String methodName, String tag) {
		LogPrinter.d(CommonUtil.isEmpty(tag) ? TAG : tag, "--> getData");

		JsonObjectRequest request = new JsonObjectRequest(requestType,
				Contants.BASE_URL+ methodName, parameters, listener,
				errorListener);
		// 设置网络超时
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		RequestManager.getInstance().addToRequestQueue(request, tag);
	}
}
