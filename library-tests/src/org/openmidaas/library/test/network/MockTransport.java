package org.openmidaas.library.test.network;

import java.util.Map;

import org.json.JSONObject;
import org.openmidaas.library.common.network.INetworkTransport;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.test.Utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MockTransport implements INetworkTransport{

	private Context mContext;
	
	private String mFilename;
	
	public MockTransport(Context context) {
		mContext = context;
	}
	
	public void setMockDataFile(String fname) {
		this.mFilename = fname;
	}
	
	@Override
	public void doPostRequest(boolean disableSSL, String url, JSONObject data,
			AsyncHttpResponseHandler responseHandler) {
		try {
			JSONObject mData = Utils.readFileAsJSON(mContext, mFilename);
			if(mData.getJSONObject("http").getString("statusCode").equalsIgnoreCase("200 OK")) {
				if(!(mData.getJSONObject("http").getString("body").isEmpty())) {
					responseHandler.onSuccess(mData.getJSONObject("http").getString("body").toString());
				} else {
					responseHandler.onSuccess("");
				}
			} else {
				responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
			}
		} catch(Exception e) {
			
		}
	}

	@Override
	public void doGetRequest(boolean disableSSL, String url,
			Map<String, String> requestParams,
			AsyncHttpResponseHandler responseHandler) {
		// TODO Auto-generated method stub
		
	}
	

}
