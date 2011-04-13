package com.emobtech.facebook.api.graph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;

import com.emobtech.facebook.api.Request;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Represents a request for the user's profile picture.
 * </p>
 * @author ernandes@gmail.com
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
	 * Creates a request for the given user's profile picture.
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
		HttpConnection conn = null;
		//
		try {
			conn = (HttpConnection)Connector.open(url);
			//
			int rcode = conn.getResponseCode();
			//
			if (rcode >= 300 && rcode < 400) { //redirected?
				url = conn.getHeaderField("Location");
				conn.close();
				//
				conn = (HttpConnection)Connector.open(url);
				rcode = conn.getResponseCode();
			}
			//
			if (rcode == HttpConnection.HTTP_OK) {
				InputStream in = conn.openInputStream();
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
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	/**
	 * <p>
	 * Result of profile picture request.
	 * </p>
	 * @author ernandes@gmail.com
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
		 * Creates an instance of Result.
		 * </p>
		 * @param data Image data.
		 */
		Response(byte[] data) {
			this.data = data;
		}
		
		/**
		 * <p>
		 * Returns image data.
		 * </p>
		 * @return Data.
		 */
		public byte[] getData() {
			return data;
		}
		
		/**
		 * <p>
		 * Returns image object.
		 * </p>
		 * @return Object.
		 */
		public Image getImage() {
			return Image.createImage(data, 0, data.length);
		}
	}
}
