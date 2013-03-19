package org.openmidaas.library.test.models;

import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.core.DeviceIdAuthentication;
import org.openmidaas.library.model.core.DeviceRegistration;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.test.network.MockTransportFactory;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class DeviceRegistrationTest extends InstrumentationTestCase{
	
	private MockTransportFactory mockFactory;
	private Context mContext;
	private boolean notificationSuccess = false;
	
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		mockFactory = new MockTransportFactory(mContext, "device_reg_success.json");
		MIDaaS.setContext(mContext);
		ConnectionManager.setNetworkFactory(mockFactory);
	}
	
	@SmallTest
	public void testRegistrationSuccess() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		
		DeviceRegistration deviceRegistration = new DeviceRegistration(new DeviceIdAuthentication());
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
		});
		
		mLatch.await();
		Assert.assertTrue(notificationSuccess);
	}
	
	@SmallTest
	public void testRegistrationFailure() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		mockFactory.setFilename("device_reg_fail.json");
		DeviceRegistration deviceRegistration = new DeviceRegistration(new DeviceIdAuthentication());
		deviceRegistration.registerDevice(new InitializationCallback() {

			

			@Override
			public void onError(MIDaaSException exception) {
				notificationSuccess = false;
				mLatch.countDown();
			}

			@Override
			public void onSuccess() {
				notificationSuccess = true;
				mLatch.countDown();
			}	
		});
		
		mLatch.await();
		Assert.assertEquals(false, notificationSuccess);
	}

}
