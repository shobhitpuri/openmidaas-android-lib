package org.openmidaas.library.test;

import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.test.network.MockTransportFactory;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class MIDaaSTest extends InstrumentationTestCase{
	
	private Context mContext;
	
	
	protected void setUp() throws Exception {
		mContext = getInstrumentation().getContext();
		
	}
	
	public void testInitialization() throws Exception {
		
	}

}
