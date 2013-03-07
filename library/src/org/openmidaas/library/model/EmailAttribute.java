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

import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.CompleteAttributeVerificationDelegate;
import org.openmidaas.library.model.core.CompleteVerificationCallback;
import org.openmidaas.library.model.core.InitializeAttributeVerificationDelegate;
import org.openmidaas.library.model.core.InitializeVerificationCallback;

/**
 * Email Attribute class
 */
public class EmailAttribute extends AbstractAttribute<String> {
	
	//Credit for regex rule: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	private final String EMAIL_REGEX_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; 
	
	private final String ATTRIBUTE_NAME = "email";
	
	protected EmailAttribute(InitializeAttributeVerificationDelegate initEmailDelegate,
			CompleteAttributeVerificationDelegate completeEmailDelegate) {
		mIsVerifiable = true;
		mName = ATTRIBUTE_NAME;
		mInitVerificationDelegate = initEmailDelegate;
		mCompleteVerificationDelegate = completeEmailDelegate;
	}

	@Override
	protected boolean validateAttribute(String value) {
		if(value == null || value.isEmpty()) {
			return false;
		}
		Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	@Override
	public void startVerification(InitializeVerificationCallback callback) {
		mInitVerificationDelegate.startVerification(callback);
	}
	
	@Override
	public void completeVerification(String code, CompleteVerificationCallback callback)  {
		mCompleteVerificationDelegate.completeVerification(code, callback);
	}
}
