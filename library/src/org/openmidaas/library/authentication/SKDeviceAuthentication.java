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
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import com.securekey.briidge.Briidge;
import com.securekey.briidge.Briidge.AuthenticateDeviceListener;
import com.securekey.briidge.SKBriidgeFactory;

public class SKDeviceAuthentication implements DeviceAuthenticationStrategy, AuthenticateDeviceListener {

	private final String TAG = "SKDeviceAuthentication";
	
	private DeviceAuthenticationCallback mCallback;
		
	@Override
	public void performDeviceAuthentication(
			DeviceAuthenticationCallback callback) {
		mCallback = callback;
		MIDaaS.logDebug(TAG, "Authenticating device...");
		Briidge accessPlatform;
		accessPlatform = SKBriidgeFactory.getBriidgePlatform(MIDaaS.getContext());
		accessPlatform.authenticateDevice(this);
	}
	
	@Override
	public void authenticateDeviceComplete(int status, String transactionId) {
		MIDaaS.logDebug(TAG, "Authentication response received. ");
		MIDaaS.logDebug(TAG, "Request matches expected response");
		if(status == Briidge.STATUS_OK) {
			MIDaaS.logDebug(TAG, "Response status is OK");
			if(transactionId != null)  {
				MIDaaS.logDebug(TAG, "response is not null. calling back.");
				MIDaaS.logDebug("SKDeviceAuthentication", transactionId);
				mCallback.onSuccess(transactionId);
			}
			else {
				MIDaaS.logError(TAG, "Error authenticating device. Response is null");
				mCallback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
			}
		} else {
			MIDaaS.logError(TAG, "Error authenticating device. Status is not OK");
			mCallback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		}
	}
}
