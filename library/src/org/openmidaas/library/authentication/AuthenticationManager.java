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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSException;


public class AuthenticationManager  {

	/**
	 * @uml.property  name="aCCESS_TOKEN_TIMEOUT_MS"
	 */
	private final int ACCESS_TOKEN_TIMEOUT_MS = 1000;
	
	/**
	 * @uml.property  name="mAccessToken"
	 * @uml.associationEnd  
	 */
	private AccessToken mAccessToken;
	
	/**
	 * @uml.property  name="mAccessTokenStrategy"
	 * @uml.associationEnd  
	 */
	private AccessTokenStrategy mAccessTokenStrategy;
	
	/**
	 * @uml.property  name="mDeviceAuthStrategy"
	 * @uml.associationEnd  
	 */
	private DeviceAuthenticationStrategy mDeviceAuthStrategy;
	
	/**
	 * @uml.property  name="tAG"
	 */
	private final String TAG = "AuthenticationManager";
	
	private AuthenticationManager() {
		mAccessToken = null;
	}
	
	private static AuthenticationManager mInstance = null;
	
	/**
	 * Returns a single instance of the AuthenticationManager object
	 * @return -  AuthenticationManager object 
	 */
	public static synchronized AuthenticationManager getInstance() {
		if(mInstance == null) {
			mInstance = new AuthenticationManager();
		}
		return mInstance;
	}
	
	/**
	 * Sets the access token strategy
	 * @param accessTokenstrategy the access token strategy
	 */
	public void setAccessTokenStrategy(AccessTokenStrategy accessTokenstrategy) {
		mAccessToken = null;
		mAccessTokenStrategy = accessTokenstrategy;
	}
	
	/**
	 * Sets the device authentication strategy
	 * @param deviceStrategy  the device authentication strategy
	 */
	public void setDeviceAuthenticationStrategy(DeviceAuthenticationStrategy deviceStrategy) {
		mAccessToken = null;
		mDeviceAuthStrategy = deviceStrategy;
	}
	
	/**
	 * Returns the set device authentication strategy
	 * @return
	 */
	public DeviceAuthenticationStrategy getDeviceAuthenticationStrategy() {
		return mDeviceAuthStrategy;
	}
	
	/**
	 * Blocking operation. Waits till the access token 
	 * is obtained. 
	 * @return the access token object or null if unable to get 
	 * the access token. The client should handle retries if required
	 * when a null access token is returned.
	 */
	public synchronized AccessToken getAccessToken() {
		final CountDownLatch MUTEX = new CountDownLatch(1);
		if(mAccessToken == null || mAccessToken.isExpired()){
			MIDaaS.logDebug(TAG, "getting a new access token");
			mAccessTokenStrategy.getAccessToken(new AccessTokenCallback() {
				@Override
				public void onSuccess(AccessToken accessToken) {
					mAccessToken = accessToken;
					MUTEX.countDown();
				}
		
				@Override
				public void onError(MIDaaSException exception) {
					MIDaaS.logError(TAG, exception.getError().getErrorMessage());
					MUTEX.countDown();
				}
			});
			try {
				MUTEX.await(ACCESS_TOKEN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				MIDaaS.logError(TAG, e.getMessage());
			}
			return mAccessToken;	
		} 
		MIDaaS.logDebug(TAG, "access token OK, returning...");
		return mAccessToken;
	}
}
