package org.openmidaas.library.common.network;

import org.json.JSONObject;
import org.openmidaas.library.common.Constants;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class AVSServer {

	private static boolean SERVER_WITH_SSL = false;
	
	
	public static void setWithSSL(boolean val) {
		SERVER_WITH_SSL = val;
	}
	
	public static void registerDevice(JSONObject registrationData,
			AsyncHttpResponseHandler responseHandler) {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.REGISTRATION_URL, registrationData, responseHandler);
	}

	public static void startAttributeVerification(JSONObject attributeData,
			AsyncHttpResponseHandler responseHandler) {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.INIT_AUTH_URL, attributeData, responseHandler);
	}

	public static void completeAttributeVerification(JSONObject attributeData,
			AsyncHttpResponseHandler responseHandler) {
		ConnectionManager.postRequest(SERVER_WITH_SSL, Constants.COMPLETE_AUTH_URL, attributeData, responseHandler);	
	}
}
