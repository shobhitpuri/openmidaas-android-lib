package org.openmidaas.library.model.core;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.network.ConnectionManager;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Build;

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
		ConnectionManager.getInstance().postRequest(Constants.REGISTRATION_URL, registrationData, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				//TODO: Persist the signed user ID token. 
				
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
