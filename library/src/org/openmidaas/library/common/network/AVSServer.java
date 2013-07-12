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
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class AVSServer {
	
	private static String TAG = "AVSServer";

	private static boolean SERVER_WITH_SSL = false;
	
	private static HashMap<String, String> headers = new HashMap<String, String>();
	
	public static void setWithSSL(boolean val) {
		SERVER_WITH_SSL = val;
	}
	
	public static void getAuthToken(SubjectToken subjectToken, String deviceToken, AsyncHttpResponseHandler responseHandler) throws JSONException {
		if(subjectToken == null) {
			MIDaaS.logError(TAG, "Subject token is missing");
			throw new IllegalArgumentException("Subject token is missing");
		}
		if(deviceToken == null || deviceToken.isEmpty()) {
			MIDaaS.logError(TAG, "Device token is missing");
			throw new IllegalArgumentException("Device token is missing");
		}
		JSONObject data = new JSONObject();
		data.put("subjectToken", subjectToken.getSignedToken());
		data.put("deviceToken", deviceToken);
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.TOKEN_URL, null, data, responseHandler);
	}
	
	/**
	 * Registers a device with the AVS server 
	 * @param deviceAuthToken the device authentication token
	 * @param responseHandler the callback once registration is complete
	 * @throws JSONException
	 */
	public static void registerDevice(String deviceToken,
			AsyncHttpResponseHandler responseHandler) throws JSONException {
		if(deviceToken == null || deviceToken.isEmpty()) {
			throw new IllegalArgumentException("Device auth token is missing");
		}
		if(responseHandler == null) {
			throw new IllegalArgumentException("Callback is missing");
		}
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.REGISTRATION_URL, null, new JSONObject().put(Constants.AVSServerJSONKeys.DEVICE_TOKEN, deviceToken), responseHandler);
	}

	/**
	 * Starts verification for the specified attribute
	 * @param attribute the attribute for which verification needs to be started
	 * @param responseHandler the callback once verification has been started 
	 * @throws JSONException
	 */
	public static void startAttributeVerification(AbstractAttribute<?> attribute,
			AsyncHttpResponseHandler responseHandler) throws JSONException {
		if(attribute == null) {
			throw new IllegalArgumentException("Attribute is missing");
		}
		if(responseHandler == null) {
			throw new IllegalArgumentException("Callback is missing");
		}
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		if(token == null) {
			MIDaaS.logError(TAG, "Error getting access token. Access token is null");
			responseHandler.onFailure(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE), "");
		} else {
			JSONObject postData = getCommonAttributeDataAsJSONObject(attribute);
			if(attribute.getVerificationMethod() != null && !(attribute.getVerificationMethod().isEmpty())) {
				postData.put(Constants.AVSServerJSONKeys.VERIFICATION_METHOD, attribute.getVerificationMethod());
			}
			ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.INIT_AUTH_URL, getAuthHeader(token), postData, responseHandler);
		}
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
		if(attribute == null) {
			throw new IllegalArgumentException("Attribute is missing");
		}
		if(responseHandler == null) {
			throw new IllegalArgumentException("Callback is missing");
		}
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		if(token == null) {
			MIDaaS.logError(TAG, "Error getting access token. Access token is null");
			responseHandler.onFailure(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE), "");
		} else {
			JSONObject postData = getCommonAttributeDataAsJSONObject(attribute);
			postData.put(Constants.AVSServerJSONKeys.CODE, verificationCode);
			postData.put(Constants.AVSServerJSONKeys.VERIFICATION_TOKEN, attribute.getPendingData());
			ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.COMPLETE_AUTH_URL, getAuthHeader(token), postData, responseHandler);		
		}

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
			data.put(Constants.RequestKeys.CLIENT_ID, clientId);
			data.put(Constants.AttributeBundleKeys.ATTRIBUTES, new JSONObject());
			if(state != null && !state.equals("")) {
				data.put(Constants.RequestKeys.STATE, state);
			}
			if(verifiedAttributeMap != null) {
				for(Map.Entry<String, AbstractAttribute<?>> entry: verifiedAttributeMap.entrySet()) {
					if(entry.getValue() != null) { 
						if(entry.getValue().getState().equals(ATTRIBUTE_STATE.VERIFIED)) {
							if(entry.getValue().getSignedToken() != null) {
								data.getJSONObject(Constants.AttributeBundleKeys.ATTRIBUTES).put(entry.getKey(), entry.getValue().getSignedToken());
							} else {
								callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
							}
						} else {
							MIDaaS.logError(TAG, "Trying to bundle an attribute that's not in a verified state.");
							callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_STATE_ERROR));
						}
					} else {
						MIDaaS.logError(TAG, "No entry value for the attribute " + entry.getKey());
						callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
						return;
					}
				}
			}
		} catch(JSONException e) {
			callback.onError(new MIDaaSException(MIDaaSError.INTERNAL_LIBRARY_ERROR));
		}
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		if(token == null) {
			MIDaaS.logError(TAG, "Error getting access token. Access token is null");
			callback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		} else {
			ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.BUNDLE_ATTRIBUTES_URL, getAuthHeader(token), data, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) { 
					if(response == null || response.isEmpty()) {
						MIDaaS.logError(TAG, "Server responded is empty.");
						callback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
					} else {
						callback.onSuccess(response);
					}
				}
				@Override
				public void onFailure(Throwable e, String response){
					MIDaaS.logError(TAG, "Server responded with error");
					callback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				}
			});
		}
	}
	
	/**
	 * Helper method to get the authorization header
	 * @param token - the access token
	 * @return - the HTTP Authorization Bearer header
	 */
	private static HashMap<String, String> getAuthHeader(AccessToken token) {
		headers.clear();
		headers.put("Authorization", "Bearer "+token.toString());
		return headers;
	}
	
	/**
	 * Helper method. Returns the type and value of an attribute in a JSON object
	 * @param attribute the attribute 
	 * @return JSONObject containing type and value of the attribute
	 * @throws JSONException 
	 */
	private static JSONObject getCommonAttributeDataAsJSONObject(AbstractAttribute<?> attribute) throws JSONException{
		JSONObject postData = new JSONObject();
		postData.put(Constants.AVSServerJSONKeys.TYPE, attribute.getName());
		postData.put(Constants.AVSServerJSONKeys.VALUE, attribute.getValue());
		return postData;
	}
}
