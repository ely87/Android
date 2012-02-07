package com.publizar.publizaraldia;

import adapters.ActionBar;
import adapters.ActionBar.Action;
import adapters.ActionBar.IntentAction;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebLoaderActivity extends Activity {
	WebView mWebView;
	ProgressDialog mProgress;
	private String promo_website;
	private ActionBar actionBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Favoritos");
		actionBar
				.setHomeAction(new IntentAction(this,
						PreferencesTimelineActivity.createIntent(this),
						R.drawable.home));
		actionBar.setDisplayHomeAsUpEnabled(true);

		final Action promos = new IntentAction(this, new Intent(this,
				AllPromosActivity.class), R.drawable.list);
		actionBar.addAction(promos);

		final Action calendar = new IntentAction(this, new Intent(this,
				CalendarListActivity.class), R.drawable.calendar);
		actionBar.addAction(calendar);

		final Action settings = new IntentAction(this, new Intent(this,
				SettingsActivity.class), R.drawable.settings);
		actionBar.addAction(settings);
		Intent intent = getIntent();
		// Get Web view
		mWebView = (WebView) findViewById(R.id.webview); // This is the id you
															// gave
		// to the WebView in the main.xml
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true); // Zoom Control on web (You
														// don't need this
		// if ROM supports Multi-Touch
		mWebView.getSettings().setBuiltInZoomControls(true); // Enable
																// Multitouch if
																// supported by
																// ROM
		promo_website = intent.getStringExtra("Promo_website");
		mProgress = ProgressDialog.show(this, "Cargando",
				"Por favor espere un momento...");
		// Load URL

		if (promo_website.contains(".pdf")) {
			mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="
					+ promo_website);

		} else {
			mWebView.loadUrl(promo_website);
		}

		mWebView.setWebViewClient(new WebViewClient() {
			// when finish loading page
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (mProgress.isShowing()) {
					mProgress.dismiss();
				}
			}
		});

	}// End of Method onCreate

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}