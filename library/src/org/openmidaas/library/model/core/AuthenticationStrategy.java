package org.openmidaas.library.model.core;

import android.content.Context;

public interface AuthenticationStrategy {
	
	public void performAuthentication(AuthenticationCallback callback);
	
}
