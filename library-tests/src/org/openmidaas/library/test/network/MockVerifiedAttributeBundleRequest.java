/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.test.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.test.MIDaaSTest;
import org.openmidaas.library.test.Utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MockVerifiedAttributeBundleRequest extends MockTransport{
	
	//private Context mContext;
	
	
	public MockVerifiedAttributeBundleRequest(Context context) {
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
			if(!(data.getString("client_id").equals(MIDaaSTest.VALID_CLIENT_ID))) {
				responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
			}
			JSONObject attrRequest = data.getJSONObject("attrs");
			if(attrRequest == null) {
				responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
			}
			Iterator<?> keys =attrRequest.keys();
			while(keys.hasNext()) {
				String key = (String)keys.next();
				if(attrRequest.get(key) != null) {
					
				} else {
					responseHandler.onFailure(new MIDaaSException(MIDaaSError.SERVER_ERROR), "");
				}
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
