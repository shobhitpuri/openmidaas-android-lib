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
package org.openmidaas.library.test;

import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.model.EmailAttribute;
import org.openmidaas.library.model.EmailAttributeFactory;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.InitializeVerificationCallback;
import org.openmidaas.library.model.core.MIDaaSException;

import com.google.mockwebserver.RecordedRequest;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class MIDaaSTest extends ActivityInstrumentationTestCase2{
	private Context mContext;
	private CountDownLatch mLatch = new CountDownLatch(1);
	private boolean notificationSuccess = false;
	
	public MIDaaSTest() {
		super("org.openmidaas.library.test", TestActivity.class);
	}
	
	public void setUp() throws Exception {
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mContext = this.getInstrumentation().getContext();
		
	}
	
	public void testLibraryInitialization() throws Exception {
		MIDaaS.initialize(mContext, new InitializationCallback() {

			@Override
			public void onSuccess() {
				notificationSuccess = true;
				mLatch.countDown();
			}

			@Override
			public void onError(MIDaaSException exception) {
				
			}
			
		});
		mLatch.await();
		Assert.assertTrue(notificationSuccess);
	}
}
