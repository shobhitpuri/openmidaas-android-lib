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
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public final class AndroidNetworkTransport implements NetworkTransport {

	/**
	 * @uml.property  name="tAG"
	 */
	private final String TAG = "AndroidNetworkTransport";
	
	/**
	 * @uml.property  name="mHostUrl"
	 */
	private String mHostUrl;
	
	protected AndroidNetworkTransport(String hostUrl){
		this.mHostUrl = hostUrl;
	}
	
	@Override
	public void doPostRequest(boolean withSSL, String url, HashMap<String, String> headers,JSONObject data,
			AsyncHttpResponseHandler responseHandler) {
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			if(headers != null) {
				if(headers.size() > 0) {
					for(String key: headers.keySet()) {
						client.addHeader(key, headers.get(key));
					}
				}
			}
			
			client.post(null, mHostUrl + url, new StringEntity(data.toString()), "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			MIDaaS.logError(TAG, e.getMessage());
			responseHandler.onFailure(e, e.getMessage());
		}
	}

	@Override
	public void doGetRequest(boolean withSSL, String url, Map<String, String> requestParams,
			AsyncHttpResponseHandler responseHandler) {
	}

}
