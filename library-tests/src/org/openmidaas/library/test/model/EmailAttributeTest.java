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
package org.openmidaas.library.test.model;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openmidaas.library.model.EmailAttribute;
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.core.InitializeVerificationCallback;


public class EmailAttributeTest {
	static EmailAttribute emailAttribute;
	
	@BeforeClass
	public  static void testSetup(){
		emailAttribute = new EmailAttributeFactory().createAttribute();
	}
	
	@AfterClass
	public  static void testCleanup(){}
	
	@Test
	public void testIsVerifiableIsSet() {
		Assert.assertEquals(true, emailAttribute.isVerifiable());
	}
	
	@Test(expected=InvalidAttributeValueException.class)
	public void testNullEmail() throws Exception {
		emailAttribute.setValue(null);
	}
	
	@Test(expected=InvalidAttributeValueException.class)
	public void testEmptyEmail() throws Exception {
		emailAttribute.setValue("");
	}
	
	@Test
	public void testLabel() {
		String label = "User's email";
		emailAttribute.setLabel(label);
		Assert.assertEquals(label, emailAttribute.getLabel());
	}
	
	@Test
	public void testName() {
		Assert.assertEquals("email", emailAttribute.getName());
	}
	
	@Test
	public void testSetCorrectEmailValue () throws Exception {
		String correctEmail = "rob@gmail.com";
		emailAttribute.setValue(correctEmail);
		Assert.assertEquals(correctEmail, emailAttribute.getValue());	
	}
	
	@Test(expected=InvalidAttributeValueException.class)
	public void testSetInvalidEmailValue() throws Exception {
		String incorrectEmail = "rob@";
		emailAttribute.setValue(incorrectEmail);
	}
}
