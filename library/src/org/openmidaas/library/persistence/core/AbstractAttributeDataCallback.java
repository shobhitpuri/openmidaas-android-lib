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
 * 
 * Abstract callback that returns the attribute data from
 * persistence storage. 
 *
 * @param <T>
 */
public abstract class AbstractAttributeDataCallback<T>  {
	
	/**
	 * Returns a list of attributes of type T.
	 * @param list attribute list of type T
	 */
	public abstract void onSuccess(List<T> list);
	
	/**
	 * Returns an exception 
	 * @param exception
	 */
	public abstract void onError(MIDaaSException exception);

}
