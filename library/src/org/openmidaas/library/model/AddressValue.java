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
	
	private String mStreetAddress;
	
	private String mLocality;
	
	private String mRegion;
	
	private String mPostalCode;
	
	private String mCountry;
	
	private String mFormattedAddress;
	
	public AddressValue(String streetAddress, String locality, String region, String postalCode, String country) {
		this.mStreetAddress = streetAddress;
		this.mLocality = locality;
		this.mRegion = region;
		this.mPostalCode = postalCode;
		this.mCountry = country;
	}

	public String getAddressLine() {
		return mStreetAddress;
	}

	public String getLocality() {
		return mLocality;
	}

	public String getRegion() {
		return mRegion;
	}

	public String getPostalCode() {
		return mPostalCode;
	}

	public String getCountry() {
		return mCountry;
	}

	private void setFormattedAddress() {
		this.mFormattedAddress = this.mStreetAddress+"\n"+this.mLocality+", " + this.mRegion + ", " + this.mPostalCode + "\n" + this.mCountry;
	}
	
	public String getFormattedAddress() {
		setFormattedAddress();
		return mFormattedAddress;
	}
	
	public void setStreetAddress(String streetAddress) {
		this.mStreetAddress = streetAddress;
	}

	public void setLocality(String locality) {
		this.mLocality = locality;
	}

	public void setRegion(String region) {
		this.mRegion = region;
	}

	public void setPostalCode(String postalCode) {
		this.mPostalCode = postalCode;
	}

	public void setCountry(String country) {
		this.mCountry = country;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.mFormattedAddress = formattedAddress;
	}
}
