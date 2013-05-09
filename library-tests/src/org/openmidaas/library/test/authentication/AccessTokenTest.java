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

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.core.AccessToken;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AccessTokenTest extends InstrumentationTestCase{
	
	private AccessToken token;
	
	protected void setUp() throws Exception {
		MIDaaS.setContext( getInstrumentation().getContext());
		token = AccessToken.createAccessToken(TestValues.AccessToken.ACCESS_TOKEN_VALUE, TestValues.AccessToken.VALID_EXPIRY);
	}
	
	@SmallTest
	public void testCreateAccessToken() {
		
		Assert.assertNotNull(token);
		Assert.assertFalse(token.isExpired());
		Assert.assertEquals(TestValues.AccessToken.ACCESS_TOKEN_VALUE, token.toString());
	}
	
	@SmallTest
	public void testExpiry() {
		Assert.assertFalse(token.isExpired());
	}
	
	@SmallTest
	public void testInvalidExpiresIn() {
		token = AccessToken.createAccessToken(TestValues.AccessToken.ACCESS_TOKEN_VALUE, TestValues.AccessToken.INVALID_EXPIRY);
		Assert.assertNull(token);
	}
	
	@SmallTest
	public void testValidExpiresInWithExpiryCheck() {
		// sets the expiresIn to 2 seconds, creates the access token, waits for 3 seconds and makes sure that the token has expired
		token = AccessToken.createAccessToken(TestValues.AccessToken.ACCESS_TOKEN_VALUE, TestValues.AccessToken.SHORT_EXPIRY);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Assert.fail();
		}
		Assert.assertTrue(token.isExpired());
	}
}
