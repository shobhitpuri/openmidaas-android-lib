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
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.network.AVSServer;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class DeviceRegistration {
	
	private AuthenticationStrategy mAuthenticationStrategy;
	
	private InitializationCallback mInitCallback;
	
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
			registrationData.put("nickname", Build.MODEL);
		} catch (JSONException e) {
			mInitCallback.onError(null);
		}
		AVSServer.registerDevice(registrationData, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				//TODO: Persist the signed user ID token. 
				SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MIDaaS.getContext()).edit();
				editor.putBoolean("REGISTERED_KEY_NAME", true);
				editor.commit();
				mInitCallback.onSuccess();
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
