package com.emobtech.facebook.api.auth.ui.lwuit;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.emobtech.facebook.api.auth.AccessToken;
import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthenticationDialog;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.html.HTMLCallback;
import com.sun.lwuit.html.HTMLComponent;
import com.twitterapime.util.StringUtil;

/**
 * @author 82177082315
 *
 */
public final class AuthenticationDialogHTMLComponent extends HTMLComponent
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
	 * @param handler
	 * @param appId
	 * @param appSecret
	 * @param redirectUri
	 * @param permissions
	 * @param authListener
	 */
	public AuthenticationDialogHTMLComponent(String appId, String appSecret, String redirectUri,
		String[] permissions, AuthenticationListener authListener) {
		super(new HttpRequestHandler());
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
		setIgnoreCSS(true);
		setHTMLCallback(new AuthenticationDialogHTMLCallback());
	}

	/**
	 * @param handler
	 */
	public AuthenticationDialogHTMLComponent() {
		this(null, null, null, null, null);
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
		setPage(url);
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#logout()
	 */
	public void logout() {
		final String redirect = "http://www.facebook.com";
		//
		setPage("http://m.facebook.com/logout.php?confirm=1&next=" + redirect);
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
	 * @author Ernandes Jr
	 */
	private class AuthenticationDialogHTMLCallback implements HTMLCallback {
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
			if (phase == HTMLCallback.STATUS_COMPLETED
					&& url.startsWith(redirectUri)
					&& !authListeners.isEmpty()) {
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
