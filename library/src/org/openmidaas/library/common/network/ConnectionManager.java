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
	
	private INetworkFactory mNetworkFactory;
	
	private INetworkTransport mNetworkTransport;
	
	protected ConnectionManager() {
		mIsSSLDisabled = false;
	}
	
	public static synchronized ConnectionManager getInstance() {
		if (mInstance == null) {
			mInstance = new ConnectionManager();
		}
		return mInstance;
	}
	
	public void setNetworkFactory(INetworkFactory networkFactory) {
		mNetworkFactory = networkFactory;
		mNetworkTransport = mNetworkFactory.createTransport();
	}
	
	public boolean isSSLDisabled() {
		return mIsSSLDisabled;
	}
	
	public void setSSL(boolean enableSSL) {
		mIsSSLDisabled = enableSSL;
	}
	
	public void postRequest(String url, JSONObject data, AsyncHttpResponseHandler responseHandler) {
		mNetworkTransport.doPostRequest(true, url, data, responseHandler);
	}
	
	public void getRequest(String url, Map<String, String> requestParams, AsyncHttpResponseHandler responseHandler) {
		mNetworkTransport.doGetRequest(true, url, requestParams, responseHandler);
	}
}
