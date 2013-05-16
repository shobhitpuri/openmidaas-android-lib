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
	 * Converts a phone number to E164 format
	 * @param phoneNumber : Input phone number. It will first check if its a possible number and then convert it.  
	 * @return	 		  : Phone number in E-164 format
	 */
	public String convertToE164Standard(String phoneNumber){
		String convertedNumber = null;
		try{
			if(!phoneNumber.contains("+")){ // If no '+' sign then add one to make it into a format that is expected by function. 
				phoneNumber = '+'+phoneNumber;
			}
			PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
			try {
				PhoneNumber parsedNumber = phoneUtil.parse(phoneNumber, null); 
				// Since country code is unknown, so we can make the second parameter null. 
				// Then input number has to be in some international format with '+' sign(thus we checked for presence of one before) .
				if(phoneUtil.isPossibleNumber(parsedNumber)){ //check if its a possible number
					convertedNumber = phoneUtil.format(parsedNumber, PhoneNumberFormat.E164); // convert the number
					MIDaaS.logDebug(TAG, "Number in E-164 format is: "+convertedNumber);
					return convertedNumber;
				}
				
			} catch (NumberParseException e) {
				MIDaaS.logError(TAG, "Following Exception was thrown: " + e.toString());
			}
		}catch(NullPointerException e){
			MIDaaS.logError(TAG, "Following exception was thrown: "+e.toString());
		}
		return phoneNumber;
	}
	
	/**
	 * Gets the number in E-164 format, if the number is possible. 
	 * Then validates the number.
	 */
	@Override
	public void setValue(String value) throws InvalidAttributeValueException {
			String convertedValue = convertToE164Standard(value);
			super.setValue(convertedValue);
	}
	
	/**
	 * Checks to see if the provided phone number is valid. 
	 * Whatever user enters if removes all the special characters and then checks whether the entered number is valid. 
	 */
	@Override
	protected boolean validateAttribute(String value) {
		
		//Check if Null or empty
		if(value == null || value.isEmpty()) {
			MIDaaS.logError(TAG, "Phone attribute value is null/empty");
			return false;
		}	
		
		// Checks if '+' sign is present once in the given phone number string. If not then add it in the beginning. 
		//If its at the beginning or even somewhere else, then the next step will take care of its validation. 
		if(!value.contains("+")){
			value = '+'+value;
		}
		
		//Tests whether a phone number matches a valid pattern using libphonenumber library by Google.
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber parsedNumber = phoneUtil.parse(value, null); 
			if(phoneUtil.isValidNumber(parsedNumber)){ // takes into account starting digits, length and validates based on the country it belongs 
				MIDaaS.logDebug(TAG, "Phone Number entered is a valid number.");
				return true;
			}
		} catch (NumberParseException e) {
			MIDaaS.logError(TAG, "NumberParseException was thrown: " + e.toString());
		}
		MIDaaS.logError(TAG, "Phone Number entered is invalid.");
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
}
