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

import org.openmidaas.library.model.core.InitializationCallback;

/**
 * Interface to perform device registration with the server. 
 *
 */
public interface DeviceRegistrationDelegate {
	
	/**
	 * This method registers a device to the server when the library 
	 * is initialized 
	 * @param callback - the initialization callback passed in from the app. 
	 */
	public void registerDevice(InitializationCallback callback);

}
