package org.openmidaas.library.common.network;


import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.openmidaas.library.common.Constants;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ConnectionManager {
	
	private static ConnectionManager mInstance;
	
	private boolean mIsSSLDisabled;
	
	protected ConnectionManager() {
		mIsSSLDisabled = false;
	}
	
	public static synchronized ConnectionManager getInstance() {
		if (mInstance == null) {
			mInstance = new ConnectionManager();
		}
		return mInstance;
	}
	
	public boolean isSSLDisabled() {
		return mIsSSLDisabled;
	}
	
	public void setSSL(boolean enableSSL) {
		mIsSSLDisabled = enableSSL;
	}
	
	public void postRequest(String path, JSONObject data, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(null, Constants.AVP_SB_BASE_URL + path, getJSONDataAsEntity(data), "application/json", responseHandler);
	}
	
	public void getRequest(String path, Map<String, String> requestParams, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		if(requestParams != null) {
			for(Map.Entry<String, String> entry : requestParams.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
		    }
		}
		client.get(path, params, responseHandler);
	}
	
	private StringEntity getJSONDataAsEntity(JSONObject jsonData) {
		try {
			return (new StringEntity(jsonData.toString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
