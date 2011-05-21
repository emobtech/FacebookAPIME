package com.emobtech.facebook.api.auth.ui.lwuit;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthenticationDialog;

/**
 * @author 82177082315
 *
 */
public final class AuthenticationDialogHTMLComponent
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
	 * Login listener object.
	 * </p>
	 */
	private AuthenticationListener loginListener;

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
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#addLoginDialogListener(com.emobtech.facebook.api.auth.AuthenticationListener)
	 */
	public void addLoginDialogListener(AuthenticationListener listener) {
		loginListener = listener;
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#login()
	 */
	public void login() {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#logout()
	 */
	public void logout() {
	}
}
