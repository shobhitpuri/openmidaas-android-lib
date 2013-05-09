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

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;

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
	public void getAccessToken(AccessTokenCallback callback) {
		MIDaaS.logDebug(TAG, "authenticationg device with: "  +mDeviceAuthStrategy.getClass().getName());
		AccessTokenAuthDelegate authForAccessToken = new AccessTokenAuthDelegate();
		authForAccessToken.setAccessTokenCallback(callback);
		mDeviceAuthStrategy.performDeviceAuthentication(authForAccessToken);
	}
}
