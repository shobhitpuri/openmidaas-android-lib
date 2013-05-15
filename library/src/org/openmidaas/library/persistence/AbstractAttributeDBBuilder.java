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

import org.openmidaas.library.model.InvalidAttributeNameException;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.AbstractAttribute;

import android.database.Cursor;


/**
 * 
 * Abstraction for building attributes from a DB record.  
 * Given a cursor, this class builds the abstract attribute model. 
 * The "value" is specific to the attribute being created and therefore
 * must be implemented in the inheriting class. 
 * 
 */
public abstract class AbstractAttributeDBBuilder<T extends AbstractAttribute<?>> {
	
	protected Cursor mCursor;

	protected T mAttribute;
	
	private void buildCommon() {
		mAttribute.setId(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(AttributesTable._ID))));
		String pendingData = mCursor.getString(mCursor.getColumnIndex(AttributesTable.COLUMN_NAME_PENDING));
		if(pendingData != null) {
			mAttribute.setPendingData(pendingData);
		}
		String signedToken = mCursor.getString(mCursor.getColumnIndex(AttributesTable.COLUMN_NAME_TOKEN));
		if(signedToken != null) {
			mAttribute.setSignedToken(signedToken);
		}
		String label = mCursor.getString(mCursor.getColumnIndex(AttributesTable.COLUMN_NAME_LABEL));
		if(label != null) {
			mAttribute.setLabel(label);
		}
	}
	
	/**
	 * Builds the attribute from the cursor
	 * @param cursor
	 * @return the attribute
	 * @throws InvalidAttributeNameException
	 * @throws InvalidAttributeValueException
	 */
	protected abstract T buildFromCursor(Cursor cursor) throws InvalidAttributeNameException, InvalidAttributeValueException; 
	
	/**
	 * Sets the value property of the attribute
	 * @throws InvalidAttributeValueException
	 */
	protected abstract void setValue() throws InvalidAttributeValueException;
	
	/**
	 * Builds and returns the attribute
	 * @return the attribute
	 * @throws InvalidAttributeValueException
	 */
	protected T getAttribute() throws InvalidAttributeValueException {
		buildCommon();
		setValue();
		return mAttribute;
	}
	
}
