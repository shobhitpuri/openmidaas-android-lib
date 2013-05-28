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

import com.securekey.accessplatform.AccessPlatform;
import com.securekey.accessplatform.AccessPlatformException;
import com.securekey.accessplatform.AccessPlatformFactory;
import com.securekey.accessplatform.AccessPlatformListener;

public class SKDeviceAuthentication implements DeviceAuthenticationStrategy, AccessPlatformListener {

	private final String TAG = "SKDeviceAuthentication";
	
	private DeviceAuthenticationCallback mCallback;
	
	
	@Override
	public void performDeviceAuthentication(
			DeviceAuthenticationCallback callback) {
		mCallback = callback;
		MIDaaS.logDebug(TAG, "Authenticating device...");
		AccessPlatform accessPlatform;
		try {
			accessPlatform = AccessPlatformFactory.getAccessPlatform(MIDaaS.getContext());
			accessPlatform.authenticateDevice(this);
		} catch (AccessPlatformException e) {
			if(e.getMessage() != null)
				MIDaaS.logError(TAG, e.getMessage());
			else
				MIDaaS.logError(TAG, "Authentication error");
			mCallback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		}
		
	}

	@Override
	public void requestComplete(String requestType, int status, int subStatus, String transactionId) {
		MIDaaS.logDebug(TAG, "Authentication response received. ");
		if(requestType.equals(AccessPlatform.AUTHENTICATE_DEVICE_REQUEST)) {
			MIDaaS.logDebug(TAG, "Request matches expected response");
			if(status == AccessPlatform.STATUS_OK && subStatus == AccessPlatform.SUBSTATUS_NO_ERROR) {
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
		} else {
			MIDaaS.logError(TAG, "Error authenticating device. Response type does not match request type");
			mCallback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
		}
	}
}
