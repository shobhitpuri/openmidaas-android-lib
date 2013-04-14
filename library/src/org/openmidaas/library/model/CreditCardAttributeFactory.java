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
		CreditCardAttribute attribute = new CreditCardAttribute();
		attribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributesTable._ID))));
		attribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_TOKEN)));
		attribute.setPendingData(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_PENDING)));
		try {
			JSONObject object = new JSONObject(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_VALUE)));
			CreditCardValue value = new CreditCardValue(object.getString(CreditCardValue.CARD_NUMBER), (short)object.getInt(CreditCardValue.CARD_CVV),
					(short)object.getInt(CreditCardValue.CARD_EXPIRY_MONTH),
					(short)object.getInt(CreditCardValue.CARD_EXPIRY_YEAR), object.getString(CreditCardValue.CARD_HOLDER_NAME));
			attribute.setValue(value);
		} catch (JSONException e) {
			throw new InvalidAttributeValueException();
		}
		return attribute;
	}

}
