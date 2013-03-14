package org.openmidaas.library.model.core;

import org.openmidaas.library.MIDaaS;

import android.content.Context;
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
