package com.emobtech.facebook.api.graph;

import java.io.IOException;

import com.emobtech.facebook.api.Request;
import com.emobtech.facebook.api.Response;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Represents a link to be shared on user's wall.
 * </p>
 * @author ernandes@gmail.com
 */
public final class Link implements Request {
	/**
	 * <p>
	 * Link.
	 * </p>
	 */
	private String link;
	
	/**
	 * <p>
	 * Picture Uri.
	 * </p>
	 */
	private String pictureUri;
	
	/**
	 * <p>
	 * Name.
	 * </p>
	 */
	private String name;
	
	/**
	 * <p>
	 * Caption.
	 * </p>
	 */
	private String caption;
	
	/**
	 * <p>
	 * Description.
	 * </p>
	 */
	private String description;
	
	/**
	 * <p>
	 * Message.
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
	 * Creates a new link to share.
	 * </p>
	 * @param link Link.
	 * @param pictureUri Picture Uri.
	 * @param name Name.
	 * @param caption Caption.
	 * @param description Description.
	 * @param message Message.
	 * @param toUserId Recipient Id.
	 * @throws IllegalArgumentException If link is null.
	 */
	public Link(String link, String pictureUri, String name, String caption,
		String description, String message, String toUserId) {
		if (StringUtil.isEmpty(link)) {
			throw new IllegalArgumentException("Link must not be null.");
		}
		//
		this.link = link;
		this.pictureUri = pictureUri;
		this.name = name;
		this.caption = caption;
		this.description = description;
		this.message = message;
		this.toUserId = StringUtil.isEmpty(toUserId) ? "me" : toUserId;
	}

	/**
	 * <p>
	 * Creates a new link to share.
	 * </p>
	 * @param link Link.
	 * @throws IllegalArgumentException If link is null.
	 */
	public Link(String link) {
		this(link, null, null, null, null, null, null);
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
		req.setBodyParameter("link", link);
		//
		if (!StringUtil.isEmpty(pictureUri)) {
			req.setBodyParameter("picture", pictureUri);
		}
		if (!StringUtil.isEmpty(name)) {
			req.setBodyParameter("name", name);
		}
		if (!StringUtil.isEmpty(caption)) {
			req.setBodyParameter("caption", caption);
		}
		if (!StringUtil.isEmpty(description)) {
			req.setBodyParameter("description", description);
		}
		if (!StringUtil.isEmpty(message)) {
			req.setBodyParameter("message", message);
		}
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
