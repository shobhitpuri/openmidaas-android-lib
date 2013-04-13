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
package org.openmidaas.library.test.models;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributeDBPersistence;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class GenericAttributeTest extends InstrumentationTestCase {
	private String attributeName = "firstName";
	private String attributeValue = "Rob";
	private Context mContext;
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		AttributePersistenceCoordinator.setPersistenceDelegate(new MockPersistence());
		//getInstrumentation().getContext().deleteDatabase("attributes.db");
		
	}
	
	@SmallTest
	public void testPendingDataIsNull() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			Assert.assertEquals(null, genericAttribute.getPendingData());
			} catch (InvalidAttributeValueException e) {
				Assert.fail();
			} catch (IllegalArgumentException e) {
				Assert.fail();
			} catch (MIDaaSException e) {
				Assert.fail();
			}
	}
	
	@SmallTest
	public void testSetPendingDataThrowsException() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			genericAttribute.setPendingData("blob");
			Assert.fail();
			} catch (InvalidAttributeValueException e) {
				Assert.fail();
			} catch (UnsupportedOperationException ex) {
				
			} catch (IllegalArgumentException e) {
				Assert.fail();
			} catch (MIDaaSException e) {
				Assert.fail();
			}
	}
	
	@SmallTest
	public void testDeprecatedMethodCallWithNameNotSet() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttributeWithValue(attributeValue);
			Assert.fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
			e.printStackTrace();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testDeperecatedMethodCallWithNameSet() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().setAttributeName(attributeName).createAttributeWithValue(attributeValue);
			
		} catch(IllegalArgumentException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
			e.printStackTrace();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testAttributeName() {
		try {
		GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(attributeName, attributeValue);
		Assert.assertEquals(genericAttribute.getName(), attributeName);
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
		
	}
	
	@SmallTest
	public void testCreateAttributeWithNullName() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(null, attributeValue);
			Assert.fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			
		} catch(InvalidAttributeValueException e) {
			
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testStartVerificationWithoutCallback() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			genericAttribute.startVerification(null);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testStartVerificationWithCallback() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			genericAttribute.startVerification(new InitializeVerificationCallback() {

				@Override
				public void onSuccess() {
					
				}

				@Override
				public void onError(MIDaaSException exception) {
					
					
				}
			});
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testCompleteVerification() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.getGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			genericAttribute.completeVerification("1234", new CompleteVerificationCallback() {

				@Override
				public void onSuccess() {
					
					
				}

				@Override
				public void onError(MIDaaSException exception) {
					
					
				}
				
			});
			Assert.fail("Expected UnsupportedOperationException");
		} catch(UnsupportedOperationException e) {
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.fail();
		} catch (MIDaaSException e) {
			Assert.fail();
		}
	}
}
