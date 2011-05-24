/*
 * Dispatcher.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class is responsible for dispatching requests to Facebook. A Dispatcher 
 * object must be associated to given Access Token, which represents a given
 * user account.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Dispatcher {
	/**
	 * <p>
	 * Pool of Dispatcher objects. Only one object per access token.
	 * </p>
	 */
	private static Hashtable dispatcherPool;
	
	/**
	 * <p>
	 * Task to process the requests.
	 * </p>
	 */
	private Runnable task;
	
	/**
	 * <p>
	 * Access token.
	 * </p>
	 */
	private String accessToken;
	
	/**
	 * <p>
	 * Queue of requests.
	 * </p>
	 */
	private Vector queue;
	
	/**
	 * <p>
	 * Dispatcher listeners.
	 * </p>
	 */
	private Vector dispListeners;
	
	/**
	 * <p>
	 * Create a Dispatcher object associated to the given Access Token.
	 * </p>
	 * @param accessToken Access token.
	 * @return Dispatcher object.
	 * @throws IllegalArgumentException If access token is null.
	 */
	public synchronized static Dispatcher getInstance(String accessToken) {
		if (StringUtil.isEmpty(accessToken)) {
			throw new IllegalArgumentException(
				"Access Token must not be null.");
		}
		//
		if (dispatcherPool == null) {
			dispatcherPool = new Hashtable();
		}
		//
		Dispatcher dispatcher = (Dispatcher)dispatcherPool.get(accessToken);
		//
		if (dispatcher == null) {
			dispatcher = new Dispatcher(accessToken);
			dispatcherPool.put(accessToken, dispatcher);
		}
		//
		return dispatcher;
	}
	
	/**
	 * <p>
	 * Create an instance of Dispatcher class.
	 * </p>
	 * @param accessToken Access token.
	 */
	private Dispatcher(String accessToken) {
		this.accessToken = accessToken;
		//
		queue = new Vector(5);
		dispListeners = new Vector();
		//
		task = new Runnable() {
			public void run() {
				while (true) {
					processQueue();
					//
					synchronized (this) {
						if (queue.size() == 0) {
							try {
								wait();
							} catch (InterruptedException e) {}
						}
					}
				}
			}
		};
		//
		new Thread(task).run();
	}
	
	/**
	 * <p>
	 * Dispatch a given request. This methods immediately dispatches the request
	 * to Facebook. Since it is a synchronous request, this method performs a 
	 * blocking operation.
	 * <p>
	 * For asynchronous requests, use {@link Dispatcher#addToQueue(Request)} 
	 * method.
	 * </p>
	 * @param request Request.
	 * @throws IOException If any I/O error accours. 
	 * @throws IllegalArgumentException If request is null.
	 */
	public Response dispatch(Request request) throws IOException {
		if (request == null) {
			throw new IllegalArgumentException("Request must not be null.");
		}
		//
		return process(request);
	}
	
	/**
	 * <p>
	 * Add a given request to a dispatching queue. This methods does not 
	 * immediately dispatches the request to Facebook. Actually, it is added to
	 * a queue, in order to be sent asynchronously.
	 * </p>
	 * <p>
	 * For synchronous requests, use {@link Dispatcher#dispatch(Request)}
	 * method.
	 * </p>
	 * @param request Request.
	 * @throws IllegalArgumentException If request is null.
	 */
	public void addToQueue(Request request) {
		if (request == null) {
			throw new IllegalArgumentException("Request must not be null.");
		}
		//
		synchronized (queue) {
			queue.addElement(request);
		}
		//
		synchronized (task) {
			task.notify();
		}
	}
	
	/**
	 * <p>
	 * Add a dispatcher listener.
	 * </p>
	 * @param listener Listener.
	 */
	public void addDispatcherListener(DispatcherListener listener) {
		if (!dispListeners.contains(listener)) {
			dispListeners.addElement(listener);
		}
	}
	
	/**
	 * <p>
	 * Remove the given dispatcher listener.
	 * </p>
	 * @param listener Listener.
	 */
	public void removeDispatcherListener(DispatcherListener listener) {
		if (dispListeners.contains(listener)) {
			dispListeners.removeElement(listener);
		}
	}
	
	/**
	 * <p>
	 * Processes all queued requests.
	 * </p>
	 */
	private void processQueue() {
		int size = 0;
		//
		do {
			synchronized (queue) {
				size = queue.size();
			}
			//
			if (size > 0) {
				Request req = (Request)queue.elementAt(0);
				//
				try {
					triggerOnComplete(req, process(req));
				} catch (Throwable e) {
					triggerOnFail(req, e);
				}
				//
				synchronized (queue) {
					queue.removeElementAt(0);
				}
			} else {
				break;
			}
		} while (true);
	}
	
	/**
	 * <p>
	 * Process a given request, in order to send the request to Facebook.
	 * </p>
	 * @param request Request.
	 * @return Request's response.
	 * @throws IOException If any I/O error accours.
	 */
	private Response process(Request request) throws IOException {
		return request.process(accessToken);
	}
	
	/**
	 * <p>
	 * Trigger dispatcher listeners about on complete event.
	 * </p>
	 * @param request Request completed.
	 * @param response Request's response.
	 */
	private void triggerOnComplete(Request request, Response response) {
		Enumeration listeners = dispListeners.elements();
		//
		while (listeners.hasMoreElements()) {
			DispatcherListener listener =
				(DispatcherListener)listeners.nextElement();
			//
			listener.onComplete(request, response);
		}
	}

	/**
	 * <p>
	 * Trigger dispatcher listeners about on fail event.
	 * </p>
	 * @param request Request failed.
	 * @param error Error.
	 */
	private void triggerOnFail(Request request, Throwable error) {
		Enumeration listeners = dispListeners.elements();
		//
		while (listeners.hasMoreElements()) {
			DispatcherListener listener =
				(DispatcherListener)listeners.nextElement();
			//
			listener.onFail(request, error);
		}
	}
}
