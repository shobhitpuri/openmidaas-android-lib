/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.Constants.ATTRIBUTE_STATE;
import org.openmidaas.library.common.WorkQueueManager;
import org.openmidaas.library.common.WorkQueueManager.Worker;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.CompleteAttributeVerificationDelegate;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.InitializeAttributeVerificationDelegate;
import org.openmidaas.library.model.core.InitializeVerificationCallback;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

/**
 * Phone Attribute Class. 
 * It expects Phone Number in E-164 format recommended by International Telecommunication Union. 
 */
public class PhoneAttribute extends AbstractAttribute<String>{
	
	private final String TAG = "PhoneAttribute";
	
	public enum VERIFICATION_METHOD{
		sms, call
	}

	/**
	 * Constructs a new phone attribute. A new instance must be
	 * created via the provided factory method. 
	 * @param initPhoneDelegate - the delegate class that starts the phone verification process.
	 * @param completePhoneDelegate - the delegate class that completes the phone verification process.
	 */
	protected PhoneAttribute(InitializeAttributeVerificationDelegate initPhoneDelegate,
			CompleteAttributeVerificationDelegate completePhoneDelegate) {
		mName = Constants.RESERVED_WORDS.phone.toString();
		mInitVerificationDelegate = initPhoneDelegate;
		mCompleteVerificationDelegate = completePhoneDelegate;
		mState = ATTRIBUTE_STATE.NOT_VERIFIED;
	}
	
	/**
	 * Checks to see if the provided phone number is a possible and valid number.
	 * @param value: The value of the 'phone' attribute. It highly encouraged to use E-164 format for phone numbers. 
	 * 				 Format is something like: +<CountryCode><City/AreaCode><LocalNumber>
	 * 				 For example: +14162223333 number is in valid E164 format for Toronto city in Canada. 
	 * 				 Here '1' as country code, '416' as area code and 2223333 as 7 digit local number. 
	 * 				 For more information see: http://en.wikipedia.org/wiki/E.164
	 */
	@Override
	protected boolean validateAttribute(String value) {

		//Check if Null or empty
		if(value == null || value.isEmpty()) {
			MIDaaS.logError(TAG, "Phone attribute value is null/empty");
			return false;
		}
		//checks if any alphabet is present
		Pattern pattern = Pattern.compile(".*[a-zA-Z].*");    
        Matcher mat = pattern.matcher(value);    
        
        if(mat.find()){
        	MIDaaS.logError(TAG, "No alphabets ae allowed in the Phone attribute. Please make sure the number is in standard E-164 format of\"+<CountryCode><City/AreaCode><LocalNumber>\"");
        	return false;
        }
        
		//Tests whether a phone number matches a valid pattern using libphonenumber library by Google.
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		
		try {
			PhoneNumber parsedNumber = phoneUtil.parse(value, null);
			//Check if its a possible number. It is faster than doing validation.
			//It checks the number based on length and other general rules which applies to all types of phone numbers.
			if(phoneUtil.isPossibleNumber(parsedNumber)){
				// Validation function checks if it matches a valid pattern. 
				// It takes into account starting digits, length and validates based on the country it belongs
				if(phoneUtil.isValidNumber(parsedNumber)){
					if(value.equalsIgnoreCase(phoneUtil.format(parsedNumber, PhoneNumberFormat.E164))){
						return true;
					}else{
						MIDaaS.logError(TAG, "Unacceptable Format Error: Phone Number entered is valid but not in expected standard E-164 format.");
						return false;
					}
				}
			}
		} catch (NumberParseException e) {
			MIDaaS.logError(TAG, "NumberParseException was thrown: " + e.toString());
		}
		MIDaaS.logError(TAG, "Phone Number entered is invalid. Please make sure the number is in standard E-164 format of\"+<CountryCode><City/AreaCode><LocalNumber>\"");
		return false;
	}
	
	/**
	 * Starts the phone verification process by sending the phone number to the 
	 * Attribute Verification Service. 
	 */
	@Override
	public void startVerification(final InitializeVerificationCallback callback) {
		WorkQueueManager.getInstance().addWorkerToQueue(new Worker() {

			@Override
			public void execute() {
				mInitVerificationDelegate.startVerification(PhoneAttribute.this, callback);
			}
			
		});
	}
	
	/**
	 * Completes the phone verification process by sending a one-time code to the 
	 * Attribute Verification Service.
	 */
	@Override
	public void completeVerification(final String code, final CompleteVerificationCallback callback)  {
		WorkQueueManager.getInstance().addWorkerToQueue(new Worker() {

			@Override
			public void execute() {
				mCompleteVerificationDelegate.completeVerification(PhoneAttribute.this, code, callback);
			}
		});
		
	}

	@Override
	public void setPendingData(String data) {
		mPendingData = data;
	}

	@Override
	public String toString() {
		if( (mValue != null) && (!mValue.isEmpty()) ) {
			return mValue;
		}
		MIDaaS.logError(TAG, "Phone number value is null");
		return "";
	}
}
