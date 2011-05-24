/*
 * AccessToken.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines a request that retrieves an access token. This token will
 * be used to sign all following requests for a given account to Facebook.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class AccessToken implements Request {
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
	 * Get the value of a given param in the Url.
	 * </p>
	 * @param url Url.
	 * @param param Parameter.
	 * @return Value.
	 */
	public static String getUrlParamValue(String url, String param) {
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
	 * Create an instance of AccessToken class.
	 * </p>
	 * @param code Authentication code.
	 * @param appId App id.
	 * @param appSecret App secret.
	 * @param redirectUri Redirect uri.
	 * @throws IllegalArgumentException If any parameter is empty.
	 */
	public AccessToken(String appId, String appSecret, String redirectUri) {
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
		this.appId = appId;
		this.appSecret = appSecret;
		this.redirectUri = redirectUri;
	}

	/**
	 * @see com.emobtech.facebook.api.Request#process(java.lang.String)
	 */
	public com.emobtech.facebook.api.Response process(String code)
		throws IOException {
		final String url =
			"https://graph.facebook.com/oauth/access_token?client_id=" + appId
				+ "&redirect_uri=" + redirectUri
				+ "&client_secret=" + appSecret
				+ "&code=" + code;
		//
		HttpRequest req = new HttpRequest(url);
		//
		try {
			HttpResponse res = req.send();
			code = getUrlParamValue(res.getBodyContent(), "access_token");
			//
			return new Response(code);
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * AccessToken's response class.
	 * </p>
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	public static class Response implements com.emobtech.facebook.api.Response {
		/**
		 * <p>
		 * Access Token.
		 * </p>
		 */
		private String token;
		
		/**
		 * <p>
		 * Create an instance of Response class.
		 * </p>
		 * @param token
		 */
		public Response(String token) {
			this.token = token;
		}
		
		/**
		 * <p>
		 * Get the access token.
		 * </p>
		 * @return Token.
		 */
		public String getToken() {
			return token;
		}
	}
}
