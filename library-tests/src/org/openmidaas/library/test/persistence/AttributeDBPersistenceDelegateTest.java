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
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.CreditCardValue;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributeDBPersistence;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.core.GenericDataCallback;
import org.openmidaas.library.test.models.MockPersistence;

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
	private static final String SIGNED_TOKEN = "123456";
	private boolean mNotification = false;
	private Context mContext;
	// create a generic attribute of type "test"
	
	private GenericAttributeFactory factory = AttributeFactory.getGenericAttributeFactory();
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistence());
	}
	
	
	@SmallTest
	public void testUpdate() throws Exception {
		mContext.deleteDatabase("attributes.db");
		final CountDownLatch mLatch = new CountDownLatch(1);
		GenericAttribute a1 = factory.createAttribute(TEST_NAME, "a1");
		a1.setSignedToken(SIGNED_TOKEN);
		mNotification = false;
		if(AttributePersistenceCoordinator.saveAttribute(a1)) {
				// get all attributes of type test - there will be only 1 under the current test condition
			AttributePersistenceCoordinator.getGenericAttributes("test", new GenericDataCallback() {

				@Override
				public void onSuccess(List<GenericAttribute> list) {
					for(GenericAttribute a: list) {
						if(a.getSignedToken().equalsIgnoreCase(SIGNED_TOKEN)) {
							mNotification = true;
							mLatch.countDown();
						} else {
							mNotification = false;
							mLatch.countDown();
						}
					}
				}

				@Override
				public void onError(MIDaaSException exception) {
					Assert.fail();
				}
			});
			mLatch.await();
			Assert.assertTrue(mNotification);
		} else {
			Assert.fail();
		}
		
	}
	
	@SmallTest
	public void testDelete() throws Exception {
		//mContext.deleteDatabase("attributes.db");
		GenericAttribute a1 = factory.createAttribute(TEST_NAME, "a1");
		final CountDownLatch mLatch = new CountDownLatch(1);
		mNotification = false;
		if(AttributePersistenceCoordinator.removeAttribute(a1)) {
		} else {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testSaveAndRetrieval() throws Exception {
		// store the following values that are of type "test"
		factory.setAttributeName(TEST_NAME);
		GenericAttribute a1 = factory.createAttributeWithValue("TEST_VALUE_1");
		GenericAttribute a2 = factory.createAttributeWithValue("TEST_VALUE_2");
		GenericAttribute a3 = factory.createAttributeWithValue("TEST_VALUE_3");
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
				
			}
		});
		mLatch.await();
	}
	
	@SmallTest
	public void testComplexValueSaveAndRetrieval() {
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistence());
		try {
			CreditCardValue mValue = new CreditCardValue("4485227712981401", (short)01, (short)15, "Rob Smith");
			AttributePersistenceCoordinator.saveAttribute(AttributeFactory.getCreditCardAttributeFactory().createAttributeWithValue(mValue));
		} catch (MIDaaSException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		}	
	}
}
