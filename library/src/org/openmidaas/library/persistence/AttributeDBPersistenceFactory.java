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
package org.openmidaas.library.persistence;

import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.persistence.core.PersistenceDelegate;
import org.openmidaas.library.persistence.core.PersistenceFactory;

public class AttributeDBPersistenceFactory implements PersistenceFactory<AbstractAttribute<?>>{

	private String dbName;
	
	public AttributeDBPersistenceFactory(String dbName) {
		this.dbName = dbName;
		
	}

	@Override
	public PersistenceDelegate<AbstractAttribute<?>> createPersistenceStore() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
