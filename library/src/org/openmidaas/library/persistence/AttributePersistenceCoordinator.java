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

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.WorkQueueManager;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.core.AttributeDataCallback;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;
import org.openmidaas.library.persistence.core.CreditCardDataCallback;
import org.openmidaas.library.persistence.core.EmailDataCallback;
import org.openmidaas.library.persistence.core.GenericDataCallback;
import org.openmidaas.library.persistence.core.ShippingAddressDataCallback;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

/**
 * 
 * Wrapper around a specific attribute persistence delegate. 
 *
 */
public class AttributePersistenceCoordinator {
	
	private static String TAG = "AttributePersistenceCoordinator";
	
	private static AttributePersistenceDelegate mDelegate = null;
	
	/**
	 * Sets the persistence delegate
	 * @param delegate persistence delegate
	 */
	public static void setPersistenceDelegate(AttributePersistenceDelegate delegate) {
		mDelegate = delegate;
	}
	
	
	/**
	 * Removes the specified attribute from persistence storage
	 * @param attribute the attribute to remove
	 * @return true/false if the operation succeeded/failed
	 * @throws MIDaaSException
	 */
	public static boolean removeAttribute(AbstractAttribute<?> attribute) throws MIDaaSException {
		MIDaaS.logDebug(TAG, "deleteing attribute: " + attribute.getName());
		return (mDelegate.delete(attribute));
	}
	
	/**
	 * Saves the specified attribute to persistence storage
	 * @param attribute the attribute to store
	 * @return true/false if the operation succeeded/failed
	 * @throws MIDaaSException
	 */
	public static boolean saveAttribute(AbstractAttribute<?> attribute) throws MIDaaSException {
		MIDaaS.logDebug(TAG, "saving attribute: " + attribute.getName());
		return (mDelegate.save(attribute));
	}
	
	/**
	 * Returns the subject token from persistence storage
	 * @param callback
	 */
	public static void getSubjectToken(final SubjectTokenCallback callback) {
		MIDaaS.logDebug(TAG, "fetching subject token");
		mDelegate.getSubjectToken(callback);
	}
	
	/**
	 * Returns a list of email from persistence storage
	 * @param callback
	 */
	public static void getEmails(final EmailDataCallback callback) {
		MIDaaS.logDebug(TAG, "fetching emails");
		WorkQueueManager.getInstance().addWorkerToQueue(new WorkQueueManager.Worker() {
			
			@Override
			public void execute() {
				mDelegate.getEmails(callback);
			}
		});
	}
	
	/**
	 * Returns a list of generic attribute with the specified name from 
	 * persistence storage
	 * @param attributeName the attribute name 
	 * @param callback
	 */
	public static void getGenericAttributes(final String attributeName, final GenericDataCallback callback) {
		MIDaaS.logDebug(TAG, "fetching generic attributes");
		WorkQueueManager.getInstance().addWorkerToQueue(new WorkQueueManager.Worker() {
			
			@Override
			public void execute() {
				mDelegate.getGenerics(attributeName, callback);
			}
		});
	}
	
	/**
	 * Returns all attributes stored in persistence storage
	 * @param callback
	 */
	public static void getAllAttributes(final AttributeDataCallback callback) {
		MIDaaS.logDebug(TAG, "fetching all attributes");
		WorkQueueManager.getInstance().addWorkerToQueue(new WorkQueueManager.Worker() {
			@Override
			public void execute() {
				mDelegate.getAllAttributes(callback);
			}
		});
	}
	
	public static void getCreditCardAttributes(final CreditCardDataCallback callback) {
		MIDaaS.logDebug(TAG, "fetching credit card attributes");
		WorkQueueManager.getInstance().addWorkerToQueue(new WorkQueueManager.Worker() {
			@Override
			public void execute() {
				mDelegate.getCreditCards(callback);
			}
		});
	}
	
	public static void getShippingAddressAttributes(final ShippingAddressDataCallback callback) {
		MIDaaS.logDebug(TAG, "fetching shipping address attributes");
		WorkQueueManager.getInstance().addWorkerToQueue(new WorkQueueManager.Worker() {
			@Override
			public void execute() {
				mDelegate.getShippingAddresses(callback);
			}
		});
	}
}
