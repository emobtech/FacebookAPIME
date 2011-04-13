package com.emobtech.facebook.api;

import java.io.IOException;

/**
 * <p>
 * Represents a request.
 * </p>
 * @author ernandes@gmail.com
 */
public interface Request {
	/**
	 * <p>
	 * Processes the request.
	 * </p>
	 * @param accessToken Access Token.
	 * @return Result of request.
	 * @throws IOException If any I/O error occurs during processing.
	 */
	public Response process(String accessToken) throws IOException;
}
