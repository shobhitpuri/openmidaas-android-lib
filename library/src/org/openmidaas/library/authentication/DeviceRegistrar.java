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
package org.openmidaas.library.authentication;

import org.openmidaas.library.authentication.core.DeviceRegistrationDelegate;
import org.openmidaas.library.model.core.InitializationCallback;

/**
 * 
 * Wrapper to set the device registration delegate and 
 * perform device registration
 *
 */
public class DeviceRegistrar {
	private static DeviceRegistrationDelegate mDelegate = null;
	
	/**
	 * Sets the device registration delegate. 
	 * @param delegate device registration delegate
	 */
	public static void setDeviceRegistrationDelegate(DeviceRegistrationDelegate delegate) {
		mDelegate = delegate;
	}
	
	/**
	 * Registers the device using the specified delegate
	 * @param callback the initialization callback 
	 */
	public static void registerDevice(InitializationCallback callback) {
		mDelegate.registerDevice(callback);
	}
}
