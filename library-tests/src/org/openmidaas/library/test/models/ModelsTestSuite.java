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
package org.openmidaas.library.test.models;




import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;


public class ModelsTestSuite extends InstrumentationTestRunner {
	@Override
	public junit.framework.TestSuite getAllTests() {
		InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
		suite.addTestSuite(AddressAttributeTest.class);
		suite.addTestSuite(AuthenticationStrategyTest.class);
		suite.addTestSuite(CreditCardAttributeTest.class);
		suite.addTestSuite(DeviceRegistrationTest.class);
		suite.addTestSuite(EmailAttributeTest.class);
		suite.addTestSuite(GenericAttributeTest.class);
		suite.addTestSuite(PhoneAttributeTest.class);
		return suite;
	}

	@Override
	public ClassLoader getLoader() {
		return ModelsTestSuite.class.getClassLoader();
	}
}
