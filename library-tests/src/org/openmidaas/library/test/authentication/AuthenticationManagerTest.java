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
import org.openmidaas.library.authentication.AVSAccessTokenStrategy;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.authentication.Level0DeviceAuthentication;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.SubjectTokenFactory;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.test.models.MockPersistence;
import org.openmidaas.library.test.network.MockTransport;
import org.openmidaas.library.test.network.MockTransportFactory;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AuthenticationManagerTest  extends InstrumentationTestCase {
	
	private MockTransportFactory mockFactory;
	
	private Context mContext;
	
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext( getInstrumentation().getContext());	
	}

	@SmallTest
	public void testGetAccessTokenSuccess() {
		AuthenticationManager.getInstance().setAccessTokenStrategy(new MockAccessTokenSuccessStrategy());
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		Assert.assertEquals(AuthenticationTestValues.AccessToken.ACCESS_TOKEN_VALUE, token.toString());
	}
	
	@SmallTest
	public void testGetAccessTokenError() {
		AuthenticationManager.getInstance().setAccessTokenStrategy(new MockAccessTokenErrorStrategy());
		AccessToken token = AuthenticationManager.getInstance().getAccessToken();
		Assert.assertNull(token);
	}
	
	@SmallTest
	public void testGetAccessTokenUsingAVSStrategy() {
		AuthenticationManager.getInstance().setDeviceAuthenticationStrategy(new Level0DeviceAuthentication());
		AuthenticationManager.getInstance().setAccessTokenStrategy(new AVSAccessTokenStrategy());
		
		AttributePersistenceCoordinator.setPersistenceDelegate(new MockPersistence());
		mockFactory = new MockTransportFactory("access_token_success.json");
		mockFactory.setTrasport(new MockTransport(mContext));
		ConnectionManager.setNetworkFactory(mockFactory);
		SubjectToken token = SubjectTokenFactory.createAttribute();
		token.setSignedToken("header.payload.signature");
		try {
			token.save();
		} catch (MIDaaSException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		}
		AccessToken accessToken = AuthenticationManager.getInstance().getAccessToken();
		Assert.assertNotNull(accessToken);
		Assert.assertFalse(accessToken.isExpired());
		Assert.assertEquals("some_access_token", accessToken.toString());
	}
}
