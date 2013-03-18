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
