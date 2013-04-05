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
package org.openmidaas.library.common;

public final class Constants {
	private Constants(){}
	public static final String AVP_SB_BASE_URL = "http://avpsandbox-securekey.dotcloud.com";
	public static final String AVP_LIVE_BASE_URL = "http://avplive-securekey.dotcloud.com";
	public static final String INIT_AUTH_URL = "/1/requestAttributeVerification";
	public static final String COMPLETE_AUTH_URL = "/1/completeAttributeVerification";
	public static final String REGISTRATION_URL = "/1/device/register";
	public enum ATTRIBUTE_STATE { VERIFIED, PENDING_VERIFICATION, NOT_VERIFIABLE, NOT_VERIFIED, ERROR_IN_SAVE, UNKNOWN };
	public static final class RESERVED_WORDS{
		public static final String SUBJECT_TOKEN = "subject_token";
	}
}
