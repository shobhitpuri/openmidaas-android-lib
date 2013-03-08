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

import org.openmidaas.library.model.core.InitializeAttributeVerificationDelegate;
import org.openmidaas.library.model.core.InitializeVerificationCallback;

/**
 * Class that implements the delegate that initializes
 * attribute verification. 
 */
public class InitializeEmailVerification implements InitializeAttributeVerificationDelegate {

	/**
	 * The method calls the server with the provided attribute 
	 * value (email address) and returns the result via a callback to 
	 * the caller. 
	 */
	@Override
	public void startVerification(InitializeVerificationCallback callback) {
		//TODO: call the server to start email verification and call callback.onSuccess();/callback.onError() accordingly
		callback.onSuccess();
	}

}
