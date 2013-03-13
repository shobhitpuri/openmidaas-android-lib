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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.GenericAttributeFactory;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSException;

import android.test.suitebuilder.annotation.SmallTest;

public class GenericAttributeTest extends TestCase {
	private GenericAttribute genericAttribute;
	private String attributeName = "firstName";
	
	public void setUp() {
		genericAttribute = new GenericAttributeFactory(attributeName).createAttribute();
	}
	
	@SmallTest
	public void testAttributeName() {
		Assert.assertEquals(genericAttribute.getName(), attributeName);
	}
	
	@SmallTest
	public void testCreateAttributeWithNullName() {
		try {
			GenericAttribute genericAttribute = new GenericAttributeFactory(null).createAttribute();
			Assert.fail("Expected IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			
		}
	}
	
	@SmallTest
	public void testStartVerificationWithoutCallback() {
		try {
			genericAttribute.startVerification(null);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			
		}
	}
	
	@SmallTest
	public void testStartVerificationWithCallback() {
		try {
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
			
		}
	}
	
	@SmallTest
	public void testCompleteVerification() {
		try {
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
			
		}
	}
}
