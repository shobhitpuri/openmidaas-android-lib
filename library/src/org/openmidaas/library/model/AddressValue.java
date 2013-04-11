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
