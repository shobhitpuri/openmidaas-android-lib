package org.openmidaas.library.authentication;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.authentication.core.DeviceAuthenticationStrategy;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import com.securekey.accessplatform.AccessPlatform;
import com.securekey.accessplatform.AccessPlatformFactory;
import com.securekey.accessplatform.AccessPlatformListener;

public class SKDeviceAuthentication implements DeviceAuthenticationStrategy, AccessPlatformListener {

	private DeviceAuthenticationCallback mCallback;
	
	@Override
	public void performDeviceAuthentication(
			DeviceAuthenticationCallback callback) {
		mCallback = callback;
		AccessPlatform accessPlatform = AccessPlatformFactory.getAccessPlatform(MIDaaS.getContext());
		accessPlatform.authenticateDevice(this);
	}

	@Override
	public void requestComplete(String requestType, int status, int subStatus, String transactionId) {
		if(requestType.equals(AccessPlatform.AUTHENTICATE_DEVICE_REQUEST)) {
			if(status == AccessPlatform.STATUS_OK && subStatus == AccessPlatform.SUBSTATUS_NO_ERROR) {
				if(transactionId != null) 
					mCallback.onSuccess(transactionId);
				else 
					mCallback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
			} else {
				mCallback.onError(new MIDaaSException(MIDaaSError.ERROR_AUTHENTICATING_DEVICE));
			}
		}
	}
}
