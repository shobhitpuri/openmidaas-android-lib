/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.common;

import java.util.Vector;

import org.openmidaas.library.MIDaaS;

/**
 *
 * Queue manager class
 *
 */
public class WorkQueueManager {
	
	public interface Worker {
		public void execute();
	}
	
	private final String TAG = "WorkQueueManager";
	
	private Vector<Worker> workQueue;
	
	private Thread queueThread = null;
	
	private static WorkQueueManager mInstance = null;
	
	private boolean isStopRequested = false;

	private WorkQueueManager() {
		MIDaaS.logDebug(TAG, "creating new instance of work queue manager");
		workQueue = new Vector<Worker>();
		queueThread = new Thread(new Runnable() {

			@Override
			public void run() {
				MIDaaS.logDebug(TAG, "starting to process queue");
				startProcessingQueue();
			}			
		});
		queueThread.setName("WorkQueueThread");
		queueThread.start();
	}
	
	public static synchronized WorkQueueManager getInstance() {
		if(mInstance == null) {
			mInstance = new WorkQueueManager();
		}
		return mInstance;
	}
	
	public void addWorkerToQueue(Worker worker) {
		try {
			synchronized(workQueue) {
				MIDaaS.logDebug(TAG, "adding new worker to queue");
				workQueue.add(worker);
				workQueue.notify();
			}
		} catch(Exception e) {
			MIDaaS.logError(TAG, e.getMessage());
		}
	}
	
	private void startProcessingQueue() {
		while(!isStopRequested) {
			try {
				Worker worker = null;
				synchronized(workQueue) {
					while(workQueue.isEmpty()) {
						MIDaaS.logDebug(TAG, "waiting for work...");
						workQueue.wait();
					}
					if(isStopRequested) {
						MIDaaS.logDebug(TAG, "stopping work queue...");
						break;
					}
					worker = (Worker)workQueue.elementAt(0);
					workQueue.removeElementAt(0);
				}
				MIDaaS.logDebug(TAG, "doing work...");
				worker.execute();
				
			} catch(InterruptedException intException) {
				break;
			} catch(Exception e) {
				MIDaaS.logError(TAG, e.getMessage());
			}
			MIDaaS.logDebug(TAG, "done work...");
		}
	}
	
	public synchronized void terminateWorkQueue() {
		MIDaaS.logDebug(TAG, "stopping work queue...");
		isStopRequested = true;
		workQueue.notify();
	}
}
