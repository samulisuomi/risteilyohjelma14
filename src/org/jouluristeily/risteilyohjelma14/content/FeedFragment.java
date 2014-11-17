package org.jouluristeily.risteilyohjelma14.content;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class FeedFragment extends SherlockFragment {

    private int mPos = -1;
    private static WebView feedView;
    private static final String URL_FEED = "http://sasuomi.github.io/risteilyfeed/";

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
        feedView = (WebView) feedlayout.findViewById(R.id.feedView);
        feedView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(URL_FEED)) {
                    Log.i("moi", "eka");
                    feedView.loadUrl(url);
                    return true;
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    Log.i("moi", "toka");
                    startActivity(browserIntent);
                    return true;
                }
            }
        });
        WebSettings webSettings = feedView.getSettings();
        feedView.setBackgroundColor(getResources().getColor(
                R.color.fragment_tausta));
        webSettings.setAppCachePath(getActivity().getCacheDir()
                .getAbsolutePath());
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);

        loadFeedFromCache();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_paivita_feed) {
            refreshFeed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFeedFromCache() {
        feedView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        feedView.loadUrl(URL_FEED);
    }

    private void refreshFeed() {
        // Roaming:
        ContentResolver cr = getActivity().getContentResolver();
        int isRoaming = 1;
        try {
            isRoaming = Settings.Secure
                    .getInt(cr, Settings.Secure.DATA_ROAMING);
        } catch (SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        feedView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        if (!isNetworkAvailable() || isRoaming == 1) { // loading offline
            CharSequence text = "Ei verkkoyhteyttä tai verkkovierailu käynnissä";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getSherlockActivity(), text, duration);
            toast.show();
            feedView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        } else {
            CharSequence text = "Ladataan...";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getSherlockActivity(), text, duration);
            toast.show();
        }
        feedView.loadUrl(URL_FEED);
    }

    private boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager check = (ConnectivityManager) getActivity()
                .getSystemService(getActivity().CONNECTIVITY_SERVICE);
        if (check != null) {
            NetworkInfo[] info = check.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                    }
                }
            }
        }
        return isConnected;
    }

    /*
     * private class MyWebViewClient extends WebViewClient {
     * 
     * @Override public boolean shouldOverrideUrlLoading(WebView view, String
     * url) { if (Uri.parse(url).getHost().equals(URL_FEED)) { // This is my web
     * site, so do not override; let my WebView load the page return false; } //
     * Otherwise, the link is not for a page on my site, so launch another
     * Activity that handles URLs Intent intent = new Intent(Intent.ACTION_VIEW,
     * Uri.parse(url)); startActivity(intent); return true; } }
     */
}
