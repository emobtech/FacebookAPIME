/*
 * Picture.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.graph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.emobtech.facebook.api.Request;
import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines a request that retrieves a user's profile picture.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Picture implements Request {
	/**
	 * <p>
	 * User Id.
	 * </p>
	 */
	private String userId;
	
	/**
	 * <p>
	 * Create an instance of Picture class.
	 * </p>
	 * @param userId User Id.
	 * @throws IllegalArgumentException If user Id is null.
	 */
	public Picture(String userId) {
		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException("User Id must not be null.");
		}
		//
		this.userId = userId;
	}
	
	/**
	 * @see com.emobtech.facebook.api.Request#process(java.lang.String)
	 */
	public com.emobtech.facebook.api.Response process(String accessToken)
		throws IOException {
		String url = "https://graph.facebook.com/" + userId + "/picture";
		//
		HttpRequest req = null;
		//
		try {
			req = new HttpRequest(url);
			//
			HttpResponse resp = req.send();
			int rcode = resp.getCode();
			//
			if (rcode >= 300 && rcode < 400) { //redirected?
				url = resp.getResponseField("Location");
				req.close();
				//
				req = new HttpRequest(url);
				//
				resp = req.send();
				rcode = resp.getCode();
			}
			//
			if (rcode == HttpConnection.HTTP_OK) {
				InputStream in = resp.getStream();
				//
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
				//
				for (int n; (n = in.read(buffer)) > 0;) {
					out.write(buffer, 0, n);
				}
				//
				return new Response(out.toByteArray());
			} else {
				throw new IOException("HTTP: " + rcode);
			}
		} finally {
			if (req != null) {
				req.close();
			}
		}
	}
	
	/**
	 * <p>
	 * Picture's response class.
	 * </p>
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	public static class Response implements com.emobtech.facebook.api.Response {
		/**
		 * <p>
		 * Image data.
		 * </p>
		 */
		private byte[] data;
		
		/**
		 * <p>
		 * Create an instance of Result class.
		 * </p>
		 * @param data Image data.
		 */
		Response(byte[] data) {
			this.data = data;
		}
		
		/**
		 * <p>
		 * Return image data.
		 * </p>
		 * @return Data.
		 */
		public byte[] getData() {
			return data;
		}
	}
}
