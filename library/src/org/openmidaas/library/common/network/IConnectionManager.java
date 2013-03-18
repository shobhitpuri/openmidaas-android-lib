package org.openmidaas.library.common.network;

import java.util.Map;

import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

public interface IConnectionManager {
	
	public void postRequest(String path, JSONObject data, AsyncHttpResponseHandler responseHandler);
	
	public void getRequest(String path, Map<String, String> requestParams, AsyncHttpResponseHandler responseHandler);
}
