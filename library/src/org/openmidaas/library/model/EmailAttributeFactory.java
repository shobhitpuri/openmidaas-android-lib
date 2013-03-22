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
 * Creates a new email attribute
 */
public class EmailAttributeFactory implements AbstractAttributeFactory<EmailAttribute, String>{

	
	public EmailAttribute createAttributeFromCursor(Cursor cursor) {
		EmailAttribute emailAttribute = new EmailAttribute(new InitializeEmailVerification(), new CompleteEmailVerification(), new DeviceIdAuthentication());
		emailAttribute.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(AttributeEntry._ID))));
		emailAttribute.setSignedToken(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_TOKEN)));
		try {
			emailAttribute.setValue(cursor.getString(cursor.getColumnIndex(AttributeEntry.COLUMN_NAME_VALUE)));
		} catch (InvalidAttributeValueException e) {
			// should not get an exception here, otherwise we have a serious bug. 
			e.printStackTrace();
		}
		return emailAttribute;
	}
	
	@Override
	public EmailAttribute createAttribute(String email) throws InvalidAttributeValueException {
		EmailAttribute emailAttribute = new EmailAttribute(new InitializeEmailVerification(), new CompleteEmailVerification(), new DeviceIdAuthentication());
		emailAttribute.setValue(email);
		// saves attribute to the DB in background. 
		AttributePersistenceCoordinator.saveAttribute(emailAttribute, null);
		return emailAttribute;
	}
}
