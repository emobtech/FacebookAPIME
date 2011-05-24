/*
 * Friends.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.graph;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.emobtech.facebook.api.Request;
import com.twitterapime.io.HttpRequest;

/**
 * <p>
 * This class defines a request that retrieves the friends list.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Friends implements Request {
	/**
	 * <p>
	 * Parses the JSON content.
	 * </p>
	 * @param json Content;
	 * @return Array of data.
	 * @throws JSONException If any error occurs during parsing. 
	 */
	private static Hashtable[] parseJSON(String json) throws JSONException {
		JSONObject jo = new JSONObject(json);
		JSONArray ja = jo.getJSONArray("data");
		Hashtable[] fa = new Hashtable[ja.length()];
		//
		for (int i = 0; i < fa.length; i++) {
			jo = (JSONObject)ja.get(i);
			Hashtable fo = new Hashtable(2);
			//
			fo.put("id", jo.get("id"));
			fo.put("name", jo.get("name"));
			//
			fa[i] = fo;
		}
		//
		return fa;
	}

	/**
	 * <p>
	 * Create an instance of Friends class.
	 * </p>
	 */
	public Friends() {
	}

	/**
	 * @see com.emobtech.facebook.api.Request#process(java.lang.String)
	 */
	public com.emobtech.facebook.api.Response process(String accessToken)
		throws IOException {
		final String url =
			"https://graph.facebook.com/me/friends?access_token=" + accessToken;
		//
		HttpRequest req = new HttpRequest(url);
		//
		try {
			String json = req.send().getBodyContent();
			//
			return new Response(parseJSON(json));
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Friends's response class.
	 * </p>
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	public static class Response implements com.emobtech.facebook.api.Response,
		Enumeration {
		/**
		 * <p>
		 * Index.
		 * </p>
		 */
		private int index;
		
		/**
		 * <p>
		 * Friends data.
		 * </p>
		 */
		private Hashtable[] friendsData;
		
		/**
		 * <p>
		 * Create an instance of Result class.
		 * </p>
		 * @param friendsData Friends data.
		 */
		Response(Hashtable[] friendsData) {
			index = -1;
			this.friendsData = friendsData;
		}
		
		/**
		 * <p>
		 * Return friend's Id.
		 * </p>
		 * @return Id.
		 */
		public String getId() {
			return (String)friendsData[index].get("id");
		}

		/**
		 * <p>
		 * Return friend's name.
		 * </p>
		 * @return Name.
		 */
		public String getName() {
			return (String)friendsData[index].get("name");
		}

		/**
		 * @see java.util.Enumeration#hasMoreElements()
		 */
		public boolean hasMoreElements() {
			return index +1 < friendsData.length;
		}

		/**
		 * @see java.util.Enumeration#nextElement()
		 */
		public Object nextElement() {
			index++;
			return this;
		}
	}
}
