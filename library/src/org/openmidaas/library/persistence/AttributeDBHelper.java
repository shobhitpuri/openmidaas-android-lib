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
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttributeDBHelper extends SQLiteOpenHelper{

	private final String TAG = "AttributeSQLiteHelper";

	private static AttributeDBHelper mInstance = null;
	
	private static final String DATABASE_NAME = "attributes.db";
	
	private AttributeDBHelper() {
		super(MIDaaS.getContext(), DATABASE_NAME, null, 1);
	}
	
	public static synchronized AttributeDBHelper getInstance() {
		if(mInstance == null) {
			mInstance = new AttributeDBHelper();
		}
		return mInstance;
	}
	
	
//	public AttributeDBHelper(final Context context) {
//		super(context, DATABASE_NAME, null, 1);
//	}

	/**
	 * Creates the Attributes table.
	 * attributes
	 * 		_id 	-  	primary key 	(INTEGER)
	 * 		name 	- 	attribute name 	(TEXT)
	 * 		value 	- 	attribute value (TEXT)
	 * 		token	-	signed token	(TEXT)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		MIDaaS.logDebug(TAG, "Creating database and tables");
		db.execSQL("CREATE TABLE " + AttributeEntry.TABLE_NAME + " (" + 
				AttributeEntry._ID 					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				AttributeEntry.COLUMN_NAME_NAME 	+ " TEXT NOT NULL, " +
				AttributeEntry.COLUMN_NAME_VALUE 	+ " TEXT UNIQUE, " +
				AttributeEntry.COLUMN_NAME_TOKEN 	+ " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MIDaaS.logDebug(TAG, "Upgrading database version from: " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + AttributeEntry.TABLE_NAME);
	    onCreate(db);
	}
}
