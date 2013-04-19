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
package org.openmidaas.library.persistence;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.common.Constants;

import org.openmidaas.library.model.AddressValue;
import org.openmidaas.library.model.InvalidAttributeNameException;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.AddressAttribute;
import org.openmidaas.library.model.AddressAttributeFactory;

import android.database.Cursor;

public class AddressDBBuilder extends AbstractAttributeDBBuilder<AddressAttribute>{

	@Override
	protected AddressAttribute buildFromCursor(Cursor cursor)
			throws InvalidAttributeNameException,
			InvalidAttributeValueException {
		if (!(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_NAME)).equals(Constants.RESERVED_WORDS.address.toString()))) {
			throw new InvalidAttributeNameException("Attribute name does not match that present in cursor");
		}
		mCursor = cursor;
		mAttribute = AddressAttributeFactory.createAttribute();
		return (getAttribute());
	}

	@Override
	protected void setValue() throws InvalidAttributeValueException {
		try {
			JSONObject object = new JSONObject(mCursor.getString(mCursor.getColumnIndex(AttributesTable.COLUMN_NAME_VALUE)));
			AddressValue value = new AddressValue(object.getString(AddressValue.STREET_ADDRESS), object.getString(AddressValue.LOCALITY),
					object.getString(AddressValue.REGION), object.getString(AddressValue.POSTAL_CODE), object.getString(AddressValue.COUNTRY));
			mAttribute.setValue(value);
		} catch (JSONException e) {
			throw new InvalidAttributeValueException();
		}
	}
}
