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

	private DeviceAuthenticationCallback mCallback;
	
	private final String TAG = "SKDeviceAuthentication";
	
	@Override
	public void performDeviceAuthentication(
			DeviceAuthenticationCallback callback) {
		mCallback = callback;
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
