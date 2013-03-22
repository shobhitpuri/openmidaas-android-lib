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

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.core.GenericDataCallback;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AttributeDBPersistenceDelegateTest extends InstrumentationTestCase {
	
	private static final String TEST_VALUE_1 = "TEST_VALUE_1";
	private static final String TEST_VALUE_2 = "TEST_VALUE_2";
	private static final String TEST_VALUE_3 = "TEST_VALUE_3";
	
	private Context mContext;
	private GenericAttribute mAttribute;
	private GenericAttributeFactory factory = new GenericAttributeFactory("test");
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
	}
	
	@SmallTest
	public void testDelete() {
		mContext.deleteDatabase("attributes.db");
		GenericAttribute a1 = factory.createAttribute("1");
		a1.delete();
	}
	
	@SmallTest
	public void testGetGenericAttributes() throws Exception {
		GenericAttribute a1 = factory.createAttribute("TEST_VALUE_1");
		GenericAttribute a2 = factory.createAttribute("TEST_VALUE_2");
		GenericAttribute a3 = factory.createAttribute("TEST_VALUE_3");
		CountDownLatch mLatch = new CountDownLatch(1);
		// Retrieve all the "test" attributes
		AttributePersistenceCoordinator.getGenericAttributes("test", new GenericDataCallback() {

			@Override
			public void onSuccess(List<GenericAttribute> list) {
				for(GenericAttribute a: list) {
					if((a.getValue() == TEST_VALUE_1) || (a.getValue() == TEST_VALUE_2) || (a.getValue() == TEST_VALUE_3) ) {
						continue;
					} else {
						Assert.fail();
					}
				}
			}
		});
		mLatch.await();
	}
}
