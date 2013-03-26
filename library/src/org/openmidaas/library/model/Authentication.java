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

import org.openmidaas.library.model.core.AuthenticationStrategy;

public class Authentication {
	
	private AuthenticationStrategy mAuthenticationStrategy;
	
	private Authentication() {}
	
	private static Authentication mInstance = null;
	
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
	 * @return
	 */
	public String getAccessToken() {
		return null;
	}
}
