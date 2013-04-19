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
package org.openmidaas.library.model;

import org.openmidaas.library.common.Constants;
import org.openmidaas.library.model.core.MIDaaSException;

/**
 * Creates a new generic attribute factory
 */
public class GenericAttributeFactory {
	
	
	/**
	 * Creates a generic attribute with the specified name and value. 
	 * @param name
	 * @return
	 * @throws InvalidAttributeNameException 
	 * @throws InvalidAttributeValueException
	 * @throws IllegalArgumentException
	 * @throws MIDaaSException 
	 */
	public static GenericAttribute createAttribute(String name) throws InvalidAttributeNameException {
		
		if(name == null || name.isEmpty()) {
			throw new InvalidAttributeNameException("Attribute name cannot be null or empty");
		}
		for(Constants.RESERVED_WORDS attrName: Constants.RESERVED_WORDS.values()) {
			if(name.equalsIgnoreCase(attrName.toString())) {
				throw new InvalidAttributeNameException("The specified name conflicts with a reserved attribute name");
			}	
		}
		
		GenericAttribute attribute = new GenericAttribute(name); 
		return attribute;
	}
}
