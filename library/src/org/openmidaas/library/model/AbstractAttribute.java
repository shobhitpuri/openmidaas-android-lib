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

/**
 * Abstract class that defines an attribute
 */

public abstract class AbstractAttribute implements Verifiable{
	
	protected InitializeAttributeVerificationDelegate mInitVerificationDelegate;
	
	protected CompleteAttributeVerificationDelegate mCompleteVerificationDelegate;
	
	private String mName;
	
	protected Object mValue;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public Object getValue() {
		return mValue;
	}

	public void setValue(Object value) {
		this.mValue = value;
	}
	
	public void initializeVerification(InitializeVerificationCallback callback) {
		mInitVerificationDelegate.initializeVerification(callback);
	}
	
	public void completeVerification(CompleteVerificationCallback callback) {
		mCompleteVerificationDelegate.completeVerification(callback);
	}
}
