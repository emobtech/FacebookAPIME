package com.emobtech.facebook.api.graph;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.emobtech.facebook.api.Response;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Represents a status message to show on user's wall.
 * </p>
 * @author ernandes@gmail.com
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
	 * Creates a new status message.
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
	 * Creates a new status message.
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
