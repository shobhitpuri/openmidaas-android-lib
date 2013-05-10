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
package org.openmidaas.library;

import java.util.Map;

import org.openmidaas.library.authentication.AVSAccessTokenStrategy;
import org.openmidaas.library.authentication.AVSDeviceRegistration;
import org.openmidaas.library.authentication.AuthenticationManager;
import org.openmidaas.library.authentication.DeviceRegistrar;
import org.openmidaas.library.authentication.Level0DeviceAuthentication;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.WorkQueueManager;
import org.openmidaas.library.common.WorkQueueManager.Worker;
import org.openmidaas.library.common.network.AVSServer;
import org.openmidaas.library.common.network.AndroidNetworkFactory;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.AttributeDBPersistence;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.content.Context;
import android.util.Log;

/**
 * Class that controls device registration
 */
public final class MIDaaS{
	private static String TAG = "MIDaaS";
	private static int currentLoggingLevel = 6;
	public static final int LOG_LEVEL_VERBOSE = 2;
	public static final int LOG_LEVEL_DEBUG = 3;
	public static final int LOG_LEVEL_INFO = 4;
	public static final int LOG_LEVEL_WARN = 5;
	public static final int LOG_LEVEL_ERROR = 6;
	private static final Object LOG_LOCK = new Object();
	private static final Object LOCK = new Object();
	private static Context mContext;
	
	/**
	 * Returns the current logging level.
	 * @return
	 */
	public static int getLoggingLevel() {
		return currentLoggingLevel;
	}
	
	public static void setContext(Context context) {
		mContext = context;
	}
	
	/**
	 * Set's the logging level of the library. 
	 * Default is 6 - logs only errors. 
	 * @param level
	 */
	public static void setLoggingLevel(int level) {
		synchronized(LOG_LOCK) {
			currentLoggingLevel = level;
		}
	}
	
	/**
	 * Returns the set context. 
	 * @return -  the set context
	 */
	public static Context getContext() {
		return mContext;
	}
	
	/**
	 * This methods initializes the library. It first checks to see if the 
	 * device is already registered. If it is, it calls the onSuccess()
	 * method. Otherwise, it tries to register the device with the server and calls
	 * onSuccess() or onError() accordingly. 
	 * @param context - the Android context. 
	 * @param initCallback - the Initialization callback. 
	 */
	public static void initialize(Context context, final InitializationCallback initCallback) {
		mContext = context.getApplicationContext();
		/* *** initialization routines *** */
		// we will target our sandbox URL.
		logDebug(TAG, "Initializing library");
		ConnectionManager.setNetworkFactory(new AndroidNetworkFactory(Constants.AVP_SB_BASE_URL));
		// we will use a SQLITE database to persist attributes. 
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistence());
		// set the authentication strategy to level0 device authentication 
		AuthenticationManager.getInstance().setDeviceAuthenticationStrategy(new Level0DeviceAuthentication());
		// we will use our access token strategy that depends on level 0 device authentication
		AuthenticationManager.getInstance().setAccessTokenStrategy(new AVSAccessTokenStrategy());
		logDebug(TAG, "Checking to see if device is registered.");
		WorkQueueManager.getInstance().addWorkerToQueue(new WorkQueueManager.Worker() {
			@Override
			public void execute() {
				DeviceRegistrar.setDeviceRegistrationDelegate(new AVSDeviceRegistration());
				DeviceRegistrar.registerDevice(initCallback);
			}
		});
	}
	
	
	private static void log(int logLevel, String tag, String message, Throwable throwable) {
		if(logLevel >= currentLoggingLevel) {
			if (throwable == null) {
				Log.println(logLevel, tag, message);
			} else {
				Log.println(logLevel, tag, (new StringBuilder()).append(message).append("\n").append(throwable.getMessage()).toString());
			}
		}
	}
	
	/**
	 * Log - information messages
	 * @param tag
	 * @param message
	 */
	public static void logInfo(String tag, String message) {
		logInfo(tag, message, null);
	}
	
	/**
	 * Log - information messages
	 * @param tag
	 * @param message
	 * @param throwable
	 */
	public static void logInfo(String tag, String message, Throwable throwable) {
		log(4, tag, message, throwable);
	}
	
	/**
	 * Log - errors
	 * @param tag
	 * @param message
	 */
	public static void logError(String tag, String message) {
		logError(tag, message, null);
	}
	
	/**
	 * Log - errors
	 * @param tag
	 * @param message
	 * @param throwable
	 */
	public static void logError(String tag, String message, Throwable throwable) {
		log(6, tag, message, throwable);
	}
	
	/**
	 * Log - warnings
	 * @param tag
	 * @param message
	 */
	public static void logWarn(String tag, String message) {
		logWarn(tag, message, null);
	}
	
	/**
	 * Log - warnings 
	 * @param tag
	 * @param message
	 * @param throwable
	 */
	public static void logWarn(String tag, String message, Throwable throwable) {
		log(5, tag, message, throwable);
	}
	
	/**
	 * Log - debug messages
	 * @param tag
	 * @param message
	 */
	public static void logDebug(String tag, String message) {
		logDebug(tag, message, null);
	}
	
	/**
	 * Log - debug messages
	 * @param tag
	 * @param message
	 * @param throwable
	 */
	public static void logDebug(String tag, String message, Throwable throwable) {
		log(3, tag, message, throwable);
	}
	
	/**
	 * Log - detailed messages
	 * @param tag
	 * @param message
	 */
	public static void logVerbose(String tag, String message) {
		logVerbose(tag, message, null);
	}
	
	/**
	 * Log - detailed messages
	 * @param tag
	 * @param message
	 * @param throwable
	 */
	public static void logVerbose(String tag, String message, Throwable throwable) {
		log(2, tag, message, throwable);
	}
	
	/**
	 * Returns an attribute bundle signed by the AVS server 
	 * for a set of verified attributes.
	 * @param clientId the client making the request
	 * @param state the sate parameter if present
	 * @param attributeBundleMap the attribute map. The key of the map is set as the key in the response
	 * object
	 * @param callback callback method provides the response once done. 
	 * @throws IllegalArgumentException 
	 */
	public static void getVerifiedAttributeBundle(final String clientId, final String state, final Map<String, AbstractAttribute<?>> attributeBundleMap, 
		final VerifiedAttributeBundleCallback callback) throws IllegalArgumentException{
		if(clientId == null || clientId.isEmpty()) {
			throw new IllegalArgumentException("Client ID must be provided");
		}
		if(attributeBundleMap == null || attributeBundleMap.size() == 0) {
			throw new IllegalArgumentException("Attribute bundle is null or of size 0");
		}

		WorkQueueManager.getInstance().addWorkerToQueue(new Worker() {
			
			@Override
			public void execute() {
				AVSServer.bundleVerifiedAttributes(clientId, state, attributeBundleMap, callback); 
			}
		});
	}
	
	/**
	 * 
	 * Callback interface to get the verified attribute bundle
	 *
	 */
	public static interface VerifiedAttributeBundleCallback {
		
		/**
		 * Called when the verifiedResponse is successfully received
		 * from the AVS server.
		 * @param verifiedResponse the base-64 encoded response 
		 * from the AVS server
		**/
		public void onSuccess(String verifiedResponse);
		
		/**
		 * Called when a error occurs either internally or sent
		 * by the server. 
		 * @param exception MIDaaSException 
		 */
		public void onError(MIDaaSException exception);
	}
}
