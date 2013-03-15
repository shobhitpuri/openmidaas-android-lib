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
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.AuthenticationCallback;
import org.openmidaas.library.model.core.InitializeAttributeVerificationDelegate;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSException;

/**
 * Class that implements the delegate that initializes
 * attribute verification. 
 */
public class InitializeEmailVerification implements InitializeAttributeVerificationDelegate, AuthenticationCallback {

	
	/**
	 * The method calls the server with the provided attribute 
	 * value (email address) and returns the result via a callback to 
	 * the caller. 
	 */
	@Override
	public void startVerification(AbstractAttribute<?> attribute,
			final InitializeVerificationCallback callback) {
		attribute.performAuthentication(this);
		JSONObject postData = new JSONObject();
		try {
			postData.put("attribute", attribute.getAttributeAsJSONObject());
			postData.put("deviceToken", "some token");
		} catch (JSONException e) {
			callback.onError(null);
		}
	}

	@Override
	public void onSuccess(String deviceId) {
		
		
	}

	@Override
	public void onError(MIDaaSException exception) {
		
		
	}

	
}
