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

import org.json.JSONException;
import org.json.JSONObject;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.Constants.ATTRIBUTE_STATE;
import org.openmidaas.library.model.core.AbstractAttribute;

/**
 * 
 * ADT for a verifiable shipping address attribute
 *
 */
public class AddressAttribute extends AbstractAttribute<AddressValue> {
	
	private final String TAG = "AddressAttribute";
	
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
			MIDaaS.logError(TAG, "AddressValue is null");
			return false;
		}
		if(value.getAddressLine() == null || value.getAddressLine().isEmpty()) {
			MIDaaS.logError(TAG, "Street address is null/empty");
			return false;
		}
		if(value.getLocality() == null || value.getLocality().isEmpty()) {
			MIDaaS.logError(TAG, "Locality is null/empty");
			return false;
		}
		if(value.getRegion() == null || value.getRegion().isEmpty()) {
			MIDaaS.logError(TAG, "Region is null/empty");
			return false;
		}
		if(value.getPostalCode() == null || value.getPostalCode().isEmpty()) {
			MIDaaS.logError(TAG, "Postal code is null/empty");
			return false;
		}
		if(value.getCountry() == null || value.getCountry().isEmpty()) {
			MIDaaS.logError(TAG, "Country is null/empty");
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		if(mValue != null) {
			return (mValue.getFormattedAddress());
		} 
		MIDaaS.logError(TAG, "Value is null");
		return "";
	}

	@Override
	public Object getValueAsJSONSerializableObject() {
		if(this.mValue != null) {
			JSONObject object = new JSONObject();
			try {
				object.put(AddressValue.STREET_ADDRESS, this.mValue.getAddressLine());
				object.put(AddressValue.STREET_ADDRESS, this.mValue.getAddressLine());
				object.put(AddressValue.LOCALITY, this.mValue.getLocality());
				object.put(AddressValue.REGION, this.mValue.getRegion());
				object.put(AddressValue.POSTAL_CODE, this.mValue.getPostalCode());
				object.put(AddressValue.COUNTRY, this.mValue.getCountry());
			} catch (JSONException e) {
				MIDaaS.logError(TAG, e.getMessage());
				object = null;
			}
			return object;
		}
		return null;
	}

	@Override
	public Object getResponseTokenValue() {
		if(this.mValue != null) {
			JSONObject object = new JSONObject();
			try {
				object.put("formatted", this.mValue.getFormattedAddress());
				object.put("street_address", this.mValue.getAddressLine());
				object.put("locality", this.mValue.getLocality());
				object.put("region", this.mValue.getRegion());
				object.put("postal_code", this.mValue.getPostalCode());
				object.put("country", this.mValue.getCountry());
			} catch (JSONException e) {
				MIDaaS.logError(TAG, e.getMessage());
				object = null;
			}
			return object;
		}
		return null;
	}
}
