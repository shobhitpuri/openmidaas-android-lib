package org.openmidaas.library.common.network;

public class AndroidNetworkFactory implements INetworkFactory{

	@Override
	public INetworkTransport createTransport() {
		return (new AndroidNetworkTransport());
	}
}
