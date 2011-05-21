package com.emobtech.facebook.api.auth.ui;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.Permission;

/**
 * <p>
 * Represents the definition of a Facebook's authentication dialog.
 * </p>
 * @author ernandes@gmail.com
 */
public interface AuthenticationDialog {
	/**
	 * <p>
	 * </p>
	 * @param appId
	 */
	public void setAppId(String appId);

	/**
	 * <p>
	 * </p>
	 * @param appSecret
	 */
	public void setAppSecret(String appSecret);

	/**
	 * <p>
	 * </p>
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri);

	/**
	 * <p>
	 * </p>
	 * @param permissions
	 * @see Permission
	 */
	public void setPermissions(String[] permissions);

	/**
	 * <p>
	 * Sets the login listener object.
	 * </p>
	 * @param listener Listener object.
	 */
	public void addLoginDialogListener(AuthenticationListener listener);

	/**
	 * <p>
	 * </p>
	 * @throws IllegalArgumentException
	 */
	public void login();

	/**
	 * <p>
	 * </p>
	 */
	public void logout();
}
