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

import java.util.List;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.authentication.core.DeviceRegistrationDelegate;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

public class AVSDeviceRegistration implements DeviceRegistrationDelegate {
	
	
	private final String TAG = "DeviceRegistration";
	
	private DeviceAuthenticationStrategy mAuthenticationStrategy;
	
	private InitializationCallback mInitCallback;
	
	
	public AVSDeviceRegistration() {
		mAuthenticationStrategy = AuthenticationManager.getInstance().getDeviceAuthenticationStrategy();
	}
	
	public void registerDevice(final InitializationCallback initCallback) {
		mInitCallback = initCallback;
		// first get the subject token
		AttributePersistenceCoordinator.getSubjectToken(new SubjectTokenCallback() {

			@Override
			public void onSuccess(List<SubjectToken> list) {
				// if list is empty, it means that the device isn't registered. 
				if (list.isEmpty()) {
					
					MIDaaS.logDebug(TAG, "Device NOT registered. Registering device.");
					mInitCallback.onRegistering();
					AuthCallbackForRegistration deviceAuthCallback = new AuthCallbackForRegistration();
					deviceAuthCallback.setInitCallback(mInitCallback);
					mAuthenticationStrategy.performDeviceAuthentication(deviceAuthCallback);
					
				} else if(list.size() > 1) {
					// if we have more than one device attribute, we have an error. 
					MIDaaS.logError(TAG, "Two or more device registrations already exist. ");
					initCallback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else {
					MIDaaS.logDebug(TAG, "Device already registered.");
					initCallback.onSuccess();
				}
				
			}

			@Override
			public void onError(MIDaaSException exception) {
				MIDaaS.logError(TAG, "Error registering device.");
				initCallback.onError(exception);
			}
		});

	}
}
