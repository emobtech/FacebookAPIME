/*
 * RevokeAuthorization.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.emobtech.facebook.api.Response;
import com.twitterapime.io.HttpRequest;

/**
 * <p>
 * This class defines a request that revokes the user's authorization granted to
 * the application to access his account. This request invalidate the access
 * token, so it cannot be used any more.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class RevokeAuthorization implements Request {
	/**
	 * @see com.emobtech.facebook.api.Request#process(java.lang.String)
	 */
	public Response process(String accessToken) throws IOException {
		String url =
			"https://api.facebook.com/method/auth.revokeAuthorization?" +
			"access_token=" + accessToken;
		//
		HttpRequest req = new HttpRequest(url);
		//
		try {
			req.send();
		} finally {
			req.close();
		}
		//
		return null;
	}
}
