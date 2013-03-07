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
package org.openmidaas.library.test.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openmidaas.library.test.model.EmailAttributeTest;
import org.openmidaas.library.test.model.GenericAttributeTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	GenericAttributeTest.class,
	EmailAttributeTest.class
})
public class ModelTestSuite {
	@BeforeClass 
    public static void setUpClass() {      
        
    }

    @AfterClass 
    public static void tearDownClass() { 
       
    }


}
