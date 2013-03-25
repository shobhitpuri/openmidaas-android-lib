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

import org.openmidaas.library.model.core.AbstractAttributeFactory;
import org.openmidaas.library.persistence.AttributeEntry;


import android.database.Cursor;

public class DeviceAttributeFactory implements AbstractAttributeFactory<DeviceAttribute>{

	protected DeviceAttributeFactory(){}
	
	@Override
	public DeviceAttribute createAttribute(String value) throws InvalidAttributeValueException {
		DeviceAttribute token = new DeviceAttribute();
		token.setValue(value);
		return token;
	}
	
	@Override
	public DeviceAttribute createAttributeFromCursor(Cursor cursor) throws InvalidAttributeValueException {
		DeviceAttribute attribute = new DeviceAttribute();
		attribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributeEntry._ID))));
		attribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_TOKEN)));
		attribute.setValue(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_VALUE)));
		return attribute;
	}
}
