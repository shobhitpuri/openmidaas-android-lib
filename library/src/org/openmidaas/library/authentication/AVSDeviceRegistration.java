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
package org.openmidaas.library.authentication;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.authentication.core.DeviceRegistrationDelegate;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.SubjectTokenFactory;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

import android.os.Build;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class AVSDeviceRegistration implements DeviceRegistrationDelegate {
	
	private final String TAG = "DeviceRegistration";
	
	private DeviceAuthenticationStrategy mAuthenticationStrategy;
	
	private InitializationCallback mInitCallback;
	
	private SubjectToken deviceToken;
	
	public AVSDeviceRegistration() {
		mAuthenticationStrategy = AuthenticationManager.getInstance().getDeviceAuthenticationStrategy();
	}
	
	public void registerDevice(final InitializationCallback initCallback) {
		mInitCallback = initCallback;
		// first get the subject token
		AttributePersistenceCoordinator.getSubjectToken(new SubjectTokenCallback() {

			@Override
			public void onSuccess(List<SubjectToken> list) {
				// if list is empty, it means that the device isn't registered. 
				if (list.isEmpty()) {
					
					MIDaaS.logDebug(TAG, "Device NOT registered. Registering device.");
					mInitCallback.onRegistering();
					authenticateDevice();
					
				} else if(list.size() > 1) {
					// if we have more than one device attribute, we have an error. 
					MIDaaS.logError(TAG, "Two or more device registrations already exist. ");
					initCallback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else {
					MIDaaS.logDebug(TAG, "Device already registered.");
					initCallback.onSuccess();
				}
				
			}

			@Override
			public void onError(MIDaaSException exception) {
				MIDaaS.logError(TAG, "Error registering device.");
				initCallback.onError(exception);
			}
		});

	}
	
	
	private void performRegistration(String deviceId) {     
		try {
			AVSServer.registerDevice(deviceId, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					try {
						MIDaaS.logDebug(TAG, "device successfully registered. persisting registration.");
						JSONObject responseObject = new JSONObject(response);
						if(responseObject.has("subjectToken") && !(responseObject.isNull("subjectToken"))) {
							deviceToken =SubjectTokenFactory.createAttribute();
							deviceToken.setValue(Build.MODEL);
							deviceToken.setSignedToken(responseObject.getString("subjectToken"));
							deviceToken.save();
							// if we didn't get the access token, we can get it on-demand at a later time. 
							if((responseObject.has("accessToken") && !(responseObject.isNull("accessToken"))) 
									&& (responseObject.has("expiresIn") && !(responseObject.isNull("expiresIn")))) {
								AccessToken token = AccessToken.createAccessToken(responseObject.getString("accessToken"), responseObject.getInt("expiresIn"));
								if(token != null) {
									AuthenticationManager.getInstance().setAccessToken(token);
								} else {
									mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
								}
							}
						} else {
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
				
				@Override
				public void onFailure(Throwable e, String response){
					MIDaaS.logError(TAG, response);
					mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				}
			});
		} catch (JSONException e) {
			MIDaaS.logError(TAG, e.getMessage());
			mInitCallback.onError(null);
		}
	}
	
	private void authenticateDevice() {
		mAuthenticationStrategy.performDeviceAuthentication(new DeviceAuthenticationCallback() {

			@Override
			public void onSuccess(String deviceId) {
				MIDaaS.logDebug(TAG, "device successfully authenticated. registering device with server now.");
				performRegistration(deviceId);
			}

			@Override
			public void onError(MIDaaSException exception) {
				MIDaaS.logError(TAG, "error authenticating device.");
				mInitCallback.onError(exception);
			}
			
		});
	}
}
