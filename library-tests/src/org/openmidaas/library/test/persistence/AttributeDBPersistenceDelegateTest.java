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
package org.openmidaas.library.test.persistence;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.CreditCardAttribute;
import org.openmidaas.library.model.CreditCardAttributeFactory;
import org.openmidaas.library.model.CreditCardValue;
import org.openmidaas.library.model.EmailAttribute;
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeNameException;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributeDBPersistence;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.AttributeDataCallback;
import org.openmidaas.library.persistence.core.CreditCardDataCallback;
import org.openmidaas.library.persistence.core.GenericDataCallback;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests various persistence methods. 
 * Uses the AttributePersistenceCoordinator to perform the tests. 
 * The AttributePersistenceCoordinator has the delegate to 
 * persist the data, i.e., the AttributeDBPersistenceDelegate. 
 */
public class AttributeDBPersistenceDelegateTest extends InstrumentationTestCase {
	private static final String TEST_NAME 	 = "test";
	private static final String TEST_VALUE_1 = "TEST_VALUE_1";
	private static final String TEST_VALUE_2 = "TEST_VALUE_2";
	private static final String TEST_VALUE_3 = "TEST_VALUE_3";
	private boolean mNotification = false;
	private Context mContext;
	// create a generic attribute of type "test"

	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistence());
	}


	

	@SmallTest
	public void testDelete() throws Exception {
		//mContext.deleteDatabase("attributes.db");
		GenericAttribute a1 = GenericAttributeFactory.createAttribute(TEST_NAME);
		a1.setValue("a1");
		mNotification = false;
		if(AttributePersistenceCoordinator.removeAttribute(a1)) {
		} else {
			Assert.fail();
		}
	}

	@SmallTest
	public void testSaveAndRetrieval() throws Exception {
		// store the following values that are of type "test"
		GenericAttribute a1 = GenericAttributeFactory.createAttribute(TEST_NAME);
		a1.setValue(TEST_VALUE_1);
		a1.save();
		GenericAttribute a2 = GenericAttributeFactory.createAttribute(TEST_NAME);
		a2.setValue(TEST_VALUE_2);
		a2.save();
		GenericAttribute a3 = GenericAttributeFactory.createAttribute(TEST_NAME);
		a3.setValue(TEST_VALUE_3);
		a3.save();
		final CountDownLatch mLatch = new CountDownLatch(1);
		// Retrieve all the "test" attributes
		AttributePersistenceCoordinator.getGenericAttributes("test", new GenericDataCallback() {

			@Override
			public void onSuccess(List<GenericAttribute> list) {
				for(GenericAttribute a: list) {
					if((a.getValue().equalsIgnoreCase(TEST_VALUE_1)) || (a.getValue().equalsIgnoreCase(TEST_VALUE_2)) || (a.getValue().equalsIgnoreCase(TEST_VALUE_3))) {
						continue;
					} else {
						Assert.fail();
						
					}
				}
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				Assert.fail();
			}
		});
		mLatch.await();
	}
	
	@SmallTest
	public void testGetAllAttributes() {
		mContext.deleteDatabase("attributes.db");
		GenericAttribute a1;
		try {
			final CountDownLatch mLatch = new CountDownLatch(1);
			a1 = GenericAttributeFactory.createAttribute(TEST_NAME);
			a1.setValue(TEST_VALUE_1);
			a1.save();
			GenericAttribute a2 = GenericAttributeFactory.createAttribute(TEST_NAME);
			a2.setValue(TEST_VALUE_2);
			a2.save();
			EmailAttribute email = EmailAttributeFactory.createAttribute();
			email.setValue("rob@gmail.com");
			email.save();
			CreditCardAttribute cc = CreditCardAttributeFactory.createAttribute();
			cc.setValue(new CreditCardValue("4485227712981401", "123", "01", "15", "Rob Smith"));
			cc.save();
			AttributePersistenceCoordinator.getAllAttributes(new AttributeDataCallback() {

				@Override
				public void onSuccess(List<AbstractAttribute<?>> list) {
					mNotification = true;
					mLatch.countDown();
				}

				@Override
				public void onError(MIDaaSException exception) {
					mNotification = false;
					mLatch.countDown();
				}
				
			});
			try {
				mLatch.await();
				if(!mNotification) {
					Assert.fail();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InvalidAttributeNameException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
		
		
	}

	@SmallTest
	public void testComplexValueSaveAndRetrieval() {
		mContext.deleteDatabase("attributes.db");
		final CountDownLatch mLatch = new CountDownLatch(1);
		final String cardNumber = "4485227712981401";
		final String cvv = "123";
		final String expiryMonth = "01";
		final String expiryYear = "15";
		final String name = "Rob Smith";
		mNotification = false;
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistence());
		try {
			CreditCardValue mValue = new CreditCardValue(cardNumber, cvv, expiryMonth, expiryYear, name);
			CreditCardAttribute cc = CreditCardAttributeFactory.createAttribute();
			cc.setValue(mValue);
			AttributePersistenceCoordinator.saveAttribute(cc);
			
			AttributePersistenceCoordinator.getCreditCardAttributes(new CreditCardDataCallback() {

				@Override
				public void onSuccess(List<CreditCardAttribute> list) {
					for(CreditCardAttribute cc: list) {
						if(cc.getValue().getCreditCardNumber().equalsIgnoreCase(cardNumber) && cc.getValue().getCVV().equals(cvv)
								&& cc.getValue().getExpiryMonth().equals(expiryMonth) && cc.getValue().getExpiryYear().equals(expiryYear)
								&& cc.getValue().getHolderName().equalsIgnoreCase(name)) {
							mNotification = true;
						}
					}
					mLatch.countDown();				
				}

				@Override
				public void onError(MIDaaSException exception) {
					mNotification = false;
					mLatch.countDown();
				}
				
			});
			try {
				mLatch.await();
				if(!mNotification) {
					Assert.fail();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MIDaaSException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		}	
	}
	
	protected void tearDown() throws Exception {
		mContext.deleteDatabase("attributes.db");
	}
}
