/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.authentication.core;

import java.util.concurrent.Callable;


/**
 * 
 * Implement this interface to specify your own 
 * strategy for obtaining an access token. 
 *
 */
public interface AccessTokenStrategy{
	
	/**
	 * Method to obtain the access token in a background thread.
	 * If this method involves a call to the 
	 * @param callback
	 */
	public void getAccessToken(AccessToken.AccessTokenCallback callback);

}
