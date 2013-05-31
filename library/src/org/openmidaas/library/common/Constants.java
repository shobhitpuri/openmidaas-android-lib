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
package org.openmidaas.library.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Public constants used by the library. 
 *
 */
public final class Constants {
	private Constants(){}
	private static final String SERVER_API_VERSION = "/1";
	public static final String AVP_SB_BASE_URL = "https://midaas-avp.securekeylabs.com";
	public static final String AVP_LIVE_BASE_URL = "http://avplive-securekey.dotcloud.com";
	public static final String INIT_AUTH_URL = SERVER_API_VERSION + "/requestAttributeVerification";
	public static final String COMPLETE_AUTH_URL = SERVER_API_VERSION + "/completeAttributeVerification";
	public static final String REGISTRATION_URL = SERVER_API_VERSION + "/device/register";
	public static final String BUNDLE_ATTRIBUTES_URL = SERVER_API_VERSION + "/bundleVerifiedAttributes";
	public static final String TOKEN_URL = SERVER_API_VERSION + "/authenticate";
	public static final Object APP_ISSUER_ID = "org.openmidaas.library";
	public static final class RequestKeys {
		public static final String CLIENT_ID = "client_id";
		public static final String STATE = "state";
	}
	public static final class AttributeBundleKeys {
		public static final String ISSUER = "iss";
		public static final String AUDIENCE = "aud";
		public static final String ISSUED_AT = "iat";
		public static final String ATTRIBUTES = "attrs";
	}
	public static final class AccessTokenKeys {
		public static final String ACCESS_TOKEN = "accessToken";
		public static final String EXPIRES_IN = "expiresIn";
	}
	public static final class AVSServerJSONKeys {
		public static final String DEVICE_TOKEN = "deviceToken";
		public static final String TYPE = "type";
		public static final String VALUE = "value";
		public static final String VERIFICATION_METHOD = "method";
		public static final String CODE = "code";
		public static final String VERIFICATION_TOKEN = "verificationToken";
	}

	public enum ATTRIBUTE_STATE { VERIFIED, PENDING_VERIFICATION, NOT_VERIFIABLE, NOT_VERIFIED, ERROR_IN_SAVE, UNKNOWN };

	public enum RESERVED_WORDS { email, credit_card, address, subject_token, phone_number }
	private static List<String> mReservedWordsAsString = new ArrayList<String>();
	static {
		mReservedWordsAsString.clear();
		for(RESERVED_WORDS word:RESERVED_WORDS.values()) {
			mReservedWordsAsString.add(word.toString());
		}
	}
	public static List<String> getReservedWordsAsList() {
		return mReservedWordsAsString;
	}
}
