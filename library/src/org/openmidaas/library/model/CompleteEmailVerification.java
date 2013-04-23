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
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.CompleteAttributeVerificationDelegate;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import com.loopj.android.http.AsyncHttpResponseHandler;


/**
 * This class implements the delegate class that completes the 
 * attribute verification process. It sends the one-time code 
 * collected via a GUI to the server for verification. 
 * The result is returned via a callback.
 */
public class CompleteEmailVerification implements CompleteAttributeVerificationDelegate{

	/**
	 * This methods is an implementation of the interface that 
	 * completes an attribute verification. 
	 * Here the one-time code is send to the server and the result
	 * is send back to the caller via a callback.
	 */
	@Override
	public void completeVerification(final AbstractAttribute<?> attribute, final String code, final CompleteVerificationCallback completeVerificationCallback) {
		try {
			AVSServer.completeAttributeVerification(attribute, code, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(String response) {
					attribute.setSignedToken(response);
					attribute.setPendingData(null);
					
					try {
						if(AttributePersistenceCoordinator.saveAttribute(attribute)) {
							completeVerificationCallback.onSuccess();
						} else {
							completeVerificationCallback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
						}
						
					} catch (MIDaaSException e) {
						completeVerificationCallback.onError(e);
					}
				}
				
				@Override
				public void onFailure(Throwable e, String response){
					completeVerificationCallback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
				}
			});
		} catch (JSONException e1) {
			completeVerificationCallback.onError(null);
		}
	}
}
