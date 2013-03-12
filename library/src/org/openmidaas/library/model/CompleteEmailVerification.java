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

import org.openmidaas.library.model.core.CompleteAttributeVerificationDelegate;
import org.openmidaas.library.model.core.CompleteVerificationCallback;

/**
 * This class implements the delegate class that completes the 
 * attribute verification process. It sends the one-time code 
 * collected via a GUI to the server for verification. 
 * The result is returned via a callback.
 */
public class CompleteEmailVerification implements CompleteAttributeVerificationDelegate{

	/**
	 * This methods is an implementation of the interface that 
	 * completes an attribute verification. 
	 * Here the one-time code is send to the server and the result
	 * is send back to the callver via a callback.
	 */
	@Override
	public void completeVerification(String code, CompleteVerificationCallback callback) {
		callback.onSuccess();
	}

}
