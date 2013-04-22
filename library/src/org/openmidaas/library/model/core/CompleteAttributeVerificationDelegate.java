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
 * Delegate class that completes an attribute verification. 
 * Verification is specific to the type of attribute. For 
 * example, an email attribute will implement this class. 
 * For now it is assumed that the verification process uses a 
 * one-time code. 
 */
public interface CompleteAttributeVerificationDelegate {
	
	/**
	 * This method completes the verification 
	 * @param pin
	 * @param callback
	 */
	public void completeVerification(AbstractAttribute<?> attribute, String pin, CompleteVerificationCallback callback);

}
