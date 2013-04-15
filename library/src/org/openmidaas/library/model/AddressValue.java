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

/**
 * 
 * ADT that describes an address value
 *
 */
public class AddressValue {
	
	public static final String STREET_ADDRESS = "streetAddress";
	
	public static final String LOCALITY = "locality";
	
	public static final String REGION = "region";
	
	public static final String POSTAL_CODE = "postalCode";
	
	public static final String COUNTRY = "country";
	
	private String streetAddress;
	
	private String locality;
	
	private String region;
	
	private String postalCode;
	
	private String country;
	
	private String formattedAddress;
	
	public AddressValue(String streetAddress, String locality, String region, String postalCode, String country) {
		this.streetAddress = streetAddress;
		this.locality = locality;
		this.region = region;
		this.postalCode = postalCode;
		this.country = country;
	}

	public String getAddressLine() {
		return streetAddress;
	}

	public String getLocality() {
		return locality;
	}

	public String getRegion() {
		return region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	private void setFormattedAddress() {
		this.formattedAddress = this.streetAddress+"\n"+this.locality+" " + this.region + " " + this.postalCode + "\n" + this.country;
	}
	
	public String getFormattedAddress() {
		setFormattedAddress();
		return formattedAddress;
	}
	
	@Override
	public String toString() {
		JSONObject object = new JSONObject();
		try {
			object.put(STREET_ADDRESS, this.streetAddress);
			object.put(LOCALITY, this.locality);
			object.put(REGION, this.region);
			object.put(POSTAL_CODE, this.postalCode);
			object.put(COUNTRY, this.country);
		} catch (JSONException e) {
			object = null;
		}
		return object.toString();	
	}
}
