package com.emobtech.facebook.api.auth.ui.swt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

import com.emobtech.facebook.api.auth.AccessToken;
import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthenticationDialog;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Represents the Facebook's authentication page.
 * </p>
 * @author ernandes@gmail.com
 */
public final class AuthenticationDialogBrowser extends Browser
	implements AuthenticationDialog {
	/**
	 * <p>
	 * App Id.
	 * </p>
	 */
	private String appId;

	/**
	 * <p>
	 * App Secret.
	 * </p>
	 */
	private String appSecret;

	/**
	 * <p>
	 * Redirect Uri.
	 * </p>
	 */
	private String redirectUri;
	
	/**
	 * <p>
	 * </p>
	 */
	private String[] permissions;
	
	/**
	 * <p>
	 * </p>
	 */
	private Vector authListeners;
	
	/**
	 * <p>
	 * Ignore further events when authentication is complete.
	 * </p>
	 */
	private boolean ignoreEvents;
	
	/**
	 * <p>
	 * Returns the value of a given param in the Url.
	 * </p>
	 * @param url Url.
	 * @param param Parameter.
	 * @return Value.
	 */
	private static String getUrlParamValue(String url, String param) {
		int ix = url.indexOf('?');
		//
		if (ix != -1) {
			url = url.substring(ix +1);
		}
		//
		String[] params = StringUtil.split(url, '&');
		//
		for (int i = 0; i < params.length; i++) {
			if (params[i].startsWith(param + '=')) {
				return StringUtil.split(params[i], '=')[1];
			}
		}
		//
		return null;
	}

	/**
	 * <p>
	 * </p>
	 * @param composite
	 * @param layout
	 * @param appId
	 * @param appSecret
	 * @param redirectUri
	 * @param permissions
	 * @param authListener
	 */
	public AuthenticationDialogBrowser(Composite composite, int layout,
		String appId, String appSecret, String redirectUri,
		String[] permissions, AuthenticationListener authListener) {
		super(composite, layout);
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
		//
		addProgressListener(new AuthenticationDialogProgressListener());
	}

	/**
	 * <p>
	 * Creates an instance of LoginPage.
	 * </p>
	 * @param composite Composite.
	 * @param layout Layout.
	 */
	public AuthenticationDialogBrowser(Composite composite, int layout) {
		this(composite, layout, null, null, null, null, null);
		//
		addProgressListener(new AuthenticationDialogProgressListener());
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setAppId(java.lang.String)
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setAppSecret(java.lang.String)
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setRedirectUri(java.lang.String)
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setPermissions(java.lang.String[])
	 */
	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	/**
	 * <p>
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#addAuthenticationListener(com.emobtech.facebook.api.auth.AuthenticationListener)
	 */
	public void addAuthenticationListener(AuthenticationListener listener) {
		if (!authListeners.contains(listener)) {
			authListeners.addElement(listener);
		}
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#removeAuthenticationListener(com.emobtech.facebook.api.auth.AuthenticationListener)
	 */
	public void removeAuthenticationListener(AuthenticationListener listener) {
		if (authListeners.contains(listener)) {
			authListeners.removeElement(listener);
		}
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#login()
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
			"&display=wap" + 
			"&scope=" + scope.toString();
		//
		setUrl(url);
		//
		refresh();
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#logout()
	 */
	public void logout() {
		setUrl("http://m.facebook.com/logout.php?confirm=1&next=http://www.facebook.com");
		//
		refresh();
	}
	
	/**
	 * @param accessToken
	 */
	private void triggerOnAuthorize(String accessToken) {
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
	 * @param error
	 * @param message
	 */
	private void triggerOnFail(String error, String message) {
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
	 * @param message
	 */
	private void triggerOnAccessDenied(String message) {
		Enumeration listeners = authListeners.elements();
		//
		while (listeners.hasMoreElements()) {
			AuthenticationListener listener =
				(AuthenticationListener)listeners.nextElement();
			//
			listener.onAccessDenied(message);
		}
	}

	/**
	 * <p>
	 * </p>
	 * @author ernandes@gmail.com
	 */
	private class AuthenticationDialogProgressListener
		implements ProgressListener {
		/**
		 * @see org.eclipse.swt.browser.ProgressListener#changed(org.eclipse.swt.browser.ProgressEvent)
		 */
		public void changed(ProgressEvent event) {
			final String url = getUrl();
			//
			if (!ignoreEvents
					&& !authListeners.isEmpty()
					&& url.startsWith(redirectUri)) {
				ignoreEvents = true;
				//
				if (url.indexOf("code=") != -1) {
					try {
						String code = getUrlParamValue(url, "code");
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
					String err = getUrlParamValue(url, "error_reason");
					String msg = getUrlParamValue(url, "error_description");
					//
					if ("user_denied".equals(err)) {
						triggerOnAccessDenied(msg);
					} else {
						triggerOnFail(err, msg);
					}
				} else {
					throw new IllegalStateException("Condition not expected.");
				}
			}
		}

		/**
		 * @see org.eclipse.swt.browser.ProgressListener#completed(org.eclipse.swt.browser.ProgressEvent)
		 */
		public void completed(ProgressEvent event) {
		}
	}
}
