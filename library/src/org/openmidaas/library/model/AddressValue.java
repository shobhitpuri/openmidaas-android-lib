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

public class AddressValue {
	
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
		setFormattedAddress();
	}

	public String getAddressLine() {
		return streetAddress;
	}

	public void setAddressLine(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	private void setFormattedAddress() {
		this.formattedAddress = this.streetAddress+"\n"+this.locality+" " + this.region + " " + this.postalCode + "\n" + this.country;
	}
	
	public String getFormattedAddress() {
		return formattedAddress;
	}
}
