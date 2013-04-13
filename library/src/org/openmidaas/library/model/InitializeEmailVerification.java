/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.model;

import org.json.JSONException;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.InitializeAttributeVerificationDelegate;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Class that implements the delegate that initializes
 * attribute verification. 
 */
public class InitializeEmailVerification implements InitializeAttributeVerificationDelegate{

	private final String TAG = "InitializeEmailVerification";
	
	/**
	 * The method calls the server with the provided attribute 
	 * value (email address) and returns the result via a callback to 
	 * the caller. 
	 */
	@Override
	public void startVerification(final AbstractAttribute<?> attribute,
			final InitializeVerificationCallback initVerificationCallback) {
		try {
			AVSServer.startAttributeVerification(attribute, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(String response) { 
					if((!(response.isEmpty())) ) {
						attribute.setPendingData(response);
						// it is important that we guarantee that the data is persisted before we return. 
						try {
							if(AttributePersistenceCoordinator.saveAttribute(attribute)) {
								MIDaaS.logDebug(TAG, "done init email verification.");
								initVerificationCallback.onSuccess();
							} else {
								initVerificationCallback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
							}
						} catch (MIDaaSException e) {
							initVerificationCallback.onError(e);
						}
						
					} else {
						initVerificationCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
					}
				}
				
				@Override
				public void onFailure(Throwable e, String response){
					initVerificationCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				}
			});
			
		} catch (JSONException e) {
			initVerificationCallback.onError(null);
		}
	}
}
