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
package org.openmidaas.library.test.models;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.Level0DeviceAuthentication;
import org.openmidaas.library.authentication.core.DeviceAuthenticationCallback;
import org.openmidaas.library.model.core.MIDaaSException;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AuthenticationStrategyTest extends InstrumentationTestCase {
	
	private Context mContext;
	private boolean notificationSuccess = false;
	private String deviceToken;
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
	}
	
	@SmallTest
	public void testDeviceIDAuthenticationStrategy() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		Level0DeviceAuthentication deviceAuth = new Level0DeviceAuthentication();
		deviceAuth.performDeviceAuthentication(new DeviceAuthenticationCallback() {

			@Override
			public void onSuccess(String deviceId) {
				notificationSuccess = true;
				deviceToken = deviceId;
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				notificationSuccess = false;
				mLatch.countDown();
			}
		});
		mLatch.await();
		Assert.assertTrue(notificationSuccess);
		Assert.assertNotNull(deviceToken);
	}
	
	@SmallTest
	public void testErrorInAuthenticationCallback() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		MockErrorDeviceAuthenticationStrategy deviceAuth = new MockErrorDeviceAuthenticationStrategy();
		deviceAuth.performDeviceAuthentication(new DeviceAuthenticationCallback() {

			@Override
			public void onSuccess(String deviceId) {
				notificationSuccess = true;
				deviceToken = deviceId;
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				notificationSuccess = false;
				mLatch.countDown();
			}
		});
		mLatch.await();
		Assert.assertEquals(false, notificationSuccess);
	}
	
}
