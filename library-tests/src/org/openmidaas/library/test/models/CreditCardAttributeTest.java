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
package org.openmidaas.library.test.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.CreditCardAttribute;
import org.openmidaas.library.model.CreditCardValue;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributeDBPersistence;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class CreditCardAttributeTest extends InstrumentationTestCase {
	private final String VALID_CARD_NUMBER = "4485227712981401";
	private final String INVALID_CARD_NUMBER = "1234";
	private final short VALID_EXPIRY_MONTH = 01;
	private final short INVALID_EXPIRY_MONTH = 13;
	private final short VALID_EXPIRY_YEAR = 14;
	private final short INVALID_EXPIRY_YEAR = 02;
	private final String CARD_HOLDER_NAME = "Rob Smith";
	private final short VALID_CVV = 123;
	private final short INVALID_CVV = 1;
	private CreditCardValue mValue; 
	private CreditCardAttribute creditCardAttribute;
	protected void setUp() throws Exception {
		MIDaaS.setContext(getInstrumentation().getContext());
		AttributePersistenceCoordinator.setPersistenceDelegate(new MockPersistence());
	}
	
	@SmallTest
	public void testValidCreditCardValue() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV,VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.assertEquals(VALID_CARD_NUMBER, creditCardAttribute.getValue().getCreditCardNumber());
			Assert.assertEquals(VALID_EXPIRY_MONTH, creditCardAttribute.getValue().getExpiryMonth());
			Assert.assertEquals(VALID_EXPIRY_YEAR, creditCardAttribute.getValue().getExpiryYear());
			Assert.assertEquals(CARD_HOLDER_NAME, creditCardAttribute.getValue().getHolderName());
			Assert.assertEquals("VISA", creditCardAttribute.getValue().getCardType().toString());
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}

	@SmallTest
	public void testInvalidCreditCardNumber() {
		try {
			createAttribute(new CreditCardValue(INVALID_CARD_NUMBER, VALID_CVV,VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testInvalidMonthOnCard() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV, INVALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testInvalidYearOnCard() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV, VALID_EXPIRY_MONTH, INVALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testWithNullNumber() {
		try {
			createAttribute(new CreditCardValue(null, VALID_CVV, VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testToStringMethodOfCreditCardValue() {
		mValue = new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV,VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME);
		try {
			JSONObject object = new JSONObject(mValue.toString());
			Assert.assertEquals(VALID_CARD_NUMBER, object.getString(CreditCardValue.CARD_NUMBER));
			Assert.assertEquals(VALID_EXPIRY_MONTH, object.getInt(CreditCardValue.CARD_EXPIRY_MONTH));
			Assert.assertEquals(VALID_EXPIRY_YEAR, object.getInt(CreditCardValue.CARD_EXPIRY_YEAR));
			Assert.assertEquals(CARD_HOLDER_NAME, object.getString(CreditCardValue.CARD_HOLDER_NAME));
		} catch (JSONException e) {
			Assert.fail();
		}
		
	}
	
	@SmallTest
	public void testToStringMethodWithCVVSet() {
		try {
			
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV, VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			JSONObject object = new JSONObject(creditCardAttribute.getValue().toString());
			Assert.assertEquals(VALID_CARD_NUMBER, object.getString(CreditCardValue.CARD_NUMBER));
			Assert.assertEquals(VALID_EXPIRY_MONTH, object.getInt(CreditCardValue.CARD_EXPIRY_MONTH));
			Assert.assertEquals(VALID_EXPIRY_YEAR, object.getInt(CreditCardValue.CARD_EXPIRY_YEAR));
			Assert.assertEquals(CARD_HOLDER_NAME, object.getString(CreditCardValue.CARD_HOLDER_NAME));
			Assert.assertEquals(VALID_CVV, (short)object.getInt(CreditCardValue.CARD_CVV));
		} catch (JSONException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testInvalidCVV() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, INVALID_CVV, VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	private void createAttribute(CreditCardValue value) throws InvalidAttributeValueException, MIDaaSException {
		creditCardAttribute = AttributeFactory.getCreditCardAttributeFactory().createAttribute();
		creditCardAttribute.setValue(value);
	}
}
