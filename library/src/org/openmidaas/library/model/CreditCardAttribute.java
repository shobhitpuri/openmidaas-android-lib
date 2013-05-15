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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.Constants.ATTRIBUTE_STATE;
import org.openmidaas.library.model.CreditCardValue.CARD_TYPE;
import org.openmidaas.library.model.core.AbstractAttribute;

/**
 * 
 * ADT for a verifiable credit card attribute
 *
 */
public class CreditCardAttribute extends AbstractAttribute<CreditCardValue>{
	
	/**
	 * @uml.property  name="tAG"
	 */
	private final String TAG = "CreditCardAttribute";
	// source: http://www.regular-expressions.info/creditcard.html
	/**
	 * @uml.property  name="vISA_PATTERN"
	 */
	private final String VISA_PATTERN = "^4[0-9]{12}(?:[0-9]{3})?$";
	/**
	 * @uml.property  name="mC_PATTERN"
	 */
	private final String MC_PATTERN = "^5[1-5][0-9]{14}$";
	/**
	 * @uml.property  name="aMEX_PATTERN"
	 */
	private final String AMEX_PATTERN = "^3[47][0-9]{13}$";
	/**
	 * @uml.property  name="dC_PATTERN"
	 */
	private final String DC_PATTERN = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
	/**
	 * @uml.property  name="dISCOVER_PATTERN"
	 */
	private final String DISCOVER_PATTERN = "^6(?:011|5[0-9]{2})[0-9]{12}$";
	/**
	 * @uml.property  name="jCB_PATTERN"
	 */
	private final String JCB_PATTERN = "^(?:2131|1800|35\\d{3})\\d{11}$";
	
	protected CreditCardAttribute() {
		mName = Constants.RESERVED_WORDS.credit_card.toString();
		mState = ATTRIBUTE_STATE.NOT_VERIFIED;
	}

	@Override
	protected boolean validateAttribute(CreditCardValue value) {
		if (value == null) {
			MIDaaS.logError(TAG, "CreditCardValue is null");
			return false;
		}
		// holder name should not be blank
		if(value.getHolderName() == null || value.getHolderName().isEmpty()) {
			MIDaaS.logError(TAG, "Card holder name is null/empty");
			return false;
		}
		try {
			if(value.getExpiryMonth() == null || value.getExpiryYear() == null || value.getExpiryMonth().isEmpty() || value.getExpiryYear().isEmpty()) {
				MIDaaS.logError(TAG, "Expiry month/year is null/empty");
				return false;
			}
			// The SimpleDateFormat below sets invalid month formats to 1 (Jan) (e.g. 14)
			// we need to ensure it doesn't get passed to the function. 
			if((Integer.parseInt(value.getExpiryMonth())) < 1 || (Integer.parseInt(value.getExpiryMonth())) >12) {
				MIDaaS.logError(TAG, "Expiry month is invalid");
				return false;
			}
			// check cvv
			if(value.getCVV() == null) {
				MIDaaS.logError(TAG, "CVV is null");
				return false;
			}
			// check to see if cvv is an integer and not a-z
			if(!(value.getCVV().isEmpty())) {
				Integer.parseInt(value.getCVV());
			}
		} catch(NumberFormatException e) {
			MIDaaS.logError(TAG, "CVV is NaN");
			return false;
		}
		
		// get the format as MM/yy
		SimpleDateFormat date = new SimpleDateFormat("MM/yy", Locale.US);
		Date expiry;
		try {
			// check the expiry date. 
			expiry = date.parse(value.getExpiryMonth()+"/"+value.getExpiryYear()+"");
		} catch (ParseException e) {
			MIDaaS.logError(TAG, "Unable to parse date format.");
			return false;
		}
		boolean expired = expiry.before(new Date());
		if(expired) {
			MIDaaS.logError(TAG, "Card has expired.");
			return false;
		}

		if(value.getCreditCardNumber() == null || value.getCreditCardNumber().isEmpty()) {
			MIDaaS.logError(TAG, "Credit card number is null/empty");
			return false;
		} else {
			String cardNumber = value.getCreditCardNumber();
			if(Pattern.matches(VISA_PATTERN, cardNumber)) {
				value.setCardType(CARD_TYPE.VISA);
				
			} else if(Pattern.matches(MC_PATTERN, cardNumber)) {
				value.setCardType(CARD_TYPE.MASTER_CARD);
				
			} else if(Pattern.matches(AMEX_PATTERN, cardNumber)) {
				value.setCardType(CARD_TYPE.AMEX);
				
			} else if(Pattern.matches(DISCOVER_PATTERN, cardNumber)) {
				value.setCardType(CARD_TYPE.DISCOVER);
				
			} else if(Pattern.matches(DC_PATTERN, cardNumber)) {
				value.setCardType(CARD_TYPE.DINERS_CLUB);
				
			} else if(Pattern.matches(JCB_PATTERN, cardNumber)) {
				value.setCardType(CARD_TYPE.JCB);
				
			} else {
				MIDaaS.logError(TAG, "Credit card number is invalid");
				return false;
			}
			return true;
		}
	}
	
	@Override
	public void setPendingData(String data) {
		mPendingData = data;
	}
	
	@Override
	public String toString() {
		if(mValue != null) {
			return (mValue.getCreditCardNumber() + "\n" + String.format("%02d", Integer.parseInt(mValue.getExpiryMonth())) + 
					"/" +mValue.getExpiryYear() + "\n" + mValue.getHolderName());
		 
		}
		MIDaaS.logError(TAG, "Value is null");
		return "";
	}
}
