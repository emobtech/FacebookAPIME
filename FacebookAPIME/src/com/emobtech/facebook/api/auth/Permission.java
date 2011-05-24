/*
 * Permission.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth;


/**
 * <p>
 * This class defines some contants that represents the access permissions for
 * each type of content provided by Facebook. Some requests will not work if
 * the user has granted permission for that.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Permission {
	/**
	 * <p>
	 * This constant represents the permission to post anything on user's wall.
	 * </p>
	 */
	public static final String PUBLISH_STREAM = "publish_stream";
	
	/**
	 * <p>
	 * This constant represents the permission to keep the access token alive
	 * during multiple sessions. In other words, the token will not expire.
	 * </p>
	 */
	public static final String OFFLINE_ACCESS = "offline_access";
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Permission() {
	}
}
