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
package org.openmidaas.library.persistence.core;


import org.openmidaas.library.model.core.AbstractAttribute;

/**
 * Interface to persist attributes. 
 * Implement this interface to persist attribute using your own 
 * storage engine.
 */
public interface AttributePersistenceDelegate extends PersistenceDelegate<AbstractAttribute<?>>{
	
	/**
	 * Returns all email attributes in a list via the callback
	 * @param callback - callback to get a list of emails
	 */
	public void getEmails(EmailDataCallback callback);

	/**
	 * Returns generic attribute with the specified name in a list via the callback
	 * @param attributeName - the attribute name to get
	 * @param callback - callback to get list of generic attributes with the specified name
	 */
	public void getGenerics(String attributeName, GenericDataCallback callback);

	/**
	 * Returns the subject token via a callback
	 * @param callback - the subject token callback
	 */
	public void getSubjectToken(SubjectTokenCallback callback);
	
	/**
	 * Returns all attributes in a list via the callback. 
	 * The attributes are sorted by state: pending, verified, generic/others. 
	 * @param callback - callback for a list of all attributes. 
	 */
	public void getAllAttributes(AttributeDataCallback callback);
	
}
