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
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.SubjectTokenFactory;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import android.os.Build;

import com.loopj.android.http.AsyncHttpResponseHandler;
/**
 * 
 * This class implements the device authentication callback. 
 * Once the device auth token is obtained, it is sent to the 
 * server for registration. A subject token is created from the 
 * response. 
 *
 */
public class AuthCallbackForRegistration implements DeviceAuthenticationCallback {
	
	private final String TAG = "RegistrationAuthDelegate";
	
	private InitializationCallback mInitCallback;
	
	protected AuthCallbackForRegistration(){}
	
	public void setInitCallback(InitializationCallback callback) {
		mInitCallback = callback;
	}

	@Override
	public void onSuccess(String deviceToken) {
		try {
			AVSServer.registerDevice(deviceToken, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					if(response == null || response.isEmpty()) {
						mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
					} else {
						try {
							MIDaaS.logDebug(TAG, "device successfully registered. persisting registration.");
							JSONObject responseObject = new JSONObject(response);
							if(responseObject.has("subjectToken") && !(responseObject.isNull("subjectToken"))) {
								SubjectToken subjectToken =SubjectTokenFactory.createAttribute();
								subjectToken.setValue(Build.MODEL);
								subjectToken.setSignedToken(responseObject.getString("subjectToken"));
								subjectToken.save();
								// if we didn't get the access token, we can get it on-demand at a later time. 
								if((responseObject.has(Constants.AccessTokenKeys.ACCESS_TOKEN) && !(responseObject.isNull(Constants.AccessTokenKeys.ACCESS_TOKEN))) 
										&& (responseObject.has(Constants.AccessTokenKeys.EXPIRES_IN) && !(responseObject.isNull(Constants.AccessTokenKeys.EXPIRES_IN)))) {
									MIDaaS.logDebug(TAG, "Registration response has an access token.");
									AccessToken token = AccessToken.createAccessToken(responseObject.getString(Constants.AccessTokenKeys.ACCESS_TOKEN), responseObject.getInt(Constants.AccessTokenKeys.EXPIRES_IN));
									if(token != null) {
										MIDaaS.logDebug(TAG, "Access token is ok.");
										AuthenticationManager.getInstance().setAccessToken(token);
									} else {
										MIDaaS.logError(TAG, "Access token is null.");
										mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
									}
								} else {
									MIDaaS.logDebug(TAG, "No access token object in server response. Access token will be created on-demand.");
								}
							} else {
								MIDaaS.logError(TAG, "Server response doesn't match expected response");
								mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
							}
							mInitCallback.onSuccess();
						} catch (InvalidAttributeValueException e) {
							// should never get here b/c we're returning true. 
							MIDaaS.logError(TAG, "logic error. should never have thrown exception");
						} catch (MIDaaSException e) {
							MIDaaS.logError(TAG, e.getError().getErrorMessage());
							mInitCallback.onError(e);
							
						} catch (JSONException e) {
							MIDaaS.logError(TAG, e.getMessage());
							mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
						}
					}
				}
				
				@Override
				public void onFailure(Throwable e, String response){
					MIDaaS.logError(TAG, response);
					mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				}
			});
		} catch (JSONException e) {
			MIDaaS.logError(TAG, "Internal error");
			MIDaaS.logError(TAG, e.getMessage());
			mInitCallback.onError(null);
		}
		
	}

	@Override
	public void onError(MIDaaSException exception) {
		MIDaaS.logError(TAG, exception.getError().getErrorMessage());
		mInitCallback.onError(exception);
	}
}
