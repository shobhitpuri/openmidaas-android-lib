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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openmidaas.library.model.core.AuthenticationCallback;
import org.openmidaas.library.model.core.AuthenticationStrategy;
import org.openmidaas.library.model.core.DeviceTokenCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

public class AuthenticationManager  {
	
	private AuthenticationStrategy mAuthenticationStrategy;
	
	private AccessToken mAccessToken;
	
	private final Object LOCK = new Object(){};
	
	private ExecutorService executor;
	
	private AuthenticationManager() {
		mAccessToken = null;
		executor = Executors.newSingleThreadExecutor();
	}
	
	private static AuthenticationManager mInstance = null;
	
	private Thread accessTokenThread = new Thread();
	
	public static synchronized AuthenticationManager getInstance() {
		if(mInstance == null) {
			mInstance = new AuthenticationManager();
			
		}
		return mInstance;
	}
	
	public void setDeviceAuthenticationStrategy(AuthenticationStrategy strategy) {
		mAuthenticationStrategy = strategy;
	}
}
