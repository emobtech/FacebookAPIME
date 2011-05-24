/*
 * AuthenticationListener.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth;

import com.emobtech.facebook.api.auth.ui.AuthDialogWrapper;

/**
 * <p>
 * This interface defines all the events that can be triggered during the
 * authentication process.
 * </p>
 * <p>
 * Implement this interface in order to retrieve the access token, which will be
 * used to sign the requests to Facebook.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see AuthDialogWrapper
 */
public interface AuthenticationListener {
	/**
	 * <p>
	 * Called when the user has authorized the application to access his
	 * Facebook account.
	 * </p>
	 * @param accessToken Access token.
	 */
	public void onAuthorize(String accessToken);
	
	/**
	 * <p>
	 * Called when the user has denied access to his Facebook account by the
	 * application.
	 * </p>
	 * @param message Message.
	 */
	public void onAccessDenied(String message);
	
	/**
	 * <p>
	 * Called when an unknown error occurs during authentication process.
	 * </p>
	 * @param error Error.
	 * @param message Message.
	 */
	public void onFail(String error, String message);
}
