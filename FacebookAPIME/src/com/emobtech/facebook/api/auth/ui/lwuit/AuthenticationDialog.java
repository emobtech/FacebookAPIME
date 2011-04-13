package com.emobtech.facebook.api.auth.ui.lwuit;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.Permission;

/**
 * @author 82177082315
 *
 */
public final class AuthenticationDialog {
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
}
