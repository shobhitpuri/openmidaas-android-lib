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

public class CreditCardValue {
	
	private String mCardNumber;
	
	private short mExpiryMonth;
	
	private short mExpiryYear;
	
	private String mHolderName;
	
	private short mCVV;
	
	public CreditCardValue(String creditCardNumber, short expiryMonth, short expiryYear, String holderName) {
		this.mCardNumber = creditCardNumber;
		this.mExpiryMonth = expiryMonth;
		this.mExpiryYear = expiryYear;
		this.mHolderName = holderName;
	}

	public String getCreditCardNumber() {
		return mCardNumber;
	}

	public void setCreditCardNumber(String cardNumber) {
		this.mCardNumber = cardNumber;
	}

	public short getExpiryMonth() {
		return mExpiryMonth;
	}

	public void setExpiryMonth(short expiryMonth) {
		this.mExpiryMonth = expiryMonth;
	}

	public short getExpiryYear() {
		return mExpiryYear;
	}

	public void setExpiryYear(short expiryYear) {
		this.mExpiryYear = expiryYear;
	}

	public String getHolderName() {
		return mHolderName;
	}

	public void setHolderName(String holderName) {
		this.mHolderName = holderName;
	}

	public short getCVV() {
		return mCVV;
	}

	public void setCVV(short cvv) {
		this.mCVV = cvv;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.mCardNumber = null;
		this.mCVV = 0;
		this.mExpiryMonth = 0;
		this.mExpiryYear = 0;
		this.mHolderName = null;
		super.finalize();
	}
}
