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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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

	private static final long POLL_TIMEOUT_MS = 150;
	
	private final String TAG = "WorkQueueManager";
	
	private BlockingQueue<Worker> workQueue;
	
	private Thread queueThread = null;
	
	private static WorkQueueManager mInstance = null;
	
	private boolean isStopRequested = false;

	private WorkQueueManager() {
		MIDaaS.logDebug(TAG, "creating new instance of work queue manager");
		workQueue = new LinkedBlockingQueue<Worker>();
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
			MIDaaS.logDebug(TAG, "adding new worker to queue");
			workQueue.add(worker);
		} catch(Exception e) {
			MIDaaS.logError(TAG, e.getMessage());
		}
	}
	
	private void startProcessingQueue() {
		while(!isStopRequested) {
			try {
				// wait till the timeout
				Worker worker = workQueue.poll(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
				// first check if stop was requested
				if(isStopRequested) {
					MIDaaS.logDebug(TAG, "stopping work queue...");
					break;
				}
				// if worker is null (nothing was added to the queue, continue)
				if(worker == null) {
					continue;
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
	
	/**
	 * Terminates the current work queue
	 */
	public synchronized void terminateWorkQueue() {
		MIDaaS.logDebug(TAG, "stopping work queue...");
		isStopRequested = true;
		// add a dummy worker to notify the queue that something is added. 
		addWorkerToQueue(new Worker() {

			@Override
			public void execute() {
			}
			
		});
	}
}
