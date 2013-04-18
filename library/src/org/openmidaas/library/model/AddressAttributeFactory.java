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
package org.openmidaas.library.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.model.core.AbstractAttributeFactory;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.persistence.AttributesTable;

import android.database.Cursor;

/**
 * 
 * Shipping address attribute factory
 *
 */
public class AddressAttributeFactory implements AbstractAttributeFactory<AddressAttribute, AddressValue>{

	@Override
	public AddressAttribute createAttributeWithValue(AddressValue value)
			throws InvalidAttributeValueException, MIDaaSException {
		AddressAttribute attribute = new AddressAttribute();
		attribute.setValue(value);
		AttributePersistenceCoordinator.saveAttribute(attribute);
		return attribute;
	}

	@Override
	public AddressAttribute createAttributeFromCursor(Cursor cursor)
			throws InvalidAttributeValueException {
		AddressAttribute attribute = new AddressAttribute();
		attribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributesTable._ID))));
		attribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_TOKEN)));
		attribute.setPendingData(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_PENDING)));
		try {
			JSONObject object = new JSONObject(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_VALUE)));
			AddressValue value = new AddressValue(object.getString(AddressValue.STREET_ADDRESS), object.getString(AddressValue.LOCALITY),
					object.getString(AddressValue.REGION), object.getString(AddressValue.POSTAL_CODE), object.getString(AddressValue.COUNTRY));
			attribute.setValue(value);
		} catch (JSONException e) {
			throw new InvalidAttributeValueException();
		}
		return attribute;
	}
}
