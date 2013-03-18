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

import org.json.JSONObject;
import org.openmidaas.library.common.Constants;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class AVSServer {

	private static boolean SERVER_WITH_SSL = false;
	
	
	public static void setWithSSL(boolean val) {
		SERVER_WITH_SSL = val;
	}
	
	public static void registerDevice(JSONObject registrationData,
			AsyncHttpResponseHandler responseHandler) {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.REGISTRATION_URL, registrationData, responseHandler);
	}

	public static void startAttributeVerification(JSONObject attributeData,
			AsyncHttpResponseHandler responseHandler) {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.INIT_AUTH_URL, attributeData, responseHandler);
	}

	public static void completeAttributeVerification(JSONObject attributeData,
			AsyncHttpResponseHandler responseHandler) {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.COMPLETE_AUTH_URL, attributeData, responseHandler);	
	}
}
