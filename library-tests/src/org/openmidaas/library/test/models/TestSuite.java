package org.openmidaas.library.test.models;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;


public class TestSuite extends InstrumentationTestRunner {
	@Override
	public junit.framework.TestSuite getAllTests() {
		InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
		suite.addTestSuite(EmailAttributeTest.class);
		suite.addTestSuite(GenericAttributeTest.class);
		return suite;
	}

	@Override
	public ClassLoader getLoader() {
		return TestSuite.class.getClassLoader();
	}
}
