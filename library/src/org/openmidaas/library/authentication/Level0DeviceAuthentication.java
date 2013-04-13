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
package org.openmidaas.library.authentication;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import android.provider.Settings.Secure;

/**
 * 
 * Level 0 device authentication that gets the device ID built in 
 * the device
 *
 */
public class Level0DeviceAuthentication implements DeviceAuthenticationStrategy{

	@Override
	public void performDeviceAuthentication(
			DeviceAuthenticationCallback callback) {
		String deviceId = Secure.getString(MIDaaS.getContext().getContentResolver(),
                Secure.ANDROID_ID); 
		if(deviceId != null) {
			callback.onSuccess(deviceId);
		} else {
			callback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		}
	}
}
