/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.model;

import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.Constants.ATTRIBUTE_STATE;
import org.openmidaas.library.model.core.AbstractAttribute;

/**
 * 
 * ADT for a verifiable shipping address attribute
 *
 */
public class AddressAttribute extends AbstractAttribute<AddressValue> {
	
	protected AddressAttribute() {
		mName = Constants.RESERVED_WORDS.address.toString();
		mState = ATTRIBUTE_STATE.NOT_VERIFIED;
	}

	@Override
	public void setPendingData(String data) {
		mPendingData = data;
	}
	
	@Override
	protected boolean validateAttribute(AddressValue value) {
		if(value == null) {
			return false;
		}
		if(value.getAddressLine() == null || value.getAddressLine().isEmpty()) {
			return false;
		}
		if(value.getLocality() == null || value.getLocality().isEmpty()) {
			return false;
		}
		if(value.getRegion() == null || value.getRegion().isEmpty()) {
			return false;
		}
		if(value.getPostalCode() == null || value.getPostalCode().isEmpty()) {
			return false;
		}
		if(value.getCountry() == null || value.getCountry().isEmpty()) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		if(mValue != null) {
			return (mValue.getFormattedAddress());
		} 
		return "";
	}
}
