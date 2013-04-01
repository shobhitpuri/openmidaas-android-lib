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

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.AVSAccessTokenStrategy;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.authentication.Level0DeviceAuthentication;
import org.openmidaas.library.authentication.core.AccessToken;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.core.DeviceRegistration;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributeDBPersistenceDelegate;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.test.network.MockTransportFactory;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

public class AuthenticationManagerTest extends InstrumentationTestCase{
	private MockTransportFactory mockFactory;
	private Context mContext;
	private boolean notificationSuccess = false;
	private String deviceToken;
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		mockFactory = new MockTransportFactory(mContext, "device_reg_success.json");
		ConnectionManager.setNetworkFactory(mockFactory);
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistenceDelegate());
		mContext.deleteDatabase("attributes.db");
	}

	@SmallTest
	public void testGetAccessToken() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		// first register the device. 
		DeviceRegistration deviceRegistration = new DeviceRegistration(new Level0DeviceAuthentication());
		deviceRegistration.registerDevice(new InitializationCallback() {

			@Override
			public void onSuccess() {
				notificationSuccess = true;
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				notificationSuccess = false;
				mLatch.countDown();
			}

			@Override
			public void onRegistering() {
				
			}	
		});
		mLatch.await();
		if(notificationSuccess) {
			AuthenticationManager.getInstance().setAccessTokenStrategy(new AVSAccessTokenStrategy(new Level0DeviceAuthentication()));
			AccessToken token = AuthenticationManager.getInstance().getAccessToken();
			Assert.assertNotNull(token);
		} else {
			Assert.fail();
		}
		
	}
	
}
