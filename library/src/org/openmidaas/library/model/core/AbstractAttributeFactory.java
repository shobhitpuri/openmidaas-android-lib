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
package org.openmidaas.library.model.core;

import org.openmidaas.library.model.InvalidAttributeValueException;
import android.database.Cursor;

/**
 * Implement this interface to create your custom attributes.
 * @param <T> - the type of attribute. 
 */
public interface AbstractAttributeFactory<T extends AbstractAttribute<?>> {
	
	/**
	 * Creates an attribute of type T with the specified value.
	 * @param value -  the attribute value
	 * @return - the attribute of type T
	 * @throws InvalidAttributeValueException
	 */
	public T createAttribute();
	
	/**
	 * Creates an attribute of type T with the specified cursor. 
	 * @param cursor - the cursor containing the data. 
	 * @return - the attribute of type T
	 * @throws InvalidAttributeValueException
	 */
	public T createAttributeFromCursor(Cursor cursor) throws InvalidAttributeValueException;
}
