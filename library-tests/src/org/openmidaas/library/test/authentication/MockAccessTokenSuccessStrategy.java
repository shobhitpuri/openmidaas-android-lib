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

import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.authentication.core.AccessToken.AccessTokenCallback;
import org.openmidaas.library.authentication.core.AccessTokenStrategy;

public class MockAccessTokenSuccessStrategy implements AccessTokenStrategy {

	@Override
	public void getAccessToken(AccessTokenCallback callback) {
		callback.onSuccess(AccessToken.createAccessToken(AuthenticationTestValues.AccessToken.ACCESS_TOKEN_VALUE, AuthenticationTestValues.AccessToken.VALID_EXPIRY));
	}

}
