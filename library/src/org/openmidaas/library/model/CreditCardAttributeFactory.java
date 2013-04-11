package org.openmidaas.library.model;

import org.openmidaas.library.model.core.AbstractAttributeFactory;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.database.Cursor;

public class CreditCardAttributeFactory implements AbstractAttributeFactory<CreditCardAttribute, CreditCardValue>{

	@Override
	public CreditCardAttribute createAttributeWithValue(CreditCardValue value)
			throws InvalidAttributeValueException, MIDaaSException {
		CreditCardAttribute attribute = new CreditCardAttribute();
		attribute.setValue(value);
		AttributePersistenceCoordinator.saveAttribute(attribute);
		return attribute;
	}

	@Override
	public CreditCardAttribute createAttributeFromCursor(Cursor cursor)
			throws InvalidAttributeValueException {
		// TODO Auto-generated method stub
		return null;
	}

}
