/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 * http://www.apache.org/licenses/LICENSE-2.0
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
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import android.database.Cursor;

/**
 * Creates a new generic attribute factory
 */
public class GenericAttributeFactory implements AbstractAttributeFactory<GenericAttribute>{

	private String mAttributeName;
	
	
	
	protected GenericAttributeFactory() {}
	
	public GenericAttributeFactory setAttributeName(String name) {
		mAttributeName = name;
		return this;
	}
	
	@Override
	public GenericAttribute createAttributeFromCursor(Cursor cursor) throws InvalidAttributeValueException {
		GenericAttribute attribute = new GenericAttribute(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_NAME)));
		attribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributeEntry._ID))));
		attribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_TOKEN)));
		attribute.setValue(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_VALUE)));
		return attribute;
	}
	
	/**
	 * @deprecated Use the method createAttribute(String name, String value) to create a GenericAttribute instead. Otherwise, call
	 * "setAttributeName(String name)" before calling createAttribute(String value).
	 */
	@Override
	@Deprecated
	public GenericAttribute createAttributeWithValue(String value) throws InvalidAttributeValueException, IllegalArgumentException {
		if(mAttributeName == null || mAttributeName.isEmpty()) {
			throw new IllegalArgumentException("Attribute value cannot be set when attribute name is null. Try calling \"setAttributeName()\" first");
		}
		GenericAttribute attribute = new GenericAttribute(mAttributeName);
		attribute.setValue(value);
		// saves to the DB in the background.
		AttributePersistenceCoordinator.saveAttribute(attribute, null);
		return (attribute);
	}
	
	/**
	 * Creates a generic attribute with the specified name and value. 
	 * @param name
	 * @param value
	 * @return
	 * @throws InvalidAttributeValueException
	 * @throws IllegalArgumentException
	 */
	public GenericAttribute createAttribute(String name, String value) throws InvalidAttributeValueException, IllegalArgumentException {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Attribute value cannot be set when attribute name is null. Try calling \"setAttributeName()\" first");
		}
		GenericAttribute attribute = new GenericAttribute(name);
		attribute.setValue(value);
		// saves to the DB in the background.
		AttributePersistenceCoordinator.saveAttribute(attribute, null);
		return attribute;
	}
}
