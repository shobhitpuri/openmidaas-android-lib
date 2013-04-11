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
import org.openmidaas.library.persistence.AttributesTable;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;


import android.database.Cursor;

/**
 * Email attribute factory implementation that creates new email attributes. 
 */
public class EmailAttributeFactory implements AbstractAttributeFactory<EmailAttribute, String> {

	protected EmailAttributeFactory(){}
	
	@Override
	public EmailAttribute createAttributeFromCursor(Cursor cursor) throws InvalidAttributeValueException {
		EmailAttribute emailAttribute = new EmailAttribute(new InitializeEmailVerification(), new CompleteEmailVerification());
		emailAttribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributesTable._ID))));
		emailAttribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_TOKEN)));
		emailAttribute.setValue(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_VALUE)));
		emailAttribute.setPendingData(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_PENDING)));
		return emailAttribute;
	}
	
	@Override
	public EmailAttribute createAttributeWithValue(String email) throws InvalidAttributeValueException, MIDaaSException {
		EmailAttribute emailAttribute = new EmailAttribute(new InitializeEmailVerification(), new CompleteEmailVerification());
		emailAttribute.setValue(email);
		AttributePersistenceCoordinator.saveAttribute(emailAttribute);
		return emailAttribute;
	}
}
