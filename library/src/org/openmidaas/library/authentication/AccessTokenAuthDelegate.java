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

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class AccessTokenAuthDelegate implements DeviceAuthenticationCallback{

	private final String TAG = "DeviceAuthForAccessToken";
	
	private AccessTokenCallback mCallback = null;
	
	protected AccessTokenAuthDelegate(){}
	
	public void setAccessTokenCallback(AccessTokenCallback callback) {
		mCallback = callback;
	}
	
	@Override
	public void onSuccess(final String deviceToken) {
		AttributePersistenceCoordinator.getSubjectToken(new SubjectTokenCallback() {
			
			@Override
			public void onSuccess(List<SubjectToken> list) {
				MIDaaS.logDebug(TAG, "device authentication successful...");
				// we now have the device token. With that, we will create an access token. 
				// the access token is a combination of the device auth token and subject token. 
				if(list.isEmpty()) {
					MIDaaS.logError(TAG, "no subject token");
					mCallback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else if(list.size() > 1) {
					MIDaaS.logError(TAG, "multiple subject tokens..error");
					mCallback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else {
					MIDaaS.logDebug(TAG, "returning new access token");
					try {
						obtainAccessToken(list.get(0), deviceToken);
					} catch (JSONException e) {
						
					}
				}
			}

			@Override
			public void onError(MIDaaSException exception) {
				mCallback.onError(exception);
			}
		});
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
					mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				} else {
					try {
						JSONObject accessToken = new JSONObject(response);
						if((accessToken.has("accessToken") && !(accessToken.isNull("accessToken"))) 
								&& (accessToken.has("expiresIn") && !(accessToken.isNull("expiresIn")))) {
							AccessToken token = AccessToken.createAccessToken(accessToken.getString("accessToken"), accessToken.getInt("expiresIn"));
							if(token != null) {
								mCallback.onSuccess(token);
							} else {
								mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
							}
						}
					
					} catch (JSONException e) {
						mCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
					}
					
				}
				
			}
			@Override
			public void onFailure(Throwable e, String response){
				
			}
		});
	}
}
