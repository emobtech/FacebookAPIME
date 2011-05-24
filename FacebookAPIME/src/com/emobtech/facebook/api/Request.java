/*
 * Request.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api;

import java.io.IOException;

/**
 * <p>
 * This interface defines a request to be sent to Facebook.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
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
