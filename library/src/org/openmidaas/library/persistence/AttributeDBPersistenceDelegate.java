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
package org.openmidaas.library.persistence;

import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AttributeDBPersistenceDelegate implements AttributePersistenceDelegate{

	private SQLiteDatabase database;
	
	private AttributeDBHelper dbHelper;

	public AttributeDBPersistenceDelegate(Context context) {
		dbHelper = new AttributeDBHelper(context);
		
	}
	
	public void close() {
		dbHelper.close();
	}
	@Override
	public void saveAttribute(AbstractAttribute<?> data) {
		try {
			database = dbHelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("name", data.getName());
			contentValues.put("value", data.getValue().toString());
			contentValues.put("token", data.getSignedToken());
			database.insert(AttributeEntry.TABLE_NAME, null, contentValues);
		} finally {
			database.endTransaction();
			database.close();
		}
	}

	@Override
	public void deleteAttribute(AbstractAttribute<?> data) {
		database.delete(AttributeEntry.TABLE_NAME, "_id = ", null);
	}
}
