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

import junit.framework.Assert;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.PhoneAttribute;
import org.openmidaas.library.model.PhoneAttribute.VERIFICATION_METHOD;
import org.openmidaas.library.model.PhoneAttributeFactory;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.test.authentication.MockAccessTokenSuccessStrategy;
import org.openmidaas.library.test.network.MockTransport;
import org.openmidaas.library.test.network.MockTransportFactory;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

public class PhoneAttributeTest extends InstrumentationTestCase {
	private final String TAG = "TestingPhoneAttribute";
	
	static PhoneAttribute phoneAttribute;
	private boolean notificationSuccess = false;
	private Context mContext;
	static boolean isInit = false;
	private MockTransportFactory mockFactory;
	private String validPhoneNumber = "+14168189191";
		
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		// set the persistence delegate to a simple list. database doesn't seem to work after deletion. 
		AttributePersistenceCoordinator.setPersistenceDelegate(new MockPersistence());
		AuthenticationManager.getInstance().setAccessTokenStrategy(new MockAccessTokenSuccessStrategy());
		phoneAttribute = PhoneAttributeFactory.createAttribute();
		phoneAttribute.setValue(validPhoneNumber);

		mockFactory = new MockTransportFactory("init_email_ver_success.json");
		mockFactory.setTrasport(new MockTransport(mContext));
		ConnectionManager.setNetworkFactory(mockFactory);
		isInit = true;
	}
	
	
	@SmallTest
	public void testSetPendingData() {
		try {
			phoneAttribute.setPendingData("testPendingData");
		} catch(Exception e) {
			Assert.fail();
		}
		
	}
	
	@SmallTest
	public void testGetPendingData() {
		String data = "testPendingData";
		try {
			phoneAttribute.setPendingData(data);
			Assert.assertEquals(data, phoneAttribute.getPendingData());
		} catch(Exception e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testNullPhoneNumber(){
		try {
			MIDaaS.logDebug(TAG, "test setting Null value");
			phoneAttribute.setValue(null);
			Assert.fail("Should have thrown InvalidAttributeValueException");
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SmallTest
	public void testNameAttribute() {
		Assert.assertEquals("phone_number", phoneAttribute.getName());
	}
	
	@SmallTest
	public void testLabel() {
		String label = "User's Phone Number";
		phoneAttribute.setLabel(label);
		Assert.assertEquals(label, phoneAttribute.getLabel());
	}
	
	@SmallTest
	public void testMethodForVerification() {
		String method = "sms";
		phoneAttribute.setVerificationMethod(VERIFICATION_METHOD.sms.toString());
		Assert.assertEquals(method, phoneAttribute.getVerificationMethod());
	}
		
	@SmallTest
	public void testEmptyPhoneNumber() {
		try {
			phoneAttribute.setValue("");
			Assert.fail("Should have thrown InvalidAttributeValueException");
		} catch (InvalidAttributeValueException e) {
			
		}
		
	}
	
	@SmallTest
	public void testSetInvalidPhoneNumbers() {
		String [] incorrectPhoneNumber = { "+4168361118", "+A (403) 235 2323" , "416-836-4111","(416) 836-4111", "0014168361111", "+1 111 222 1111","+1 (403) 235-READ"};
		for ( int i = 0; i < incorrectPhoneNumber.length; i++){
			try {
				MIDaaS.logDebug(TAG, "Testing " + incorrectPhoneNumber[i]);
				phoneAttribute.setValue(incorrectPhoneNumber[i]);
				Assert.fail("Should have thrown InvalidAttributeValueException");
			} catch (InvalidAttributeValueException e) {
				
			}
		}
	}

	@SmallTest
	public void testSetValidWrongFormatPhoneNumbers() {
		String [] correctPhoneNumber = { "+1-416-836-1111", "+91(581) 2559999", "  +61 (08) 9650-5000", "+1.416.836.1111", "+1 (416) 836 4118" ,"+1/234/567/8901"};
		for ( int i = 0; i < correctPhoneNumber.length; i++){
			try {
				MIDaaS.logDebug(TAG, "Testing " + correctPhoneNumber[i]);
				phoneAttribute.setValue(correctPhoneNumber[i]);
				Assert.fail("Throws invalid attribute exception as though the number is valid but not in E164 format. Value can only be set if in standard E-164 format.");
			} catch (InvalidAttributeValueException e) {
				
			}
		}
	}
	
	@SmallTest
	public void testSetValidCorrectFormatPhoneNumbers() {
		String [] correctPhoneNumber = { "+14168361111", "+919457011377" };
		for ( int i = 0; i < correctPhoneNumber.length; i++){
			try {
				MIDaaS.logDebug(TAG, "Testing " + correctPhoneNumber[i]);
				phoneAttribute.setValue(correctPhoneNumber[i]);
				MIDaaS.logDebug(TAG, "E164 converted is: "+phoneAttribute.toString());
				Assert.assertEquals(correctPhoneNumber[i], phoneAttribute.getValue());
			} catch (InvalidAttributeValueException e) {
				Assert.fail();
			}
		}
	}
	
	@SmallTest
	public void testSetPossibleButInvalidPhoneNumber() {
		String sample = "+1 999 222 9999";
		try {
			MIDaaS.logDebug(TAG, "Testing " + sample+" which is a possible but invalid Phone Number");
			phoneAttribute.setValue(sample);
			Assert.fail("Should have thrown InvalidAttributeValueException");
		} catch (InvalidAttributeValueException e) {
		}
	}
	
	@MediumTest
	public void testPhoneVerification() throws Exception {
		mContext = getInstrumentation().getContext();
		
		initializePhoneVerificationSuccess();
		completePhoneVerificationSuccess();	
	}
	
	private void initializePhoneVerificationSuccess() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		mockFactory.setFilename("init_email_ver_success.json");
		phoneAttribute.startVerification(new InitializeVerificationCallback() {

			@Override
			public void onSuccess() {
				notificationSuccess = true;
				MIDaaS.logDebug(TAG, "Successful phone number initialization" );
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				notificationSuccess = false;
				MIDaaS.logDebug(TAG, "Error in phone number initialization" );
				mLatch.countDown();
			}
			
		});
		mLatch.await();
		Assert.assertTrue(notificationSuccess);
	}
	
	private void completePhoneVerificationSuccess() throws Exception {
		final CountDownLatch mLatch = new CountDownLatch(1);
		MIDaaS.setContext(mContext);
		mockFactory.setFilename("complete_email_ver_success.json");
		notificationSuccess = false;
		phoneAttribute.completeVerification("1234", new CompleteVerificationCallback() {

			@Override
			public void onSuccess() {
				notificationSuccess = true;
				MIDaaS.logDebug(TAG, "Successful in completing phone number verification" );
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				notificationSuccess = false;
				MIDaaS.logDebug(TAG, "Error in completing phone number verifcation" );
				mLatch.countDown();
			}
			
		});
		mLatch.await();
		Assert.assertTrue(notificationSuccess);
	}
	
}
