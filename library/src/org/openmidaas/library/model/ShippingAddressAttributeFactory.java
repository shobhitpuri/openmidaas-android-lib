package org.openmidaas.library.model;

import org.openmidaas.library.model.core.AbstractAttributeFactory;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.database.Cursor;

public class ShippingAddressAttributeFactory implements AbstractAttributeFactory<ShippingAddressAttribute, AddressValue>{

	@Override
	public ShippingAddressAttribute createAttributeWithValue(AddressValue value)
			throws InvalidAttributeValueException, MIDaaSException {
		ShippingAddressAttribute attribute = new ShippingAddressAttribute();
		attribute.setValue(value);
		AttributePersistenceCoordinator.saveAttribute(attribute);
		return attribute;
	}

	@Override
	public ShippingAddressAttribute createAttributeFromCursor(Cursor cursor)
			throws InvalidAttributeValueException {
		// TODO Auto-generated method stub
		return null;
	}

}
