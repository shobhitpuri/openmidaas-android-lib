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
import org.openmidaas.library.model.AddressValue;
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.CreditCardValue;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.ShippingAddressAttribute;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class ShippingAddressAttributeTest extends InstrumentationTestCase {
	private final String VALID_STREET_ADDRESS = "30 St.George Street.";
	private final String VALID_LOCALITY = "Toronto";
	private final String VALID_REGION = "Ontario";
	private final String VALID_POSTAL_CODE = "M2P 2C5";
	private final String VALID_COUNTRY = "Canada";
	private AddressValue mValue;
	private ShippingAddressAttribute mShippingAddressAttribute;
	protected void setUp() throws Exception {
		MIDaaS.setContext(getInstrumentation().getContext());
		AttributePersistenceCoordinator.setPersistenceDelegate(new MockPersistence());
		mValue = new AddressValue(VALID_STREET_ADDRESS, VALID_LOCALITY, VALID_REGION, VALID_POSTAL_CODE, VALID_COUNTRY);
	}
	
	@SmallTest
	public void testValidAddress() {
		
		try {
			createAttribute();
			Assert.assertEquals(VALID_STREET_ADDRESS, mShippingAddressAttribute.getValue().getAddressLine());
			Assert.assertEquals(VALID_LOCALITY, mShippingAddressAttribute.getValue().getLocality());
			Assert.assertEquals(VALID_REGION, mShippingAddressAttribute.getValue().getRegion());
			Assert.assertEquals(VALID_POSTAL_CODE, mShippingAddressAttribute.getValue().getPostalCode());
			Assert.assertEquals(VALID_COUNTRY, mShippingAddressAttribute.getValue().getCountry());
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testEmptyStringInAddress() {
		mValue.setAddressLine("");
		try {
			createAttribute();
		} catch (InvalidAttributeValueException e) {
			
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testNullInAddress() {
		mValue.setAddressLine(null);
		try {
			createAttribute();
		} catch (InvalidAttributeValueException e) {
			
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testToStringMethodOfAddressValue() {
		try {
			JSONObject object = new JSONObject(mValue.toString());
			Assert.assertEquals(VALID_STREET_ADDRESS, object.getString(AddressValue.STREET_ADDRESS));
			Assert.assertEquals(VALID_LOCALITY, object.getString(AddressValue.LOCALITY));
			Assert.assertEquals(VALID_REGION, object.getString(AddressValue.REGION));
			Assert.assertEquals(VALID_POSTAL_CODE, object.getString(AddressValue.POSTAL_CODE));
			Assert.assertEquals(VALID_COUNTRY, object.getString(AddressValue.COUNTRY));
		} catch (JSONException e) {
			Assert.fail();
		}
		
	}
	
	private void createAttribute() throws InvalidAttributeValueException, MIDaaSException {
		mShippingAddressAttribute = AttributeFactory.getShippingAddressAttributeFactory().createAttributeWithValue(mValue);
	}
	

}
