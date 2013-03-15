package org.openmidaas.library.model.core;

public interface AuthenticationCallback{
	
	public void onSuccess(String deviceId);
	
	public void onError(MIDaaSException exception);

}
