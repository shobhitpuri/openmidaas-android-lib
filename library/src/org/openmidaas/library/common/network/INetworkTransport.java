package org.openmidaas.library.common.network;

import java.util.Map;

import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

public interface INetworkTransport {
	
	public void doPostRequest(boolean disableSSL, String url, JSONObject data, AsyncHttpResponseHandler responseHandler);
	
	public void doGetRequest(boolean disableSSL, String url, Map<String, String> requestParams, AsyncHttpResponseHandler responseHandler);

}
