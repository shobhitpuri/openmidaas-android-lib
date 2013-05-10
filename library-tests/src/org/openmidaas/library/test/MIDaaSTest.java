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
package org.openmidaas.library.test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;
import org.openmidaas.library.test.authentication.MockAccessTokenSuccessStrategy;
import org.openmidaas.library.test.models.MockPersistence;
import org.openmidaas.library.test.network.MockTransport;
import org.openmidaas.library.test.network.MockTransportFactory;
import org.openmidaas.library.test.network.MockVerifiedAttributeBundleRequest;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Base64;

public class MIDaaSTest extends InstrumentationTestCase {
	private Context mContext;
	private MockTransportFactory mockFactory;
	public static final String VALID_CLIENT_ID = "http://merchant.org";
	private final String STATE = "some_state";
	private boolean mStatus = false;
	protected void setUp() {
		mContext = getInstrumentation().getContext();
		MIDaaS.setContext(mContext);
		AttributePersistenceCoordinator.setPersistenceDelegate(new MockPersistence());
		AuthenticationManager.getInstance().setAccessTokenStrategy(new MockAccessTokenSuccessStrategy());
		mockFactory = new MockTransportFactory("verification_bundle_response.json");
		mockFactory.setTrasport(new MockTransport(mContext));
		ConnectionManager.setNetworkFactory(mockFactory);
	}

	@SmallTest
	public void testValidAttributeBundle() {
		mockFactory.setTrasport(new MockVerifiedAttributeBundleRequest(mContext));
		mockFactory.setFilename("verification_bundle_response.json");
		ConnectionManager.setNetworkFactory(mockFactory);
		final CountDownLatch latch = new CountDownLatch(1);
		Map<String, AbstractAttribute<?>> map = new HashMap<String, AbstractAttribute<?>>();
		map.put("mock1", new MockAttribute());
		try {
			MIDaaS.getVerifiedAttributeBundle(VALID_CLIENT_ID, STATE, map, new MIDaaS.VerifiedAttributeBundleCallback() {

				@Override
				public void onSuccess(String verifiedResponse) {
					mStatus = true;
					latch.countDown();
				}

				@Override
				public void onError(MIDaaSException exception) {
					mStatus = false;
					latch.countDown();
				}
			});
		} catch (IllegalArgumentException e) {
			Assert.fail();
		}
		try {
			latch.await();
			if(!mStatus) {
				Assert.fail();
			}
		} catch (InterruptedException e) {
			Assert.fail();
		}
	}

	@SmallTest
	public void testNullClientId() {
		final CountDownLatch latch = new CountDownLatch(1);
		Map<String, AbstractAttribute<?>> map = new HashMap<String, AbstractAttribute<?>>();
		map.put("mock1", new MockAttribute());
		try {
			MIDaaS.getVerifiedAttributeBundle(null, STATE, map, new MIDaaS.VerifiedAttributeBundleCallback() {

				@Override
				public void onSuccess(String verifiedResponse) {
					mStatus = true;
					latch.countDown();
				}

				@Override
				public void onError(MIDaaSException exception) {
					mStatus = false;
					latch.countDown();
				}
			});
		} catch (IllegalArgumentException e) {
			mStatus = true;
			latch.countDown();

		}
		try {
			latch.await();
			if(!mStatus) {
				Assert.fail();
			} 
		} catch (InterruptedException e) {
			Assert.fail();
		}
	}

	@SmallTest
	public void testNullState() {
		mockFactory.setTrasport(new MockVerifiedAttributeBundleRequest(mContext));
		mockFactory.setFilename("verification_bundle_response.json");
		ConnectionManager.setNetworkFactory(mockFactory);
		final CountDownLatch latch = new CountDownLatch(1);
		Map<String, AbstractAttribute<?>> map = new HashMap<String, AbstractAttribute<?>>();
		map.put("mock1", new MockAttribute());
		try {
			MIDaaS.getVerifiedAttributeBundle(VALID_CLIENT_ID, null, map, new MIDaaS.VerifiedAttributeBundleCallback() {

				@Override
				public void onSuccess(String verifiedResponse) {
					mStatus = true;
					latch.countDown();
				}

				@Override
				public void onError(MIDaaSException exception) {
					mStatus = false;
					latch.countDown();
				}
			});
		} catch (IllegalArgumentException e) {
			Assert.fail();
		}
		try {
			latch.await();
			if(!mStatus) {
				Assert.fail();
			}
		} catch (InterruptedException e) {
			Assert.fail();
		}
	}

	@SmallTest
	public void testNullAttributeValueInMap() {
		final CountDownLatch latch = new CountDownLatch(1);
		Map<String, AbstractAttribute<?>> map = new HashMap<String, AbstractAttribute<?>>();
		map.put("mock1", null);
		try {
			MIDaaS.getVerifiedAttributeBundle(VALID_CLIENT_ID, STATE, map, new MIDaaS.VerifiedAttributeBundleCallback() {

				@Override
				public void onSuccess(String verifiedResponse) {
					mStatus = true;
					latch.countDown();
				}

				@Override
				public void onError(MIDaaSException exception) {
					mStatus = false;
					latch.countDown();
				}
			});
		} catch (IllegalArgumentException e) {
			Assert.fail();
		}
		try {
			latch.await();
			if(!mStatus) {

			} else {
				Assert.fail();
			}
		} catch (InterruptedException e) {
			Assert.fail();
		}
	}

	protected void tearDown() {
		AuthenticationManager.getInstance().setAccessTokenStrategy(null);
		
	}
	
	@SmallTest
	public void testAttributeBundle() {
		Map<String, AbstractAttribute<?>> map = new HashMap<String, AbstractAttribute<?>>();
		map.put("mock1", new MockAttribute());
		String bundleAsString = MIDaaS.getAttributeBundle(VALID_CLIENT_ID, null, map);
		Assert.assertNotNull(bundleAsString);
		String[] segments = bundleAsString.split("\\.");
		String header = segments[0];
		byte[] headerBytes = Base64.decode(header, Base64.NO_PADDING + Base64.NO_WRAP);
		byte[] bodyBytes = Base64.decode(segments[1], Base64.NO_PADDING + Base64.NO_WRAP);
		try {
			String headerAsString = new String(headerBytes, "UTF-8");
			JSONObject object = new JSONObject(headerAsString);
			if(!object.getString("alg").equals("none")) {
				Assert.fail();
			}
			String bodyAsString = new String(bodyBytes, "UTF-8");
			JSONObject bodyObject = new JSONObject(bodyAsString);
			if(!object.getString("alg").equals("none")) {
				Assert.fail();
			}
			if(!bodyObject.getString("iss").equals("org.openmidaas.library")) {
				Assert.fail();
			}
			if(!bodyObject.getString("aud").equals(VALID_CLIENT_ID)) {
				Assert.fail();
			}
			if(bodyObject.isNull("attrs")) {
				Assert.fail();
			}
			JSONObject attributes = bodyObject.getJSONObject("attrs"); 
			Iterator<?> keys = attributes.keys();
			// parsing through the "attrs" field now.
			while(keys.hasNext()) {
				String key = (String)keys.next();
				if(attributes.get(key) != null) {
					if(!attributes.get(key).equals("MockAttribute")) {
						Assert.fail();
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			Assert.fail();
		} catch (JSONException e) {
			Assert.fail();
		}
		
	}


	
}
