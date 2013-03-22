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
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.model.core.PersistenceCallback;
import org.openmidaas.library.persistence.AttributeDBPersistenceDelegate;
import org.openmidaas.library.persistence.AttributeEntry;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.database.Cursor;

/**
 * Creates a new generic attribute
 */
public class GenericAttributeFactory implements AbstractAttributeFactory<GenericAttribute, String>{

	private String mAttributeName;
	
	private String mValue;
	
	public GenericAttributeFactory() {
		
	}
	
	public void setAttributeName(String name) {
		mAttributeName = name;
	}
	
	public GenericAttributeFactory(String attributeName) {
		if (attributeName == null) { throw new IllegalArgumentException(); }
		mAttributeName = attributeName;
	}
	
	public GenericAttribute createAttributeFromCursor(Cursor cursor) {
		GenericAttribute attribute = new GenericAttribute(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_NAME)));
		attribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributeEntry._ID))));
		attribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_TOKEN)));
		try {
			attribute.setValue(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_VALUE)));
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attribute;
		
	}
	
	@Override
	public GenericAttribute createAttribute(String value) {
		GenericAttribute attribute = new GenericAttribute(mAttributeName);
		try {
			attribute.setValue(value);
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// saves to the DB in the background.
		AttributePersistenceCoordinator.saveAttribute(attribute, null);
		return (attribute);
	}

}
