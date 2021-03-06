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

import org.openmidaas.library.model.core.AbstractAttribute;


/**
 * Defines a generic attribute type. 
 * Generic attributes are non-verifiable and by 
 * default, are always validated. The attribute name *must* 
 * be specified at the time of construction. 
 */
public class GenericAttribute extends AbstractAttribute<String>{
	
	protected GenericAttribute(String name) {
		mName = name;
	}

	/**
	 * Method that validates the format of the attribute. 
	 * A generic attribute always returns true. 
	 */
	protected boolean validateAttribute(String value) {
		return true;
	}
}
