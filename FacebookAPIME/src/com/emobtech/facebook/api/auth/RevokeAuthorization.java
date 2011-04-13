package com.emobtech.facebook.api.auth;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.emobtech.facebook.api.Response;
import com.twitterapime.io.HttpRequest;

/**
 * <p>
 * Represents a logout operation.
 * </p>
 * @author ernandes@gmail.com
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
