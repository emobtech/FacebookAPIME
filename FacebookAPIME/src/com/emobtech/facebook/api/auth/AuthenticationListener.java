package com.emobtech.facebook.api.auth;

/**
 * <p>
 * Triggers the events related to authentication process of login page.
 * </p>
 * @author ernandes@gmail.com
 */
public interface AuthenticationListener {
	/**
	 * <p>
	 * Called when the user has authorized the application.
	 * </p>
	 * @param accessToken Access token.
	 */
	public void onAuthorize(String accessToken);
	
	/**
	 * <p>
	 * Called when the user has denied access to the application.
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
