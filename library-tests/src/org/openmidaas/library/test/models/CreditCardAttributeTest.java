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
import org.openmidaas.library.model.CreditCardAttribute;
import org.openmidaas.library.model.CreditCardAttributeFactory;
import org.openmidaas.library.model.CreditCardValue;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class CreditCardAttributeTest extends InstrumentationTestCase {
	private final String VALID_CARD_NUMBER = "4485227712981401";
	private final String INVALID_CARD_NUMBER = "1234";
	private final String VALID_EXPIRY_MONTH = "01";
	private final String INVALID_EXPIRY_MONTH = "13";
	private final String VALID_EXPIRY_YEAR = "14";
	private final String INVALID_EXPIRY_YEAR = "10";
	private final String CARD_HOLDER_NAME = "Rob Smith";
	private final String VALID_CVV = "123";
	private final String expectedValidCreditCardToString = VALID_CARD_NUMBER + "\n" + VALID_EXPIRY_MONTH + "/" + VALID_EXPIRY_YEAR + "\n" + CARD_HOLDER_NAME;		
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
			Assert.assertEquals(VALID_CVV, creditCardAttribute.getValue().getCVV());
			Assert.assertEquals("Visa", creditCardAttribute.getValue().getCardType().toString());
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testCharactersInCreditCardValue() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, "abc",VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
		
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testEmptyToStringCallAfterAttributeIsCreated() {
		CreditCardAttribute ccAttribute = CreditCardAttributeFactory.createAttribute();
		Assert.assertEquals("", ccAttribute.toString());
	}
	
	@SmallTest
	public void testToStringOfCreditCardAttributeClass() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV,VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.assertEquals(expectedValidCreditCardToString, creditCardAttribute.toString());
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testNoCVVSet() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, "",VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			Assert.assertEquals(VALID_CARD_NUMBER, creditCardAttribute.getValue().getCreditCardNumber());
			Assert.assertEquals(VALID_EXPIRY_MONTH, creditCardAttribute.getValue().getExpiryMonth());
			Assert.assertEquals(VALID_EXPIRY_YEAR, creditCardAttribute.getValue().getExpiryYear());
			Assert.assertEquals(CARD_HOLDER_NAME, creditCardAttribute.getValue().getHolderName());
			Assert.assertEquals("", creditCardAttribute.getValue().getCVV());
			Assert.assertEquals("Visa", creditCardAttribute.getValue().getCardType().toString());
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testInvalidValueSetAndSave() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV,VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			creditCardAttribute.getValue().setCardNumber(null);
			creditCardAttribute.save();
			Assert.fail("Expected InvalidAttributeValueException");
		} catch (InvalidAttributeValueException e) {
			
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
	public void testNotNullGetAttributeAsJSONObject() {
		try {
			createAttribute(new CreditCardValue(VALID_CARD_NUMBER, VALID_CVV, VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR, CARD_HOLDER_NAME));
			JSONObject object = new JSONObject(creditCardAttribute.getValue().toString());
			Assert.assertNotNull(object);
			Assert.assertEquals(VALID_CARD_NUMBER, object.getString(CreditCardValue.CARD_NUMBER));
			Assert.assertEquals(VALID_CVV, object.getString(CreditCardValue.CARD_CVV));
			Assert.assertEquals(VALID_EXPIRY_MONTH, object.getString(CreditCardValue.CARD_EXPIRY_MONTH));
			Assert.assertEquals(VALID_EXPIRY_YEAR, object.getString(CreditCardValue.CARD_EXPIRY_YEAR));
			Assert.assertEquals(CARD_HOLDER_NAME, object.getString(CreditCardValue.CARD_HOLDER_NAME));
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		} catch (JSONException e) {
			Assert.fail();
		}
	}
	
	
	private void createAttribute(CreditCardValue value) throws InvalidAttributeValueException, MIDaaSException {
		creditCardAttribute = CreditCardAttributeFactory.createAttribute();
		creditCardAttribute.setValue(value);
	}
}
