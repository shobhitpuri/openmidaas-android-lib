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


import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeNameException;
import org.openmidaas.library.model.InvalidAttributeValueException;

import android.database.Cursor;

public class GenericDBBuilder extends AbstractAttributeDBBuilder<GenericAttribute>{
	
	private String mName;
	
	protected GenericDBBuilder() {
	}

	protected void setName(String name) {
		this.mName = name;
	}
	
	@Override
	protected void setValue() throws InvalidAttributeValueException {
		mAttribute.setValue(mCursor.getString(mCursor.getColumnIndex(AttributesTable.COLUMN_NAME_VALUE)));
		
	}

	@Override
	protected GenericAttribute buildFromCursor(Cursor cursor)
			throws InvalidAttributeNameException,
			InvalidAttributeValueException {
		if (!(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_NAME)).equals(mName))) {
			throw new InvalidAttributeNameException("Attribute name does not match that present in cursor");
		}
		mCursor = cursor;
		mAttribute = GenericAttributeFactory.createAttribute(mName);
		return (getAttribute());
	}


	
}
