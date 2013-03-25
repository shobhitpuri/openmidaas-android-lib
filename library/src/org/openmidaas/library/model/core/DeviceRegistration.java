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
package org.openmidaas.library.model.core;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.DeviceAttribute;
import org.openmidaas.library.model.DeviceAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class DeviceRegistration {
	
	private AuthenticationStrategy mAuthenticationStrategy;
	
	private InitializationCallback mInitCallback;
	
	private DeviceAttribute deviceToken;
	
	public DeviceRegistration(AuthenticationStrategy authenticationStrategy) {
		mAuthenticationStrategy = authenticationStrategy;
		
	}
	
	public void registerDevice(InitializationCallback initCallback) {
		mInitCallback = initCallback;
		authenticateDevice();
	}
	
	
	private void performRegistration(String deviceId) {
		JSONObject registrationData = new JSONObject();
		try {
			registrationData.put("deviceToken", deviceId);
		} catch (JSONException e) {
			mInitCallback.onError(null);
		}
		AVSServer.registerDevice(registrationData, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				//TODO: Persist the signed user ID token. 
				try {
					deviceToken = AttributeFactory.createDeviceAttributeFactory().createAttribute("device");
					deviceToken.setSignedToken(response);
				} catch (InvalidAttributeValueException e) {
					// should never get here b/c we're returning true. 
				}
				
				AttributePersistenceCoordinator.saveAttribute(deviceToken, new PersistenceCallback() {

					@Override
					public void onSuccess() {
						mInitCallback.onSuccess();					
						}

					@Override
					public void onError(MIDaaSException exception) {
						mInitCallback.onError(exception);				
						}
				
				});
			}
			
			@Override
			public void onFailure(Throwable e, String response){
				mInitCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
			}
		});
	}
	
	private void authenticateDevice() {
		mAuthenticationStrategy.performAuthentication(new AuthenticationCallback() {

			@Override
			public void onSuccess(String deviceId) {
				performRegistration(deviceId);
			}

			@Override
			public void onError(MIDaaSException exception) {
				mInitCallback.onError(exception);
			}
			
		});
	}
	

}
