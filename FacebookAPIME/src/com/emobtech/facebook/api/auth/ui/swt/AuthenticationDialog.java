package com.emobtech.facebook.api.auth.ui.swt;

import java.io.IOException;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

import com.emobtech.facebook.api.auth.AccessToken;
import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.Permission;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Represents the Facebook's authentication page.
 * </p>
 * @author ernandes@gmail.com
 */
public final class AuthenticationDialog extends Browser {
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
	 * Login listener object.
	 * </p>
	 */
	private AuthenticationListener loginListener;
	
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
	 * Creates an instance of LoginPage.
	 * </p>
	 * @param composite Composite.
	 * @param layout Layout.
	 */
	public AuthenticationDialog(Composite composite, int layout) {
		super(composite, layout);
		//
		addProgressListener(new LoginDialogProgressListener());
	}
	
	/**
	 * <p>
	 * </p>
	 * @param appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * <p>
	 * </p>
	 * @param appSecret
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * <p>
	 * </p>
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * <p>
	 * </p>
	 * @param permissions
	 * @see Permission
	 */
	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	/**
	 * <p>
	 * Sets the login listener object.
	 * </p>
	 * @param listener Listener object.
	 */
	public void addLoginDialogListener(AuthenticationListener listener) {
		loginListener = listener;
	}

	/**
	 * <p>
	 * </p>
	 * @throws IllegalArgumentException
	 */
	public void login() {
		if (StringUtil.isEmpty(appId) || StringUtil.isEmpty(appSecret)) {
			throw new IllegalArgumentException();
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
	 * <p>
	 * </p>
	 */
	public void logout() {
		setUrl("http://m.facebook.com/logout.php?confirm=1&next=http://www.facebook.com");
		//
		refresh();
	}

	/**
	 * <p>
	 * </p>
	 * @author 82177082315
	 */
	private class LoginDialogProgressListener implements ProgressListener {
		/**
		 * @see org.eclipse.swt.browser.ProgressListener#changed(org.eclipse.swt.browser.ProgressEvent)
		 */
		public void changed(ProgressEvent event) {
			final String url = getUrl();
			//
			if (!ignoreEvents
					&& loginListener != null
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
						loginListener.onAuthorize(r.getToken());
					} catch (IOException e) {
						loginListener.onFail(
							"error_access_token", e.getMessage());
					}
				} else if (url.indexOf("error_reason=") != -1) {
					String err = getUrlParamValue(url, "error_reason");
					String msg = getUrlParamValue(url, "error_description");
					//
					if ("user_denied".equals(err)) {
						loginListener.onAccessDenied(msg);
					} else {
						loginListener.onFail(err, msg);
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
