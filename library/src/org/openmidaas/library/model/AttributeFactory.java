/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.model;

/**
 * 
 * Wrapper for the creation of various factories. 
 *
 */
public class AttributeFactory {

	public static EmailAttribute getEmailAttribute() {		
		return (new EmailAttribute(new InitializeEmailVerification(), new CompleteEmailVerification()));
	}
	
	public static GenericAttributeFactory getGenericAttributeFactory() {
		return new GenericAttributeFactory();
	}
	
	public static SubjectTokenFactory getSubjectTokenFactory() {
		return new SubjectTokenFactory();
	}
	
	public static CreditCardAttributeFactory getCreditCardAttributeFactory() {
		return new CreditCardAttributeFactory();
	}
	
	public static ShippingAddressAttributeFactory getShippingAddressAttributeFactory() {
		return new ShippingAddressAttributeFactory();
	}
}
