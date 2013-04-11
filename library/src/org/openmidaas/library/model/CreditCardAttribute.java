package org.openmidaas.library.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.routines.CreditCardValidator;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.model.core.AbstractAttribute;

public class CreditCardAttribute extends AbstractAttribute<CreditCardValue>{
	
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
	
	private boolean validateCard(CreditCardValue value) {
		CreditCardValidator validator = new CreditCardValidator(CreditCardValidator.AMEX + CreditCardValidator.DINERS + 
				CreditCardValidator.DISCOVER + CreditCardValidator.MASTERCARD + CreditCardValidator.VISA);
		SimpleDateFormat date = new SimpleDateFormat("MM/yy");
		Date expiry;
		try {
			expiry = date.parse(value.getExpiryMonth()+"/"+value.getExpiryYear()+"");
		} catch (ParseException e) {
			return false;
		}
		boolean expired = expiry.before(new Date());
		if(expired) {
			return false;
		}
		if(value.getHolderName() == null || value.getHolderName().isEmpty()) {
			return false;
		}
		if(!(validator.isValid(value.getCreditCardNumber()))) {
			return false;
		}
		return true;
	}
}
