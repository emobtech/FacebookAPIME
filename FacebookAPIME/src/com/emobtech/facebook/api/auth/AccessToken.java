package com.emobtech.facebook.api.auth;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.util.StringUtil;

/**
 * @author 82177082315
 *
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
	 * @param code
	 * @param appId
	 * @param appSecret
	 * @param redirectUri
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
	 * <p>
	 * Processes the request.
	 * </p>
	 * @param code Authorization code returned by Login Dialog.
	 * @return Result of request.
	 * @throws IOException If any I/O error occurs during processing.
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
	 * </p>
	 * @author 82177082315
	 */
	public static class Response implements com.emobtech.facebook.api.Response {
		/**
		 * <p>
		 * </p>
		 */
		private String token;
		
		/**
		 * @param token
		 */
		public Response(String token) {
			this.token = token;
		}
		
		/**
		 * @return
		 */
		public String getToken() {
			return token;
		}
	}
}
