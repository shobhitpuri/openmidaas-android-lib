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
import org.openmidaas.library.model.AttributeFactory;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSException;

import android.test.suitebuilder.annotation.SmallTest;

public class GenericAttributeTest extends TestCase {
	private String attributeName = "firstName";
	private String attributeValue = "Rob";
	public void setUp() {	
	}
	
	@SmallTest
	public void testPendingDataIsNull() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			Assert.assertEquals(null, genericAttribute.getPendingData());
			} catch (InvalidAttributeValueException e) {
				Assert.fail();
			}
	}
	
	@SmallTest
	public void testSetPendingDataThrowsException() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			genericAttribute.setPendingData("blob");
			Assert.fail();
			} catch (InvalidAttributeValueException e) {
				Assert.fail();
			} catch (UnsupportedOperationException ex) {
				
			}
	}
	
	@SmallTest
	public void testDeprecatedMethodCallWithNameNotSet() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeValue);
			Assert.fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	@SmallTest
	public void testDeperecatedMethodCallWithNameSet() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().setAttributeName(attributeName).createAttribute(attributeValue);
			
		} catch(IllegalArgumentException e) {
			Assert.fail();
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	@SmallTest
	public void testAttributeName() {
		try {
		GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeName, attributeValue);
		Assert.assertEquals(genericAttribute.getName(), attributeName);
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		}
		
	}
	
	@SmallTest
	public void testCreateAttributeWithNullName() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(null, attributeValue);
			Assert.fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
			
		} catch(InvalidAttributeValueException e) {
			
		}
	}
	
	@SmallTest
	public void testStartVerificationWithoutCallback() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeName, attributeValue);
			genericAttribute.startVerification(null);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			
		} catch (InvalidAttributeValueException e) {
			Assert.fail();
		}
	}
	
	@SmallTest
	public void testStartVerificationWithCallback() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeName, attributeValue);
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
		}
	}
	
	@SmallTest
	public void testCompleteVerification() {
		try {
			GenericAttribute genericAttribute = AttributeFactory.createGenericAttributeFactory().createAttribute(attributeName, attributeValue);
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
		}
	}
}
