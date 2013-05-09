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

import org.openmidaas.library.model.core.MIDaaSException;

/**
 * 
 * Data type for an access token
 *
 */

public class AccessToken {
	
	public static interface AccessTokenCallback {
		
		public void onSuccess(AccessToken accessToken);
		
		public void onError(MIDaaSException exception);
	}
	
	private String mToken;
	
	private long mExpiry;
	
	/**
	 * Creates a new access token object. 
	 * @param token
	 * @param expiry
	 */
	private AccessToken(String token, long expiry){
		this.mToken = token;
		this.mExpiry = expiry; 
	}
	
	public String getToken() { 
		return this.mToken;
	}
	
	public boolean isExpired() {
		long now = getNowInSeconds();
		if(this.mExpiry > now) {
			return false;
		}
		return true;
	}
	
	public static AccessToken createAccessToken(String accessToken, int expiry) {
		long now = getNowInSeconds();
		long exp = now + expiry;
		if(exp <= now) {
			return null;
		}
		if(accessToken == null || accessToken.isEmpty()) {
			return null;
		}
		return (new AccessToken(accessToken , exp));
	}
	
	@Override
	public String toString() {
		return getToken();
	}
	
	private static long getNowInSeconds() {
		Date now = new Date();
		return (now.getTime()/1000);
	}
}
