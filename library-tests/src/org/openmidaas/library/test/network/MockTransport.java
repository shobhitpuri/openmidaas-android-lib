/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.test.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.openmidaas.library.common.network.NetworkTransport;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.test.Utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MockTransport implements NetworkTransport{

	protected Context mContext;
	
	protected String mFilename;
	
	public MockTransport(Context context) {
		mContext = context;
	}
	
	public void setMockDataFile(String fname) {
		this.mFilename = fname;
	}
	
	@Override
	public void doPostRequest(boolean disableSSL, String url, HashMap<String, String> header, JSONObject data,
			AsyncHttpResponseHandler responseHandler) {
		try {
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
			
		}
	}

	@Override
	public void doGetRequest(boolean disableSSL, String url,
			Map<String, String> requestParams,
			AsyncHttpResponseHandler responseHandler) {
		// TODO Auto-generated method stub
		
	}
	

}
