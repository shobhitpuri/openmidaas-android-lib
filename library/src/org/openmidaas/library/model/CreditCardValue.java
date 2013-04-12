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

public class CreditCardValue {
	
	public static final String CARD_NUMBER = "creditCard";
	
	public static final String CARD_EXPIRY_MONTH = "expiryMonth";
	
	public static final String CARD_EXPIRY_YEAR = "expiryYear";
	
	public static final String CARD_HOLDER_NAME = "cardHolderName";

	public static final String CARD_CVV = "cardCvv";
	
	private String mCardNumber;
	
	private short mExpiryMonth;
	
	private short mExpiryYear;
	
	private String mHolderName;
	
	private short mCVV;
	
	public static enum CARD_TYPE { VISA, MASTER_CARD, AMEX, DISCOVER, DINERS_CLUB, JCB }
	
	private CARD_TYPE mCardType;
	
	public CreditCardValue(String creditCardNumber, short expiryMonth, short expiryYear, String holderName) {
		this.mCardNumber = creditCardNumber;
		this.mExpiryMonth = expiryMonth;
		this.mExpiryYear = expiryYear;
		this.mHolderName = holderName;
		this.mCVV = 0;
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
	
	protected void setCardType(CARD_TYPE type) {
		this.mCardType = type;
	}
	
	public CARD_TYPE getCardType() {
		return mCardType;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.mCardNumber = null;
		this.mCVV = 0;
		this.mExpiryMonth = 0;
		this.mExpiryYear = 0;
		this.mHolderName = null;
		this.mCardType = null;
		super.finalize();
	}
	
	@Override
	public String toString() {
		JSONObject object = new JSONObject();
		try {
			object.put(CARD_NUMBER, this.mCardNumber);
			object.put(CARD_EXPIRY_MONTH, this.mExpiryMonth);
			object.put(CARD_EXPIRY_YEAR, this.mExpiryYear);
			object.put(CARD_HOLDER_NAME, this.mHolderName);
			if(this.mCVV != 0) {
				object.put(CARD_CVV, this.mCVV);
			}
		} catch (JSONException e) {
			object = null;
		}
		return object.toString();	
	}
}
