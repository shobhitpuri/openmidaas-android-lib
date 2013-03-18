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



import java.util.Map;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ConnectionManager {
	
	private static INetworkFactory mNetworkFactory;
	
	private static INetworkTransport mNetworkTransport;
	
	public static void setNetworkFactory(INetworkFactory networkFactory) {
		mNetworkFactory = networkFactory;
		mNetworkTransport = mNetworkFactory.createTransport();
	}
	
	public static void postRequest(boolean withSSL, String url, JSONObject data, AsyncHttpResponseHandler responseHandler) {
		mNetworkTransport.doPostRequest(withSSL, url, data, responseHandler);
	}
	
	public static void getRequest(boolean withSSL, String url, Map<String, String> requestParams, AsyncHttpResponseHandler responseHandler) {
		mNetworkTransport.doGetRequest(withSSL, url, requestParams, responseHandler);
	}
}
