package org.openmidaas.library.model;

public interface Verifiable {
	
	public void initializeVerification(InitializeVerificationCallback callback);
	
	public void completeVerification(CompleteVerificationCallback callback);

}
