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
package org.openmidaas.library.test.persistence;

import java.io.File;

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.persistence.AttributeDBPersistenceDelegate;
import org.openmidaas.library.persistence.AttributeEntry;
import org.openmidaas.library.test.network.MockTransportFactory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AttributeDBPersistenceDelegateTest extends InstrumentationTestCase {
	
	private static final String TEST_VALUE = "TEST_VALUE";
	private Context mContext;
	private MockAttribute mAttribute;
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		mAttribute = new MockAttribute("MockAttribute", AttributeDBPersistenceDelegate.getInstance());
		// destroy any existing databases before beginning the tests. 
		mContext.deleteDatabase("attributes.db");
	}
	
	@SmallTest
	public void testSave() {
		mContext.deleteDatabase("attributes.db");
		
		// this call persists the value in the database
		try {
			mAttribute.setValue(TEST_VALUE);
			
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch(Exception e) {
			Assert.fail();
		}
	}
}
