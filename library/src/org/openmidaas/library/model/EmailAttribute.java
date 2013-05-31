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

/**
 * Email Attribute class.
 */
public class EmailAttribute extends AbstractAttribute<String> {
	
	private final String TAG = "EmailAttribute";
	
	//Credit for REGEX rule: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	
	private final String EMAIL_REGEX_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; 
	
	/**
	 * Constructs a new email attribute. A new instance must be
	 * created via the provided factory method. 
	 * @param initEmailDelegate - the delegate class that starts the email verification process.
	 * @param completeEmailDelegate - the delegate class that completes the email verification process.
	 */
	protected EmailAttribute(InitializeAttributeVerificationDelegate initEmailDelegate,
			CompleteAttributeVerificationDelegate completeEmailDelegate) {
		mName = Constants.RESERVED_WORDS.email.toString();
		mInitVerificationDelegate = initEmailDelegate;
		mCompleteVerificationDelegate = completeEmailDelegate;
		mState = ATTRIBUTE_STATE.NOT_VERIFIED;
	}

	/**
	 * Checks to see if the provided email address is valid.
	 */
	@Override
	protected boolean validateAttribute(String value) {
		if(value == null || value.isEmpty()) {
			MIDaaS.logError(TAG, "Attribute value is null/empty");
			return false;
		}
		Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/**
	 * Starts the email verification process by sending the email address to the 
	 * Attribute Verification Service. The Attribute Verification Server sends a 
	 * one-time code to the email address. 
	 */
	@Override
	public void startVerification(final InitializeVerificationCallback callback) {
		WorkQueueManager.getInstance().addWorkerToQueue(new Worker() {

			@Override
			public void execute() {
				mInitVerificationDelegate.startVerification(EmailAttribute.this, callback);
			}
			
		});
	}
	
	/**
	 * Completes the email verification process by sending a one-time code to the 
	 * Attribute Verification Service.
	 */
	@Override
	public void completeVerification(final String code, final CompleteVerificationCallback callback)  {
		WorkQueueManager.getInstance().addWorkerToQueue(new Worker() {

			@Override
			public void execute() {
				mCompleteVerificationDelegate.completeVerification(EmailAttribute.this, code, callback);
			}
		});
		
	}	
}
