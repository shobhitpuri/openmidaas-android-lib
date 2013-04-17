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
package org.openmidaas.library.test.models;

import java.util.ArrayList;
import java.util.List;

import org.openmidaas.library.common.Constants;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.core.AttributeDataCallback;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;
import org.openmidaas.library.persistence.core.CreditCardDataCallback;
import org.openmidaas.library.persistence.core.EmailDataCallback;
import org.openmidaas.library.persistence.core.GenericDataCallback;
import org.openmidaas.library.persistence.core.ShippingAddressDataCallback;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

public class MockPersistence implements AttributePersistenceDelegate{

	private List<AbstractAttribute<?>> mDataList = new ArrayList<AbstractAttribute<?>>(); 
	
	@Override
	public boolean save(AbstractAttribute<?> data) throws MIDaaSException {
		return (mDataList.add(data));
	}

	@Override
	public boolean delete(AbstractAttribute<?> data) throws MIDaaSException {
		return (mDataList.remove(data));
	}

	@Override
	public void getEmails(EmailDataCallback callback) {
		
	}

	@Override
	public void getGenerics(String attributeName, GenericDataCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSubjectToken(SubjectTokenCallback callback) {
		List<SubjectToken> mList = new ArrayList<SubjectToken>();
		for(AbstractAttribute<?> attribute: mDataList) {
			if(attribute.getName().equalsIgnoreCase(Constants.RESERVED_WORDS.subject_token.toString())) {
				mList.add((SubjectToken) attribute);
			}
		}
		callback.onSuccess(mList);
	}

	@Override
	public void getAllAttributes(AttributeDataCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getShippingAddresses(ShippingAddressDataCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCreditCards(CreditCardDataCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
