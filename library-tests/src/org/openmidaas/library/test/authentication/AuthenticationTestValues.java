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
package org.openmidaas.library.test.authentication;

public final class AuthenticationTestValues {

	public static final class AccessToken {
		public static final String ACCESS_TOKEN_VALUE = "some_token_value";
		public static final int VALID_EXPIRY = 600;
		public static final int INVALID_EXPIRY = 0;
		public static final int SHORT_EXPIRY = 2;
	}
	
}
