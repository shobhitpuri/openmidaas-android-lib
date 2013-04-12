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
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.model.CreditCardValue.CARD_TYPE;
import org.openmidaas.library.model.core.AbstractAttribute;

public class CreditCardAttribute extends AbstractAttribute<CreditCardValue>{
	// source: http://www.regular-expressions.info/creditcard.html
	private final String VISA_PATTERN = "^4[0-9]{12}(?:[0-9]{3})?$";
	private final String MC_PATTERN = "^5[1-5][0-9]{14}$";
	private final String AMEX_PATTERN = "^3[47][0-9]{13}$";
	private final String DC_PATTERN = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
	private final String DISCOVER_PATTERN = "^6(?:011|5[0-9]{2})[0-9]{12}$";
	private final String JCB_PATTERN = "^(?:2131|1800|35\\d{3})\\d{11}$";
	
	protected CreditCardAttribute() {
		mName = Constants.RESERVED_WORDS.CREDIT_CARD;
	}

	@Override
	protected boolean validateAttribute(CreditCardValue value) {
		if (value == null) {
			return false;
		} else {
			return (validateCard(value));
		}
	}
	/**
	 * Checks to see if properties of the credit card object are valid. 
	 * @param value
	 * @return
			return true;
		
	 */
	private boolean validateCard(CreditCardValue value) {
		// The SimpleDateFormat below sets invalid month formats to 1 (Jan)
		// we need to ensure it doesn't get passed to the function. 
		if(value.getExpiryMonth() < 1 || value.getExpiryMonth() >12) {
			return false;
		}
		// holder name should not be blank
		if(value.getHolderName() == null || value.getHolderName().isEmpty()) {
			return false;
		}
		
		if(value.getCVV() != 0) {
			// 3 or more digits in the CVV
			if(value.getCVV() < 100) {
				return false;
			}
		}
		
		// get the format as MM/yy
		SimpleDateFormat date = new SimpleDateFormat("MM/yy", Locale.US);
		
		Date expiry;
		try {
			// check the expiry date. 
			expiry = date.parse(value.getExpiryMonth()+"/"+value.getExpiryYear()+"");
		} catch (ParseException e) {
			return false;
		}
		boolean expired = expiry.before(new Date());
		if(expired) {
			return false;
		}

		if(value.getCreditCardNumber() == null || value.getCreditCardNumber().isEmpty()) {
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
				return false;
			}
			return true;
		}
	}
}
