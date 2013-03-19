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

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.core.AuthenticationCallback;
import org.openmidaas.library.model.core.AuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import android.provider.Settings.Secure;

public class DeviceIdAuthentication implements AuthenticationStrategy{

	@Override
	public void performAuthentication(
			AuthenticationCallback callback) {
		String deviceId = Secure.getString(MIDaaS.getContext().getContentResolver(),
                Secure.ANDROID_ID); 
		if(deviceId != null) {
			callback.onSuccess(deviceId);
		} else {
			callback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		}
	}
}
