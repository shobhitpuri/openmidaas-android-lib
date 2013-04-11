package org.openmidaas.library.test.models;

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.AddressValue;
import org.openmidaas.library.model.AttributeFactory;
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
	
	private void createAttribute() throws InvalidAttributeValueException, MIDaaSException {
		mShippingAddressAttribute = AttributeFactory.getShippingAddressAttributeFactory().createAttributeWithValue(mValue);
	}
	

}
