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
