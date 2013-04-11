package org.openmidaas.library.model;

import org.openmidaas.library.common.Constants;
import org.openmidaas.library.model.core.AbstractAttribute;

public class ShippingAddressAttribute extends AbstractAttribute<AddressValue> {
	
	protected ShippingAddressAttribute() {
		mName = Constants.RESERVED_WORDS.SHIPPING_ADDRESS;
	}

	@Override
	protected boolean validateAttribute(AddressValue value) {
		if(value == null) {
			return false;
		}
		if(value.getAddressLine() == null || value.getAddressLine().isEmpty()) {
			return false;
		}
		if(value.getLocality() == null || value.getLocality().isEmpty()) {
			return false;
		}
		if(value.getRegion() == null || value.getRegion().isEmpty()) {
			return false;
		}
		if(value.getPostalCode() == null || value.getPostalCode().isEmpty()) {
			return false;
		}
		if(value.getCountry() == null || value.getCountry().isEmpty()) {
			return false;
		}
		return true;
	}
}
