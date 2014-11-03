package org.jouluristeily.risteilyohjelma14.content;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;
import org.jouluristeily.risteilyohjelma14.helpers.TouchImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class FeedFragment extends SherlockFragment {

    private int mPos = -1;
    private static ImageView karttanappi_ostokset;
    private static ImageView karttanappi_baarit;
    private static ImageView karttanappi_ravintolat;
    private static TouchImageView kartta;
    private static boolean[] toggleStates;
    private static final String URL_FEED = "http://sasuomi.github.com/risteilyfeed";

    public FeedFragment() {

    }

    public FeedFragment(int pos) {
        mPos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null) {
            mPos = savedInstanceState.getInt("mPos");
        }
        final LinearLayout feedlayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_feed, container, false);
        WebView feedView = (WebView) feedlayout.findViewById(R.id.feedView);
        feedView.setWebViewClient(new WebViewClient());
        feedView.setBackgroundColor(getResources().getColor(R.color.fragment_tausta));
        WebSettings webSettings = feedView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        feedView.loadUrl(URL_FEED);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getSherlockActivity().getSupportActionBar().setLogo(
                R.drawable.title_feed);
        StartActivity.sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // Inflate the layout for this fragment
        return feedlayout;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    /*
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(URL_FEED)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
    */
}
