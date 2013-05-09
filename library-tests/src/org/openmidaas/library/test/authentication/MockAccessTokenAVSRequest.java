package org.openmidaas.library.test.authentication;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.test.Utils;
import org.openmidaas.library.test.network.MockTransport;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MockAccessTokenAVSRequest  extends MockTransport{
	public MockAccessTokenAVSRequest(Context context) {
		super(context);
		
	}
	

	/**
	 * First checks to see if the format of the request is as follows:
	 * 
	 */
	@Override
	public void doPostRequest(boolean disableSSL, String url,
			HashMap<String, String> headers, JSONObject data,
			AsyncHttpResponseHandler responseHandler) {
		try {
			if(!data.getString("subjectToken").equals("header.payload.signature")) {
				responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
			}
			if((data.getString("deviceToken") == null) ||  (data.getString("deviceToken").isEmpty())) {
				responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
			}
			JSONObject mData = Utils.readFileAsJSON(mContext, mFilename);
			if(mData.getJSONObject("http").getString("statusCode").equalsIgnoreCase("200 OK") ||
					mData.getJSONObject("http").getString("statusCode").equalsIgnoreCase("201 Created")) {
				if(!(mData.getJSONObject("http").getString("body").isEmpty())) {
					responseHandler.onSuccess(mData.getJSONObject("http").getString("body").toString());
				} else {
					responseHandler.onSuccess("");
				}
			} else {
				responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
			}
		} catch(Exception e) {
			responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
		}
		
	}

	@Override
	public void doGetRequest(boolean disableSSL, String url,
			Map<String, String> requestParams,
			AsyncHttpResponseHandler responseHandler) {
		
	}
}
