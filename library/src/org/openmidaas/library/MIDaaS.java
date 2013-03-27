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

import java.util.List;

import org.openmidaas.library.common.Constants;
import org.openmidaas.library.common.network.AndroidNetworkFactory;
import org.openmidaas.library.common.network.ConnectionManager;
import org.openmidaas.library.model.Level0Authentication;
import org.openmidaas.library.model.DeviceAttribute;
import org.openmidaas.library.model.core.DeviceRegistration;
import org.openmidaas.library.model.core.DeviceTokenCallback;
import org.openmidaas.library.model.core.InitializationCallback;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.model.core.PersistenceCallback;
import org.openmidaas.library.persistence.AttributeDBPersistenceDelegate;
import org.openmidaas.library.persistence.AttributePersistenceCoordinator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Class that controls device registration
 */
public final class MIDaaS{
	
	private static int currentLoggingLevel = 6;
	public static final int LOG_LEVEL_VERBOSE = 2;
	public static final int LOG_LEVEL_DEBUG = 3;
	public static final int LOG_LEVEL_INFO = 4;
	public static final int LOG_LEVEL_WARN = 5;
	public static final int LOG_LEVEL_ERROR = 6;
	private static final Object LOG_LOCK = new Object();
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
		ConnectionManager.setNetworkFactory(new AndroidNetworkFactory(Constants.AVP_SB_BASE_URL));
		AttributePersistenceCoordinator.setPersistenceDelegate(new AttributeDBPersistenceDelegate());
		AttributePersistenceCoordinator.getDeviceAttribute(new DeviceTokenCallback() {

			@Override
			public void onSuccess(List<DeviceAttribute> list) {
				if (list.isEmpty()) {
					initCallback.onRegistering();
					DeviceRegistration registration = new DeviceRegistration(new Level0Authentication());
					registration.registerDevice(initCallback);
				} else {
					initCallback.onSuccess();
				}
				
			}

			@Override
			public void onError(MIDaaSException exception) {
				initCallback.onError(exception);
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
}
