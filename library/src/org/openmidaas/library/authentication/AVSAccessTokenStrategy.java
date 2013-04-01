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
import java.util.concurrent.Callable;

import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.model.DeviceAttribute;
import org.openmidaas.library.model.core.DeviceTokenCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

/**
 * 
 * AVS-specific implementation of the 
 * access token strategy. 
 *
 */
public class AVSAccessTokenStrategy implements AccessTokenStrategy, DeviceAuthenticationCallback {

	private DeviceAuthenticationStrategy mDeviceAuthStrategy;
	
	private AccessToken.AccessTokenCallback mAccessTokenCallback;
	
	public AVSAccessTokenStrategy(DeviceAuthenticationStrategy strategy) {
		mDeviceAuthStrategy = strategy;
	}

	protected AVSAccessTokenStrategy() {
		
	}
	
	@Override
	public void getAccessToken(AccessTokenCallback callback) {
		mAccessTokenCallback = callback;
		// first authenticate the device.
		mDeviceAuthStrategy.performDeviceAuthentication(this);
	}

	@Override
	public void onSuccess(final String deviceToken) {   
		AttributePersistenceCoordinator.getDeviceAttribute(new DeviceTokenCallback() {

			@Override
			public void onSuccess(List<DeviceAttribute> list) {
				// we now have the device token. With that, we will create an access token. 
				// the access token is a combination of the device auth token and subject token. 
				if(list.isEmpty()) {
					mAccessTokenCallback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else if(list.size() > 1) {
					mAccessTokenCallback.onError(new MIDaaSException(MIDaaSError.DEVICE_REGISTRATION_ERROR));
				} else {
					// we should have only one entry in our list. 
					// create and return the access token.
					// XXX:Future: send the device token and subject token to the server and get the access token. 
					mAccessTokenCallback.onSuccess(AccessToken.createAccessTokenFromDeviceAttribute(list.get(0), deviceToken));
				}
			}

			@Override
			public void onError(MIDaaSException exception) {
				mAccessTokenCallback.onError(exception);
			}
			
		});
	}

	@Override
	public void onError(MIDaaSException exception) {
		mAccessTokenCallback.onError(exception);
	}
}
