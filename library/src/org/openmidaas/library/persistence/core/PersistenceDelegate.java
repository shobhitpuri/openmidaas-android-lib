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

import java.util.List;

import org.openmidaas.library.model.core.MIDaaSException;

/**
 * Interface that persists data of type T and
 * notifies whether persistence was successful via
 * the persistence callback
 * @param <T> - the type of object to persist
 */
public interface PersistenceDelegate<T> {

	/**
	 * This method saves the data of type T.
	 * @param data - the data to save
	 * @param callback - the persistence callback
	 */
	public boolean save(T data) throws MIDaaSException;
	
	/**
	 * This method deletes the data of type T.
	 * @param data - the data to delete
	 * @param callback - the persistence callback
	 */
	public boolean delete(T data) throws MIDaaSException;

}
