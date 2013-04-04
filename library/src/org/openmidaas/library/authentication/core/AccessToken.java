/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.authentication.core;

import java.util.Date;

import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.MIDaaSException;

/**
 * 
 * ADT that specifies an access token
 *
 */

public class AccessToken {
	
	public static interface AccessTokenCallback {
		
		public void onSuccess(AccessToken accessToken);
		
		public void onError(MIDaaSException exception);
	}
	
	private String mToken;
	
	private Date mExpiry;
	
	private AccessToken(String token, Date expiry){
		this.mToken = token;
		this.mExpiry = expiry;
	}
	
	public String getToken() { 
		return this.mToken;
	}
	
	public boolean isExpired() {
		Date now = new Date();
		if(this.mExpiry.compareTo(now) < 0) {
			return true;
		}
		return false;
	}
	
	public static AccessToken createAccessTokenFromDeviceAttribute(SubjectToken deviceAttribute, String deviceAuthenticationToken) {
		return (new AccessToken(deviceAttribute.getSignedToken()+":"+deviceAuthenticationToken, new Date(Long.MAX_VALUE)));
	}
	
	@Override
	public String toString() {
		return getToken();
	}
}
