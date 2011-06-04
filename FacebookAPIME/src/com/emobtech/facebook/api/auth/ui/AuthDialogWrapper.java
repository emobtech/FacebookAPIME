/*
 * AuthDialogWrapper.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth.ui;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.emobtech.facebook.api.auth.AccessToken;
import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.Permission;
import com.emobtech.facebook.api.auth.RevokeAuthorization;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines a wrapper for a browser component, which will hold the 
 * authentication process to Facebook. It is responsible for informing to
 * Facebook all the parameters about the application that is trying to get 
 * access to a given user account.
 * </p>
 * <p>
 * In addition, this class also tracks the process in order to identify when the 
 * authorization is granted or denied, so it can notify the application. This 
 * notification is done throught a listener object that implements 
 * {@link AuthenticationListener}. 
 * </p>
 * <p>
 * Before using this class, the developer must register an app on Facebook
 * Developer website.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public abstract class AuthDialogWrapper {
	/**
	 * <p>
	 * App Id.
	 * </p>
	 */
	protected String appId;

	/**
	 * <p>
	 * App Secret.
	 * </p>
	 */
	protected String appSecret;

	/**
	 * <p>
	 * Redirect Uri.
	 * </p>
	 */
	protected String redirectUri;
	
	/**
	 * <p>
	 * Array of permissions.
	 * </p>
	 */
	protected String[] permissions;
	
	/**
	 * <p>
	 * Authencation listeners.
	 * </p>
	 */
	protected Vector authListeners;
	
	/**
	 * <p>
	 * Create an instance of AccessToken class.
	 * </p>
	 * @param appId App id.
	 * @param appSecret App secret.
	 * @param redirectUri Redirect uri.
	 * @param permissions Array of permissions.
	 * @param authListener Auth listener.
	 */
	protected AuthDialogWrapper(String appId, String appSecret,
		String redirectUri, String[] permissions, 
		AuthenticationListener authListener) {
		//
		setAppId(appId);
		setAppSecret(appSecret);
		setRedirectUri(redirectUri);
		setPermissions(permissions);
		//
		authListeners = new Vector();
		if (authListener != null) {
			authListeners.addElement(authListener);
		}
	}

	/**
	 * <p>
	 * Set the App id.
	 * </p>
	 * @param appId Id.
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * <p>
	 * Set the App secret.
	 * </p>
	 * @param appSecret Secret.
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * <p>
	 * Set the Redirect uri.
	 * </p>
	 * @param redirectUri Uri.
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * <p>
	 * Set the array of permissions.
	 * </p>
	 * @param permissions Permissions.
	 * @see Permission
	 */
	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	/**
	 * <p>
	 * Add an authentication listener.
	 * </p>
	 * @param listener Listener.
	 */
	public void addAuthenticationListener(AuthenticationListener listener) {
		if (!authListeners.contains(listener)) {
			authListeners.addElement(listener);
		}
	}
	
	/**
	 * <p>
	 * Remove the given authentication listener.
	 * </p>
	 * @param listener Listener.
	 */
	public void removeAuthenticationListener(AuthenticationListener listener) {
		if (authListeners.contains(listener)) {
			authListeners.removeElement(listener);
		}
	}

	/**
	 * <p>
	 * Load the Facebook's login page, so the user can enter his credentials.
	 * </p>
	 * @throws IllegalArgumentException If App id, secret or uri is null.
	 */
	public void login() {
		if (StringUtil.isEmpty(appId)) {
			throw new IllegalArgumentException("App ID must not be null.");
		}
		if (StringUtil.isEmpty(appSecret)) {
			throw new IllegalArgumentException("App Secret must not be null.");
		}
		if (StringUtil.isEmpty(redirectUri)) {
			throw new IllegalArgumentException("RedirectURI must not be null.");
		}
		//
		StringBuffer scope = new StringBuffer();
		if (permissions != null) {
			for (int i = 0; i < permissions.length; i++) {
				if	(i > 0) {
					scope.append(',');
				}
				scope.append(permissions[i]);
			}
		}
		//
		String url =
			"http://www.facebook.com/dialog/oauth?client_id=" + appId +
			"&redirect_uri=" + redirectUri +
			"&display=" + getDisplayParamValue() + 
			"&scope=" + scope.toString();
		//
		loadUrl(url);
	}
	
	/**
	 * <p>
	 * Logout the user from the browser component and loads the Facebook's
	 * login page.
	 * </p>
	 * <p>
	 * This method does not revoke any  authorization granted by the user to the
	 * application. For that, use {@link RevokeAuthorization} class.
	 * </p>
	 */
	public void logout() {
		final String redirect = "http://www.facebook.com";
		//
		loadUrl("http://m.facebook.com/logout.php?confirm=1&next=" + redirect);
	}
	
	/**
	 * <p>
	 * Load the given Url in the browser component.
	 * </p>
	 * @param url Url.
	 */
	protected abstract void loadUrl(String url);
	
	/**
	 * <p>
	 * Load the given HTML content in the browser component.
	 * </p>
	 * @param htmlContent HTML content.
	 */
	protected abstract void loadHTML(String htmlContent);

	/**
	 * <p>
	 * Return the display parameter's value that specifies that type of 
	 * interface on which the Facebook's login page will be presented.
	 * </p>
	 * @return Display.
	 */
	protected String getDisplayParamValue() {
		return "wap";
	}
	
	/**
	 * <p>
	 * Track the given Url in order to identify any event related to the
	 * authentication process.
	 * </p>
	 * @param url Url.
	 */
	protected void trackCodeUrl(String url) {
		if (url.startsWith(redirectUri)	&& !authListeners.isEmpty()) {
			//
			if (url.indexOf("code=") != -1) {
				displayAuthSuccessPage();
				//
				try {
					String code = AccessToken.getUrlParamValue(url, "code");
					AccessToken tokenReq =
						new AccessToken(appId, appSecret, redirectUri);
					AccessToken.Response r =
						(AccessToken.Response)tokenReq.process(code);
					//
					triggerOnAuthorize(r.getToken());
				} catch (IOException e) {
					triggerOnFail("error_access_token", e.getMessage());
				}
			} else if (url.indexOf("error_reason=") != -1) {
				String err = AccessToken.getUrlParamValue(url, "error_reason");
				String msg =
					AccessToken.getUrlParamValue(url, "error_description");
				//
				if ("user_denied".equals(err)) {
					displayAuthDeniedPage(msg);
					triggerOnAccessDenied(msg);
				} else {
					displayAuthErrorPage(err, msg);
					triggerOnFail(err, msg);
				}
			} else {
				throw new IllegalStateException("Condition not expected.");
			}
		}
	}
	
	/**
	 * <p>
	 * Display the Authorization success page. Displayed when the authorization
	 * is granted.
	 * </p>
	 */
	protected void displayAuthSuccessPage() {
		String html =
			"<html><body>" +
			"<center><font color=\"blue\"><b>Facebook</b></font></center><br/>"+
			"<center>Authorization granted!<br/><br/>Close this page.</center>"+ 
			"</body></html>";
		//
		loadHTML(html);
	}
	
	/**
	 * <p>
	 * Display the Authorization denied page. Displayed when the authorization
	 * is denied.
	 * </p>
	 * @param message Message.
	 */
	protected void displayAuthDeniedPage(String message) {
		String html =
			"<html><body>" +
			"<center><font color=\"blue\"><b>Facebook</b></font></center><br/>"+
			"<center>Authorization denied: " + message + 
			"<br/><br/>Close this page.</center>" + 
			"</body></html>";
		//
		loadHTML(html);
	}

	/**
	 * <p>
	 * Display the Authorization error page. Displayed when the authorization
	 * fails.
	 * </p>
	 * @param error Error.
	 * @param message Message.
	 */
	protected void displayAuthErrorPage(String error, String message) {
		String html =
			"<html><body>" +
			"<center><font color=\"blue\"><b>Facebook</b></font></center><br/>"+
			"<center>Authorization failed: " + message + " (" + error + ")" + 
			"<br/><br/>Close this page.</center>" + 
			"</body></html>";
		//
		loadHTML(html);
	}

	/**
	 * <p>
	 * Trigger authentication listeners about on authorize event.
	 * </p>
	 * @param accessToken Access token.
	 */
	protected void triggerOnAuthorize(String accessToken) {
		Enumeration listeners = authListeners.elements();
		//
		while (listeners.hasMoreElements()) {
			AuthenticationListener listener =
				(AuthenticationListener)listeners.nextElement();
			//
			listener.onAuthorize(accessToken);
		}
	}

	/**
	 * <p>
	 * Trigger authentication listeners about on fail event.
	 * </p>
	 * @param error Error.
	 * @param message Message.
	 */
	protected void triggerOnFail(String error, String message) {
		Enumeration listeners = authListeners.elements();
		//
		while (listeners.hasMoreElements()) {
			AuthenticationListener listener =
				(AuthenticationListener)listeners.nextElement();
			//
			listener.onFail(error, message);
		}
	}
	
	/**
	 * <p>
	 * Trigger authentication listeners about on access denied event.
	 * </p>
	 * @param message Message.
	 */
	protected void triggerOnAccessDenied(String message) {
		Enumeration listeners = authListeners.elements();
		//
		while (listeners.hasMoreElements()) {
			AuthenticationListener listener =
				(AuthenticationListener)listeners.nextElement();
			//
			listener.onAccessDenied(message);
		}
	}
}
