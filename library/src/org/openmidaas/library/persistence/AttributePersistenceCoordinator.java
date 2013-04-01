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
import org.openmidaas.library.model.core.DeviceTokenCallback;
import org.openmidaas.library.model.core.EmailDataCallback;
import org.openmidaas.library.model.core.GenericDataCallback;
import org.openmidaas.library.model.core.PersistenceCallback;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;

public class AttributePersistenceCoordinator {
	
	private static AttributePersistenceDelegate mDelegate = null;
	
	public static void setPersistenceDelegate(AttributePersistenceDelegate delegate) {
		if(mDelegate == null) {
			mDelegate = delegate;
		}
	}
	
	public static void removeAttribute(AbstractAttribute<?> attribute, PersistenceCallback callback) {
		mDelegate.delete(attribute, callback);
	}
	
	public static void saveAttribute(AbstractAttribute<?> attribute, PersistenceCallback callback) {
		mDelegate.save(attribute, callback);
	}
	
	public static void getEmails(EmailDataCallback callback) {
		mDelegate.getEmails(callback);
	}
	
	public static void getGenericAttributes(String attributeName, GenericDataCallback callback) {
		mDelegate.getGenerics(attributeName, callback);
	}
	
	public static void getDeviceAttribute(DeviceTokenCallback callback){
		mDelegate.getDeviceToken(callback);
	}
	
//	public static void getAllAttributes(AttributesCallback callback) {
//		
//	}

}
