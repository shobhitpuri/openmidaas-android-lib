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
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.NotVerifiableException;

public class GenericAttributeTest {
	static GenericAttribute genericAttribute;
	static String attributeName = "firstName";
	
	@BeforeClass
	public  static void testSetup(){
		genericAttribute = new GenericAttributeFactory(attributeName).createAttribute();
	}
	
	@AfterClass
	public  static void testCleanup(){}
	
	@Test
	public void testAttributeName() {
		Assert.assertEquals(attributeName, genericAttribute.getName());
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void createAttributeWithNullName() {
		GenericAttribute genericAttribute = new GenericAttributeFactory(null).createAttribute();
	}
	
	@Test(expected=NotVerifiableException.class)
	public void startVerificationWithoutCallback() throws Exception {
		genericAttribute.startVerification(null);
	}
	
	@Test(expected=NotVerifiableException.class)
	public void startVerificationWithCallback() throws Exception {
		genericAttribute.startVerification(new InitializeVerificationCallback() {

			@Override
			public void onSuccess() {
				
			}

			@Override
			public void onError() {
				
			}
			
		});
	}
}
