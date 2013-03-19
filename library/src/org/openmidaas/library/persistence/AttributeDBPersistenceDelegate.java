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
import org.openmidaas.library.persistence.core.PersistenceDelegate;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AttributeDBPersistenceDelegate implements PersistenceDelegate<AbstractAttribute<?>>{

	private SQLiteDatabase database;
	
	private AttributeSQLiteHelper dbHelper;

	protected AttributeDBPersistenceDelegate(Context context, String dbName) {
		dbHelper = new AttributeSQLiteHelper(context, dbName);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	@Override
	public void saveAttribute(AbstractAttribute<?> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAttribute(AbstractAttribute<?> data) {
		// TODO Auto-generated method stub
		
	}
	
	

}
