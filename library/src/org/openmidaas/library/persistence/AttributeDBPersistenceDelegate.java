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

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.model.core.PersistenceCallback;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class AttributeDBPersistenceDelegate implements AttributePersistenceDelegate{

	private SQLiteDatabase database;
	
	private AttributeDBHelper dbHelper;
	
	private final int MAX_RETRIES = 3;

	private static AttributeDBPersistenceDelegate mInstance = null;
	
	private AttributeDBPersistenceDelegate(){
		dbHelper = new AttributeDBHelper(MIDaaS.getContext());
	}
	
	public static synchronized AttributeDBPersistenceDelegate getInstance() {
		if (mInstance == null) {
			return new AttributeDBPersistenceDelegate();
		}
		return mInstance;
	}
	
	public void close() {
		dbHelper.close();
	}
	@Override
	public void saveAttribute(final AbstractAttribute<?> data, final PersistenceCallback callback) {
		// DB operation is an async operation. Needs to be done on a separate thread.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// id we don't have a PK, create a row in the db.
					if(data.getId() == -1) {
						database = dbHelper.getWritableDatabase();
					
						long rowId = database.insertOrThrow(AttributeEntry.TABLE_NAME, null, getContentValuesForAttribute(data));
						if(rowId == -1) {
							// error saving record;
							// TODO: Retry save
						} else {
							data.setId(rowId);
						}
					}
					// id we have a PK, update the corresponding row. 
					else {
						database.update(AttributeEntry.TABLE_NAME, getContentValuesForAttribute(data), "_id=" + data.getId(), null);
					}
				} catch (SQLiteConstraintException exception) {
					callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_ALREADY_EXISTS));
				} finally {
					database.close();
				}
			}
			
		}).start();
	}

	@Override
	public void deleteAttribute(final AbstractAttribute<?> data) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (data.getId() != -1) {
					database.delete(AttributeEntry.TABLE_NAME, "_id = " + data.getId(), null);
				}
			}
			
		}).start();
		
	}
	
	private ContentValues getContentValuesForAttribute(AbstractAttribute<?> data) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", data.getName());
		contentValues.put("value", data.getValue().toString());
		if(data.getSignedToken() == null) {
			contentValues.putNull("token");
		} else {
			contentValues.put("token", data.getSignedToken());
		}
		return contentValues;
	}
}
