/*
 * BrowserAuthDialogWrapper.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth.ui.swt;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthDialogWrapper;

/**
 * <p>
 * This class defines a wrapper for a Browser component, which will hold
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
public final class BrowserAuthDialogWrapper extends AuthDialogWrapper {
	/**
	 * <p>
	 * Browser object.
	 * </p>
	 */
	private Browser browser;
	
	/**
	 * <p>
	 * Ignore further events when authentication is complete.
	 * </p>
	 */
	private boolean ignoreEvents;

	/**
	 * <p>
	 * Create an instance of BrowserAuthDialogWrapper class.
	 * </p>
	 * @param browser Browser to display Facebook's login page.
	 * @param appId App id.
	 * @param appSecret App secret.
	 * @param redirectUri Redirect uri.
	 * @param permissions Array of permissions.
	 * @param authListener Auth listener.
	 */
	public BrowserAuthDialogWrapper(Browser browser,
		String appId, String appSecret, String redirectUri,
		String[] permissions, AuthenticationListener authListener) {
		super(appId, appSecret, redirectUri, permissions, authListener);
		//
		if (browser == null) {
			throw new IllegalArgumentException("Browser must not be null.");
		}
		//
		this.browser = browser;
		this.browser.addProgressListener(new ProgressListenerAuth());
	}

	/**
	 * <p>
	 * Create an instance of BrowserAuthDialogWrapper class.
	 * </p>
	 * @param browser Browser to display Facebook's login page.
	 */
	public BrowserAuthDialogWrapper(Browser browser) {
		this(browser, null, null, null, null, null);
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		browser.setUrl(url);
		browser.refresh();
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthDialogWrapper#loadHTML(java.lang.String)
	 */
	protected void loadHTML(String htmlContent) {
		browser.setText(htmlContent);
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class ProgressListenerAuth implements ProgressListener {
		/**
		 * @see org.eclipse.swt.browser.ProgressListener#changed(org.eclipse.swt.browser.ProgressEvent)
		 */
		public void changed(ProgressEvent event) {
			String url = browser.getUrl();
			//
			if (!ignoreEvents
					&& !authListeners.isEmpty()
					&& url.startsWith(redirectUri)) {
				ignoreEvents = true;
				//
				trackCodeUrl(url);
			}
		}

		/**
		 * @see org.eclipse.swt.browser.ProgressListener#completed(org.eclipse.swt.browser.ProgressEvent)
		 */
		public void completed(ProgressEvent event) {
		}
	}
}
