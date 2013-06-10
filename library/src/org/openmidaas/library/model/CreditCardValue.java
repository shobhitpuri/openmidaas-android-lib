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

import java.util.EnumMap;
import java.util.Map;

/**
 * 
 * ADT that defines the value of a credit card attribute. 
 *
 */
public class CreditCardValue {
	
	public static final String CARD_NUMBER = "card_no";
	
	public static final String CARD_EXPIRY_MONTH = "expiryMonth";
	
	public static final String CARD_EXPIRY_YEAR = "expiryYear";
	
	public static final String CARD_HOLDER_NAME = "cardHolderName";

	public static final String CARD_CVV = "cardCvv";
	
	private String mCardNumber = null;
	
	private String mExpiryMonth = null;
	
	private String mExpiryYear = null;
	
	private String mHolderName = null;
	
	private String mCVV = null;
	
	public static enum CARD_TYPE { 
		VISA, 
		MASTER_CARD, 
		AMEX, 
		DISCOVER,
		DINERS_CLUB,
		JCB 
	}
	
	private Map<CARD_TYPE, String> mCardTypeMap = new EnumMap<CARD_TYPE, String>(CARD_TYPE.class);
	
	private CARD_TYPE mCardType = null;
	
	public CreditCardValue(String creditCardNumber, String cvv, String expiryMonth, String expiryYear, String holderName) {
		this.mCardNumber = creditCardNumber;
		this.mExpiryMonth = expiryMonth;
		this.mExpiryYear = expiryYear;
		this.mHolderName = holderName;
		this.mCVV = cvv;
		mCardTypeMap.put(CARD_TYPE.VISA, "Visa");
		mCardTypeMap.put(CARD_TYPE.MASTER_CARD, "MasterCard");
		mCardTypeMap.put(CARD_TYPE.AMEX, "Amex");
		mCardTypeMap.put(CARD_TYPE.DISCOVER, "Discover");
		mCardTypeMap.put(CARD_TYPE.DINERS_CLUB, "Diners Club");
		mCardTypeMap.put(CARD_TYPE.JCB, "JCB Co.");
	}

	public String getCreditCardNumber() {
		return mCardNumber;
	}

	public String getExpiryMonth() {
		return mExpiryMonth;
	}

	public String getExpiryYear() {
		return mExpiryYear;
	}

	public String getHolderName() {
		return mHolderName;
	}

	public String getCVV() {
		return mCVV;
	}

	protected void setCardType(CARD_TYPE type) {
		this.mCardType = type;
	}
	
	public String getCardType() {
		return mCardTypeMap.get(this.mCardType);
	}

	public void setCardNumber(String cardNumber) {
		this.mCardNumber = cardNumber;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.mExpiryMonth = expiryMonth;
	}

	public void setExpiryYear(String expiryYear) {
		this.mExpiryYear = expiryYear;
	}

	public void setHolderName(String holderName) {
		this.mHolderName = holderName;
	}

	public void setCVV(String cvv) {
		this.mCVV = cvv;
	}

	@Override
	protected void finalize() throws Throwable {
		this.mCardNumber = null;
		this.mCVV = null;
		this.mExpiryMonth = null;
		this.mExpiryYear = null;
		this.mHolderName = null;
		this.mCardType = null;
		super.finalize();
	}
}
