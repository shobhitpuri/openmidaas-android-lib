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
package org.openmidaas.library.authentication;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * Class that implements a device authentication callback interface. This class 
 * gets the device auth token, sends it to the server and creates an access token 
 * from the response
 *
 */
public class AuthCallbackForAccessToken implements DeviceAuthenticationCallback{

	private final String TAG = "DeviceAuthForAccessToken";
	
	private AccessTokenCallback mCallback = null;
	
	private SubjectToken mSubjectToken = null;
	
	protected AuthCallbackForAccessToken(){}
	
	public void setAccessTokenCallback(AccessTokenCallback callback) {
		mCallback = callback;
	}
	
	public void setSubjectToken(SubjectToken token) {
		mSubjectToken = token;
	}
	
	@Override
	public void onSuccess(final String deviceToken) {
		try {
			obtainAccessToken(mSubjectToken, deviceToken);
		} catch (JSONException e) {
			MIDaaS.logError(TAG, e.getMessage());
			mCallback.onError(new MIDaaSException(MIDaaSError.INTERNAL_LIBRARY_ERROR));
		}
	}

	@Override
	public void onError(MIDaaSException exception) {
		mCallback.onError(exception);
	}
	
	private void obtainAccessToken(SubjectToken subjectToken, String deviceToken) throws JSONException {
		AVSServer.getAuthToken(subjectToken, deviceToken, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) { 
				if(response == null || response.isEmpty()) {
					MIDaaS.logError(TAG, "Server response is empty.");
					mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				} else {
					try {
						JSONObject accessToken = new JSONObject(response);
						if((accessToken.has(Constants.AccessTokenKeys.ACCESS_TOKEN) && !(accessToken.isNull(Constants.AccessTokenKeys.ACCESS_TOKEN))) 
								&& (accessToken.has(Constants.AccessTokenKeys.EXPIRES_IN) && !(accessToken.isNull(Constants.AccessTokenKeys.EXPIRES_IN)))) {
							AccessToken token = AccessToken.createAccessToken(accessToken.getString(Constants.AccessTokenKeys.ACCESS_TOKEN), 
									accessToken.getInt(Constants.AccessTokenKeys.EXPIRES_IN));
							if(token != null) {
								MIDaaS.logDebug(TAG, "got access token: ");
								mCallback.onSuccess(token);
							} else {
								MIDaaS.logError(TAG, "Error could not create access token");
								mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
							}
						} else {
							MIDaaS.logError(TAG, "Server response is not what is expected");
							mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
						}
					
					} catch (JSONException e) {
						MIDaaS.logError(TAG, "Internal error while parsing server JSON response");
						mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
					}
					
				}
				
			}
			@Override
			public void onFailure(Throwable e, String response){
				MIDaaS.logError(TAG, "Server responded with error " + response);
				mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
			}
		});
	}
}
