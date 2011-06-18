/*
 * Status.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.graph;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.emobtech.facebook.api.Response;
import com.emobtech.facebook.api.auth.Permission;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines a request that posts a status on wall.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Status implements Request {
	/**
	 * <p>
	 * Status message.
	 * </p>
	 */
	private String message;

	/**
	 * <p>
	 * Recipient Id.
	 * </p>
	 */
	private String toUserId;
	
	/**
	 * <p>
	 * Create an instance of Status class.
	 * </p>
	 * <p>
	 * Require {@link Permission#PUBLISH_STREAM}.
	 * </p>
	 * @param message Message.
	 * @param toUserId Recipient Id.
	 * @throws IllegalArgumentException if message is null.
	 */
	public Status(String message, String toUserId) {
		if (StringUtil.isEmpty(message)) {
			throw new IllegalArgumentException("Message must not be null.");
		}
		//
		this.message = message;
		this.toUserId = StringUtil.isEmpty(toUserId) ? "me" : toUserId;
	}

	/**
	 * <p>
	 * Create an instance of Status class.
	 * </p>
	 * <p>
	 * Require {@link Permission#PUBLISH_STREAM}.
	 * </p>
	 * @param message Message.
	 * @throws IllegalArgumentException if message is null.
	 */
	public Status(String message) {
		this(message, null);
	}

	/**
	 * @see com.emobtech.facebook.api.Request#process(java.lang.String)
	 */
	public Response process(String accessToken) throws IOException {
		final String url = "https://graph.facebook.com/" + toUserId + "/feed";
		//
		HttpRequest req = new HttpRequest(url);
		//
		req.setMethod("POST");
		req.setBodyParameter("access_token", accessToken);
		req.setBodyParameter("message", message);
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
