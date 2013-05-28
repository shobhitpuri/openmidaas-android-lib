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

import java.util.List;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

/**
 * 
 * AVS-specific implementation of the 
 * access token strategy. 
 *
 */
public class AVSAccessTokenStrategy implements AccessTokenStrategy {
	
	private final String TAG = "AVSAccessTokenStrategy";

	private DeviceAuthenticationStrategy mDeviceAuthStrategy;
	
	public AVSAccessTokenStrategy() {
		mDeviceAuthStrategy = AuthenticationManager.getInstance().getDeviceAuthenticationStrategy();
	}
	
	@Override
	public void getAccessToken(final AccessTokenCallback callback) {
		MIDaaS.logDebug(TAG, "Authenticationg device with: "  +mDeviceAuthStrategy.getClass().getName());
		AttributePersistenceCoordinator.getSubjectToken(new SubjectTokenCallback() {
			
			@Override
			public void onSuccess(List<SubjectToken> list) {
				MIDaaS.logDebug(TAG, "device authentication successful...");
				// we now have the device token. With that, we will create an access token. 
				// the access token is a combination of the device auth token and subject token. 
				if(list.isEmpty()) {
					MIDaaS.logError(TAG, "no subject token");
					callback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else if(list.size() > 1) {
					MIDaaS.logError(TAG, "multiple subject tokens..error");
					callback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else {
					MIDaaS.logDebug(TAG, "fetching access token from the server");
					
					AuthCallbackForAccessToken authForAccessToken = new AuthCallbackForAccessToken();
					authForAccessToken.setAccessTokenCallback(callback);
					authForAccessToken.setSubjectToken(list.get(0));
					MIDaaS.logDebug(TAG, "Starting device authentication");
					mDeviceAuthStrategy.performDeviceAuthentication(authForAccessToken);
				
				}
			}

			@Override
			public void onError(MIDaaSException exception) {
				MIDaaS.logError(TAG, exception.getError().getErrorMessage());
				callback.onError(exception);
			}
		});
		
	}
}
