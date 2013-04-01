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

import java.util.ArrayList;
import java.util.List;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.DeviceAttribute;
import org.openmidaas.library.model.DeviceAttributeFactory;
import org.openmidaas.library.model.EmailAttribute;
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.DeviceTokenCallback;
import org.openmidaas.library.model.core.EmailDataCallback;
import org.openmidaas.library.model.core.GenericDataCallback;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.model.core.PersistenceCallback;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;


public class AttributeDBPersistenceDelegate implements AttributePersistenceDelegate{
	
	private final String TAG = "AttributeDBPersistenceDelegate";

	private SQLiteDatabase database;
	
	private AttributeDBHelper dbHelper;
	
	private final int MAX_RETRIES = 3;
	
	public AttributeDBPersistenceDelegate(){
		dbHelper = AttributeDBHelper.getInstance();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/*
	 * Helper interface to get the database object on a
	 * separate thread. 
	 */
	private interface DBOpenerCallback {
		
		public void onSuccess(SQLiteDatabase database);
		
		public void onError(Exception e);
	}
	
	/**
	 * Opens the database on a separate thread as per: 
	 * http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html#getReadableDatabase()
	 * @param callback
	 */
	private void openDatabase(final DBOpenerCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					database = dbHelper.getWritableDatabase();
					callback.onSuccess(database);
				} catch(Exception e) {
					callback.onError(e);
				}
			}
		}).start();
	}
	
	@Override
	public void save(final AbstractAttribute<?> data, final PersistenceCallback callback) {
			try {
				
				database = dbHelper.getWritableDatabase();
				// id we don't have a PK, create a row in the db.
				if(data.getId() == -1) {
					
				
					long rowId = database.insertOrThrow(AttributeEntry.TABLE_NAME, null, getContentValuesForAttribute(data));
					if(rowId == -1) {
						// error saving record;
						// TODO: Retry save. keep a watchdog timer/check on the status of the operation
					} else {
						data.setId(rowId);
						if(callback != null) {
							callback.onSuccess();
							
						}
					}
				}
				// id we have a PK, update the corresponding row. 
				else {
					
					int v = database.update(AttributeEntry.TABLE_NAME, getContentValuesForAttribute(data), "_id =" + data.getId(), null);
					// only 1 row should be updated!
					if (v == 1) {
						if (callback != null) {
							callback.onSuccess();
						}
					}
				}
			} catch (SQLiteConstraintException exception) {
				MIDaaS.logError(TAG, exception.getMessage());
				if(callback != null) {
					callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_ALREADY_EXISTS));
				}
			} catch(Exception e) {
				MIDaaS.logError(TAG, e.getMessage());
				if(callback != null) {
					callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
				}
			}
			finally {
				dbHelper.close();
			}
	}

	@Override
	public void delete(final AbstractAttribute<?> data, PersistenceCallback callback) {
		database = dbHelper.getWritableDatabase();
		if (data.getId() != -1) {
			database.delete(AttributeEntry.TABLE_NAME, "_id = " + data.getId(), null);
			dbHelper.close();
			if(callback != null) {
				callback.onSuccess();
			}
		} else {
			if(callback != null) {
				// data was never persisted so just return success. 
				callback.onSuccess();
			}
		}
	}
	
	/**
	 * Helper function to get content values object from attribute data. 
	 * @param data -  the attribute data to persist. 
	 * @return the content values object that is eventually stored in the DB.
	 */
	private ContentValues getContentValuesForAttribute(AbstractAttribute<?> data) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(AttributeEntry.COLUMN_NAME_NAME, data.getName());
		contentValues.put(AttributeEntry.COLUMN_NAME_VALUE, data.getValue().toString());
		contentValues.put(AttributeEntry.COLUMN_NAME_TOKEN, data.getSignedToken());
		contentValues.put(AttributeEntry.COLUMN_NAME_PENDING, data.getPendingData());
		if(data.getSignedToken() == null) {
			contentValues.putNull(AttributeEntry.COLUMN_NAME_TOKEN);
		} 
		if(data.getPendingData() == null) {
			contentValues.putNull(AttributeEntry.COLUMN_NAME_PENDING);
		}
		return contentValues;
	}

	@Override
	public void getEmails(final EmailDataCallback callback) {
		//XXX: in the future, it would seem prudent to create a work queue. 
		List<EmailAttribute> list = new ArrayList<EmailAttribute>();
		Cursor cursor = fetchByAttributeName("email");
		boolean isDataAvailable = cursor.moveToFirst();
		if(isDataAvailable) {
			EmailAttributeFactory emailFactory = AttributeFactory.createEmailAttributeFactory();
			while(!(cursor.isAfterLast())) {
				try {
					list.add(emailFactory.createAttributeFromCursor(cursor));
				} catch (InvalidAttributeValueException e) {
					// we should never get to this point otherwise we have a bug storing the data.
					callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
				}
				cursor.moveToNext();
			}
			cursor.close();
		} else {
			callback.onSuccess(list);
		}
		cursor.close();
		callback.onSuccess(list);
	}

	@Override
	public void getGenerics(final String attributeName, final GenericDataCallback callback) {
		List<GenericAttribute> list = new ArrayList<GenericAttribute>();
		Cursor cursor = fetchByAttributeName(attributeName);
		boolean isDataAvailable = cursor.moveToFirst();
		if(isDataAvailable) {
			GenericAttributeFactory af = AttributeFactory.createGenericAttributeFactory();
			while(!(cursor.isAfterLast())) {
				try {
					list.add(af.createAttributeFromCursor(cursor));
				} catch (InvalidAttributeValueException e) {
					// we should never get to this point otherwise we have a bug storing the data.
					callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
				}
				 cursor.moveToNext();
			}
			cursor.close();
		} else {
			callback.onSuccess(list);
		}
		cursor.close();
		callback.onSuccess(list);
	}
	
	private Cursor fetchByAttributeName(String name) {
		
		database = dbHelper.getWritableDatabase();
		Cursor cursor = database.query(AttributeEntry.TABLE_NAME, null, "name=?", new String[] { name }, null, null, null);
		return cursor;
	}

	@Override
	public void getDeviceToken(final DeviceTokenCallback callback) {
		List<DeviceAttribute> list = new ArrayList<DeviceAttribute>();
		DeviceAttributeFactory factory = AttributeFactory.createDeviceAttributeFactory();
		Cursor cursor = fetchByAttributeName("device");
		if(cursor.getCount()>0) {
			cursor.moveToFirst();
			// this loop should run only once. 
			while(!(cursor.isAfterLast())) {
				try {
					list.add(factory.createAttributeFromCursor(cursor));
				} catch (InvalidAttributeValueException e) {
					// we should never get to this point otherwise we have a bug storing the data.
					callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
				}
				cursor.moveToNext();
			}
			cursor.close();
			callback.onSuccess(list);
		} else {
			callback.onSuccess(list);
		}
	}
}
