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
package org.openmidaas.library.model.core;

public enum MIDaaSError {
	ERROR_AUTHENTICATING_DEVICE(100, "There was an error authenticating the device. "),
	SERVER_ERROR(101, "Error with the attribute server. "),
	DEVICE_REGISTRATION_ERROR(102, "There as an error with device registration."),
	ATTRIBUTE_ALREADY_EXISTS(200, "The current attribute already exists. "),
	DATABASE_ERROR(201, "There was an error with the database"),
	ATTRIBUTE_RETRIEVAL_MISMATCH(202, "The attribute being retrieved is not what is was originally requested"),
	ATTRIBUTE_NAME_ERROR(203, "The requested attribute does not match the attribute being retrieved from the database."),
	ATTRIBUTE_VALUE_ERROR(204, "The attribute value is invalid."),
	ATTRIBUTE_STATE_ERROR(205, "The current attribute state does not match with what is required."),
	INTERNAL_LIBRARY_ERROR(300, "There an interal error occurred in the library.");
	
	
	private final int mErrorCode;
	
	private final String mErrorMessage;
	
	private MIDaaSError(int errorCode, String errorMessage) {
		this.mErrorCode = errorCode;
		this.mErrorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return mErrorMessage;
	}
	
	public int getErrorCode() {
		return mErrorCode;
	}
}
