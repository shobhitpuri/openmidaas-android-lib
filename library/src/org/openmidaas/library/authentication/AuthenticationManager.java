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
import java.util.concurrent.Semaphore;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSException;


public class AuthenticationManager  {
	
	private AccessToken mAccessToken;
	
	private AccessTokenStrategy mAccessTokenStrategy;
	
	private DeviceAuthenticationStrategy mDeviceAuthStrategy;
	
	private CountDownLatch MUTEX;
	
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
		mAccessTokenStrategy = accessTokenstrategy;
	}
	
	/**
	 * Sets the device authentication strategy
	 * @param deviceStrategy  the device authentication strategy
	 */
	public void setDeviceAuthenticationStrategy(DeviceAuthenticationStrategy deviceStrategy) {
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
	 * the access token
	 */
	public synchronized AccessToken getAccessToken() {
		MUTEX = new CountDownLatch(1);
		if(mAccessToken == null){
			MIDaaS.logDebug(TAG, "getting a new access token");
			mAccessTokenStrategy.getAccessToken(new AccessTokenCallback() {
				@Override
				public void onSuccess(AccessToken accessToken) {
					mAccessToken = accessToken;
					synchronized(MUTEX) {
						MUTEX.countDown();
					}
				}
		
				@Override
				public void onError(MIDaaSException exception) {
					synchronized(MUTEX) {
						MUTEX.countDown();
					}
				}
			});
			synchronized(MUTEX) {
				try {
					MUTEX.await();
				} catch (InterruptedException e) {
					mAccessToken = null;
				}
			}
			return mAccessToken;	
		} else {
			MIDaaS.logDebug(TAG, "access token OK, returning...");
			return mAccessToken;
		}
	}
	
	/**
	 * Request for an access token in the background.
	 */
	public void getAccessTokenInBackground() {
		//TODO: Future work
	}
}
