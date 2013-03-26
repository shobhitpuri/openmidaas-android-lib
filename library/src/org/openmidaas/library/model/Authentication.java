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

package org.openmidaas.library.model;

import java.util.List;

import org.openmidaas.library.model.core.AuthenticationCallback;
import org.openmidaas.library.model.core.AuthenticationStrategy;
import org.openmidaas.library.model.core.DeviceTokenCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

public class Authentication {
	
	private AuthenticationStrategy mAuthenticationStrategy;
	
	private String mAccessToken;
	
	private final Object LOCK = new Object(){};
	
	private Authentication() {
		mAccessToken = null;
	}
	
	private static Authentication mInstance = null;
	
	private Thread accessTokenThread = new Thread();
	
	public static synchronized Authentication getInstance() {
		if(mInstance == null) {
			mInstance = new Authentication();
		}
		return mInstance;
	}
	
	public void setAuthenticationStrategy(AuthenticationStrategy strategy) {
		mAuthenticationStrategy = strategy;
	}
	
	/**
	 * Blocking operation that returns an access token. 
	 * @return an access token
	 */
//	public String getAccessToken() {
//	}
//	
//	private void getSubjectToken() {
//		AttributePersistenceCoordinator.getDeviceAttribute(new DeviceTokenCallback() {
//
//			@Override
//			public void onSuccess(List<DeviceAttribute> list) {
//				
//			}
//
//			@Override
//			public void onError(MIDaaSException exception) {
//				
//			}
//			
//		});
//	}
//	
//	private String getDeviceAuthenticationToken() {
//		mAuthenticationStrategy.performAuthentication(new AuthenticationCallback() {
//
//			@Override
//			public void onSuccess(String deviceId) {
//				
//			}
//
//			@Override
//			public void onError(MIDaaSException exception) {
//				
//			}
//			
//		});
//	}

	
	
}
