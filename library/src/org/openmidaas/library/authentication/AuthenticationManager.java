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
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.DeviceTokenCallback;

public class AuthenticationManager  {
	
	private AccessToken mAccessToken;
	
	private AccessTokenStrategy mAccessTokenStrategy;
	
	private CountDownLatch MUTEX;
	
	private final String TAG = "AuthenticationManager";
	
	private AuthenticationManager() {
		mAccessToken = null;
	}
	
	private static AuthenticationManager mInstance = null;
	
	public static synchronized AuthenticationManager getInstance() {
		if(mInstance == null) {
			mInstance = new AuthenticationManager();
		}
		return mInstance;
	}
	
	public void setAccessTokenStrategy(AccessTokenStrategy strategy) {
		mAccessTokenStrategy = strategy;
	}
	
	/**
	 * Blocking operation. Waits till the access token 
	 * is obtained. 
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
		
	}
}
