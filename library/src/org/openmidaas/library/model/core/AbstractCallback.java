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

/**
 * Class that defines an abstract callback that 
 * other callback implementations can use. 
 *
 */
public abstract class AbstractCallback {
	/**
	 * This method is called when a successful 
	 * operation takes place. 
	 */
	public abstract void onSuccess();
	
	/**
	 * This method is called when an error occurs while
	 * performing an operation. 
	 * @param exception - The OpenMIDaaSException containing 
	 * the error code and description. 
	 */
	public abstract void onError(OpenMIDaaSException exception);
	
}
