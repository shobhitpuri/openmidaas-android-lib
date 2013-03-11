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


import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openmidaas.library.model.EmailAttribute;
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.OpenMIDaaSException;

import android.test.suitebuilder.annotation.SmallTest;
import junit.framework.Assert;
import junit.framework.TestCase;

public class EmailAttributeTest extends TestCase{
		static EmailAttribute emailAttribute;
		private CountDownLatch mLatch = new CountDownLatch(1);
		private boolean notificationSuccess = false;
		
		public void setUp() {
			emailAttribute = new EmailAttributeFactory().createAttribute();
		}
		
		@SmallTest
		public void testIsVerifiableIsSet() {
			Assert.assertEquals(true, emailAttribute.isVerifiable());
		}
		
		@SmallTest
		public void testNullEmail(){
			try {
				emailAttribute.setValue(null);
				Assert.fail("Should have thrown InvalidAttributeValueException");
			} catch (InvalidAttributeValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@SmallTest
		public void testEmptyEmail() {
			try {
				emailAttribute.setValue("");
				Assert.fail("Should have thrown InvalidAttributeValueException");
			} catch (InvalidAttributeValueException e) {
				
			}
			
		}
		
		@SmallTest
		public void testLabel() {
			String label = "User's email";
			emailAttribute.setLabel(label);
			Assert.assertEquals(label, emailAttribute.getLabel());
		}
		
		@SmallTest
		public void testName() {
			Assert.assertEquals("emailaa", emailAttribute.getName());
		}
		
		@SmallTest
		public void testSetCorrectEmailValue () throws Exception {
			String correctEmail = "rob@gmail.com";
			emailAttribute.setValue(correctEmail);
			Assert.assertEquals(correctEmail, emailAttribute.getValue());	
		}
		
		@SmallTest
		public void testSetInvalidEmailValue() {
			String incorrectEmail = "rob@";
			try {
				emailAttribute.setValue(incorrectEmail);
				Assert.fail("Should have thrown InvalidAttributeValueException");
			} catch (InvalidAttributeValueException e) {
				
			}
		}
		
		
		@SmallTest
		public void testInitializeEmailVerification() throws Exception {
			emailAttribute.startVerification(new InitializeVerificationCallback() {

				@Override
				public void onSuccess() {
					notificationSuccess = true;
					mLatch.countDown();
					
				}

				@Override
				public void onError(OpenMIDaaSException exception) {
					notificationSuccess = false;
					mLatch.countDown();
				}
				
			});
			mLatch.await();
			Assert.assertTrue(notificationSuccess);
		}
	

}
