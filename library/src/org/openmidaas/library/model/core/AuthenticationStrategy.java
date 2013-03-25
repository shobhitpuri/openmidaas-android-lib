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
 * Implement this interface to use your
 * specific authentication strategy.
 */
public interface AuthenticationStrategy {
	
	/**
	 * This method performs the actual authentication and 
	 * returns the result via the callback handler. 
	 * Make sure that this method is thread safe.
	 * @param callback - the authentication callback handler. 
	 */
	public void performAuthentication(AuthenticationCallback callback);
	
}
