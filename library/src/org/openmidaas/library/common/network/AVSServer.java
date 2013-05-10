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

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.MIDaaS.VerifiedAttributeBundleCallback;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.Constants.ATTRIBUTE_STATE;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import android.util.Base64;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class AVSServer {
	
	private static String TAG = "AVSServer";

	private static boolean SERVER_WITH_SSL = false;
	
	private static HashMap<String, String> headers = new HashMap<String, String>();
	
	private final static String CLIENT_ID = "client_id";
	
	private final static String ATTRIBUTES = "attrs";
	
	public static void setWithSSL(boolean val) {
		SERVER_WITH_SSL = val;
	}
	
	/**
	 * Registers a device with the AVS server 
	 * @param deviceAuthToken the device authentication token
	 * @param responseHandler the callback once registration is complete
	 * @throws JSONException
	 */
	public static void registerDevice(String deviceAuthToken,
			AsyncHttpResponseHandler responseHandler) throws JSONException {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.REGISTRATION_URL, null, new JSONObject().put("deviceToken", deviceAuthToken), responseHandler);
	}

	/**
	 * Starts verification for the specified attribute
	 * @param attribute the attribute for which verification needs to be started
	 * @param responseHandler the callback once verification has been started 
	 * @throws JSONException
	 */
	public static void startAttributeVerification(AbstractAttribute<?> attribute,
			AsyncHttpResponseHandler responseHandler) throws JSONException {
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		if(token == null) {
			responseHandler.onFailure(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE), "");
		}
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.INIT_AUTH_URL, getBasicAuthHeader(token), attribute.getAttributeAsJSONObject(), responseHandler);
	}

	/**
	 * Completes verification for the specified attribute
	 * @param attribute the attribute for which verification needs to be completed
	 * @param verificationCode the verification code
	 * @param responseHandler the callback once verification is completed
	 * @throws JSONException
	 */
	public static void completeAttributeVerification(AbstractAttribute<?> attribute, String verificationCode,
			AsyncHttpResponseHandler responseHandler) throws JSONException {
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		if(token == null) {
			responseHandler.onFailure(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE), "");
		}
		JSONObject object = attribute.getAttributeAsJSONObject();
		object.put("code", verificationCode);
		object.put("verificationToken", attribute.getPendingData());
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.COMPLETE_AUTH_URL, getBasicAuthHeader(token), object, responseHandler);	
	}
	/**
	 * Bundles the attributes for the app. 
	 * @param clientId
	 * @param state
	 * @param verifiedAttributeMap
	 * @param responseHandler
	 * @throws JSONException
	 */
	public static void bundleVerifiedAttributes(String clientId, String state, Map<String, AbstractAttribute<?>> verifiedAttributeMap, 
			final VerifiedAttributeBundleCallback callback) {
		JSONObject data = new JSONObject();
		try {
			data.put(CLIENT_ID, clientId);
			data.put(ATTRIBUTES, new JSONObject());
			for(Map.Entry<String, AbstractAttribute<?>> entry: verifiedAttributeMap.entrySet()) {
				if(entry.getValue() != null) { 
					if(entry.getValue().getState().equals(ATTRIBUTE_STATE.VERIFIED)) {
						if(entry.getValue().getSignedToken() != null) {
							data.getJSONObject(ATTRIBUTES).put(entry.getKey(), entry.getValue().getSignedToken());
						} else {
							callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
						}
					} else {
						callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_STATE_ERROR));
					}
				} else {
					callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
				}
			}
		} catch(JSONException e) {
			callback.onError(new MIDaaSException(MIDaaSError.INTERNAL_LIBRARY_ERROR));
		}
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		if(token == null) {
			callback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		}
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.BUNDLE_ATTRIBUTES_URL, getBasicAuthHeader(token), data, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) { 
				if(response == null || response.isEmpty()) {
					callback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				} else {
					callback.onSuccess(response);
				}
			}
			@Override
			public void onFailure(Throwable e, String response){
				callback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
			}
		});
	}
	
	/**
	 * Helper method to get the basic auth header
	 * @param token - the access token
	 * @return - the HTTP basic auth header
	 */
	private static HashMap<String, String> getBasicAuthHeader(AccessToken token) {
		try {
			headers.clear();
			headers.put("Authorization", "Basic "+Base64.encodeToString(token.toString().getBytes("UTF-8"), Base64.NO_WRAP));
			return headers;
		} catch (UnsupportedEncodingException e) {
			MIDaaS.logError(TAG, e.getMessage());
		}
		return headers;
	}
}
