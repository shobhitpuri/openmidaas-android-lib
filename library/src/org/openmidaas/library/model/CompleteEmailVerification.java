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
import org.json.JSONObject;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.CompleteAttributeVerificationDelegate;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;

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
	public void completeVerification(AbstractAttribute<?> attribute, String code, final CompleteVerificationCallback callback) {
		JSONObject postData = new JSONObject();
		try {
			postData.put("attribute", attribute.getAttributeAsJSONObject());
			postData.put("code", code);
		} catch (JSONException e1) {
			callback.onError(null);
		}
		ConnectionManager.getInstance().postRequest(Constants.COMPLETE_AUTH_URL, postData, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(String response) {
				//TODO: persist the response before sending the callback success here. 
				callback.onSuccess();
			}
			
			@Override
			public void onFailure(Throwable e, String response){
				callback.onError(new MIDaaSException(MIDaaSError.SERVER_ERROR));
			}
		});
	}

}
