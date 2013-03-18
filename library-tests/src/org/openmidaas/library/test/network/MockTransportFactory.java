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
package org.openmidaas.library.test.network;

import org.openmidaas.library.common.network.INetworkFactory;
import org.openmidaas.library.common.network.INetworkTransport;

import android.content.Context;

public class MockTransportFactory implements INetworkFactory{
	
	private String mFileName;

	private Context mContext;
	
	private MockTransport transport;
	
	public MockTransportFactory(Context context, String filename) {
		mContext = context;
		mFileName = filename;
	}
	
	@Override
	public INetworkTransport createTransport() {
		transport = new MockTransport(mContext);
		transport.setMockDataFile(mFileName);
		return (transport);
	}

	public void setFilename(String fileName) {
		transport.setMockDataFile(fileName);
	}
}
