package org.openmidaas.library.common.network;

public class AndroidNetworkFactory implements INetworkFactory{

	private String mUrl;
	
	public AndroidNetworkFactory(String hostUrl) {
		mUrl = hostUrl;
	}
	
	@Override
	public INetworkTransport createTransport() {
		return (new AndroidNetworkTransport(mUrl));
	}
}
