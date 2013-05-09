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

import junit.framework.Assert;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.authentication.core.AccessToken;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AuthenticationManagerTest  extends InstrumentationTestCase {
	
	protected void setUp() throws Exception {
		MIDaaS.setContext( getInstrumentation().getContext());	
	}

	@SmallTest
	public void testGetAccessTokenSuccess() {
		AuthenticationManager.getInstance().setAccessTokenStrategy(new MockAccessTokenSuccessStrategy());
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		Assert.assertEquals(TestValues.AccessToken.ACCESS_TOKEN_VALUE, token.toString());
	}
	
	@SmallTest
	public void testGetAccessTokenError() {
		AuthenticationManager.getInstance().setAccessTokenStrategy(new MockAccessTokenErrorStrategy());
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		Assert.assertNull(token);
	}
}
