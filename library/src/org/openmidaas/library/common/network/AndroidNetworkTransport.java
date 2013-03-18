package org.openmidaas.library.common.network;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AndroidNetworkTransport implements INetworkTransport{

	private final String TAG = "AndroidNetworkTransport";
	
	@Override
	public void doPostRequest(boolean withSSL, String url, JSONObject data,
			AsyncHttpResponseHandler responseHandler) {
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(null, url, new StringEntity(data.toString()), "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			MIDaaS.logError(TAG, e.getMessage());
			responseHandler.onFailure(e, e.getMessage());
		}
	}

	@Override
	public void doGetRequest(boolean withSSL, String url, Map<String, String> requestParams,
			AsyncHttpResponseHandler responseHandler) {
		// TODO: Implement for future GET requests if any. 
	}

}
