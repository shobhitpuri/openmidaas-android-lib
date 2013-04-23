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


/**
 * Delegate class that starts the attribute verification process.
 * Each attribute type will implement their process to start verification.
 *
 */

public interface InitializeAttributeVerificationDelegate {
	
	/**
	 * This method starts the attribute verification process and the result
	 * is returned via a callback.
	 * @param callback - callback that returns the result of the start
	 * 					 verification process.
	 */
	public void startVerification(AbstractAttribute<?> attribute, InitializeVerificationCallback callback);

}
