package com.emobtech.facebook.api.auth.ui.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthenticationDialog;

/**
 * @author ernandes@gmail.com
 */
public class AuthenticationDialogWebView extends WebView implements AuthenticationDialog {
	/**
	 * @param context
	 */
	public AuthenticationDialogWebView(Context context) {
		super(context);
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setAppId(java.lang.String)
	 */
	public void setAppId(String appId) {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setAppSecret(java.lang.String)
	 */
	public void setAppSecret(String appSecret) {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setRedirectUri(java.lang.String)
	 */
	public void setRedirectUri(String redirectUri) {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#setPermissions(java.lang.String[])
	 */
	public void setPermissions(String[] permissions) {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#addLoginDialogListener(com.emobtech.facebook.api.auth.AuthenticationListener)
	 */
	public void addLoginDialogListener(AuthenticationListener listener) {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#login()
	 */
	public void login() {
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthenticationDialog#logout()
	 */
	public void logout() {
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class AuthenticationDialogWebViewClient extends WebViewClient {
		/**
		 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
		 */
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		/**
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
		 */
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	}
}
