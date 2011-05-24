/*
 * DispatcherListener.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api;

/**
 * <p>
 * This interface defines all the events that can be triggered during the
 * sending process of a request.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see Dispatcher
 */
public interface DispatcherListener {
	/**
	 * <p>
	 * Called when a request is processed successfully.
	 * </p>
	 * @param request Request processed.
	 * @param response Request's response.
	 */
	public void onComplete(Request request, Response response);
	
	/**
	 * <p>
	 * Called when a request fails to processed.
	 * </p>
	 * @param request Request failed.
	 * @param error Error.
	 */
	public void onFail(Request request, Throwable error);
}
