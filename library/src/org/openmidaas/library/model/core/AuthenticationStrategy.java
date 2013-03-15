package org.openmidaas.library.model.core;

public interface AuthenticationStrategy {
	
	public void performAuthentication(AuthenticationCallback callback);
	
}
