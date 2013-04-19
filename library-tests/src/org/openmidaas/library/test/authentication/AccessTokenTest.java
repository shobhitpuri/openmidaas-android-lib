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
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.SubjectToken;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AccessTokenTest extends InstrumentationTestCase{
	
	private final String DEVICE_ATTRIBUTE_VALUE = "DEVICE_ATTRIBUTE_VALUE";
	private final String DEVICE_AUTH_TOKEN = "DEVICE_AUTH_TOKEN";
	private AccessToken token;
	private SubjectToken deviceAttribute;
	private String expectedToken = DEVICE_ATTRIBUTE_VALUE+":"+DEVICE_AUTH_TOKEN;
	protected void setUp() throws Exception {
		MIDaaS.setContext( getInstrumentation().getContext());
		deviceAttribute = AttributeFactory.getSubjectTokenFactory().createAttribute();
		deviceAttribute.setValue(DEVICE_ATTRIBUTE_VALUE);
		deviceAttribute.setSignedToken(DEVICE_ATTRIBUTE_VALUE);
		token = AccessToken.createAccessTokenFromDeviceAttribute(deviceAttribute, DEVICE_AUTH_TOKEN);
	}
	
	@SmallTest
	public void testCreateAccessToken() {
		Assert.assertEquals(expectedToken, token.getToken());
	}
	
	@SmallTest
	public void testExpiry() {
		Assert.assertFalse(token.isExpired());
	}
}
