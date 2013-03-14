package org.openmidaas.library.model.core;

public interface AuthenticationCallback{
	
	public <T> void onSuccess(T deviceId);
	
	public void onError(MIDaaSException exception);

}
