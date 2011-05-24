/*
 * HTMLComponentAutDialogWrapper.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth.ui.lwuit;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthDialogWrapper;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.html.HTMLCallback;
import com.sun.lwuit.html.HTMLComponent;

/**
 * <p>
 * This class defines a wrapper for a HTMLComponent component, which will hold
 * the authentication process to Facebook. It is responsible for informing to
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
public final class HTMLComponentAutDialogWrapper extends AuthDialogWrapper {
	/**
	 * <p>
	 * HTMLComponent object.
	 * </p>
	 */
	private HTMLComponent htmlComponent;
	
	/**
	 * <p>
	 * Create an instance of HTMLComponentAutDialogWrapper class.
	 * </p>
	 * @param htmlComponent HTMLComponent to display Facebook's login page.
	 * @param appId App id.
	 * @param appSecret App secret.
	 * @param redirectUri Redirect uri.
	 * @param permissions Array of permissions.
	 * @param authListener Auth listener.
	 */
	public HTMLComponentAutDialogWrapper(HTMLComponent htmlComponent,
		String appId, String appSecret, String redirectUri,	
		String[] permissions, AuthenticationListener authListener) {
		super(appId, appSecret, redirectUri, permissions, authListener);
		//
		if (htmlComponent == null) {
			throw new IllegalArgumentException(
				"HTML Component must not be null.");
		}
		//
		this.htmlComponent = htmlComponent;
		this.htmlComponent.setHTMLCallback(new HTMLCallbackAuth());
	}

	/**
	 * <p>
	 * Create an instance of HTMLComponentAutDialogWrapper class.
	 * </p>
	 * @param htmlComponent HTMLComponent to display Facebook's login page.
	 */
	public HTMLComponentAutDialogWrapper(HTMLComponent htmlComponent) {
		this(htmlComponent, null, null, null, null, null);
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		htmlComponent.setPage(url);
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class HTMLCallbackAuth implements HTMLCallback {
		/**
		 * @see com.sun.lwuit.html.HTMLCallback#fieldSubmitted(com.sun.lwuit.html.HTMLComponent, com.sun.lwuit.TextArea, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
		 */
		public String fieldSubmitted(HTMLComponent htmlC, TextArea ta,
			String actionURL, String id, String value, int type, 
			String errorMsg) {
	        return value;
		}

		/**
		 * @see com.sun.lwuit.html.HTMLCallback#getAutoComplete(com.sun.lwuit.html.HTMLComponent, java.lang.String, java.lang.String)
		 */
		public String getAutoComplete(HTMLComponent htmlC, String actionURL,
			String id) {
			return null;
		}

		/**
		 * @see com.sun.lwuit.html.HTMLCallback#getLinkProperties(com.sun.lwuit.html.HTMLComponent, java.lang.String)
		 */
		public int getLinkProperties(HTMLComponent htmlC, String url) {
			return HTMLCallback.LINK_REGULAR;
		}

		/**
		 * @see com.sun.lwuit.html.HTMLCallback#linkClicked(com.sun.lwuit.html.HTMLComponent, java.lang.String)
		 */
		public boolean linkClicked(HTMLComponent htmlC, String url) {
			return true;
		}

		/**
		 * @see com.sun.lwuit.html.HTMLCallback#pageStatusChanged(com.sun.lwuit.html.HTMLComponent, int, java.lang.String)
		 */
		public void pageStatusChanged(HTMLComponent htmlC, int phase,
			String url) {
			if (phase == HTMLCallback.STATUS_COMPLETED) {
				trackCodeUrl(url);
			}
		}

		/**
		 * @see com.sun.lwuit.html.HTMLCallback#parsingError(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
		 */
		public boolean parsingError(int errorId, String tag, String attribute,
			String value, String description) {
			return true;
		}

		/**
		 * @see com.sun.lwuit.html.HTMLCallback#titleUpdated(com.sun.lwuit.html.HTMLComponent, java.lang.String)
		 */
		public void titleUpdated(HTMLComponent htmlC, String title) {
		}
	}
}
