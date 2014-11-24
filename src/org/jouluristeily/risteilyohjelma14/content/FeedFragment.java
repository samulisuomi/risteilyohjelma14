package org.jouluristeily.risteilyohjelma14.content;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private static TextView disclaimerText;
    private static ImageView instagram;
    private static Button disclaimerButton;
    private static final String URL_FEED = "http://sasuomi.github.io/risteilyfeed/";
    private static final String URL_FEED_CACHE = "http://sasuomi.github.io/risteilyfeed/#cache";
    private static final String PREFS_NAME = "FeedFile";
    private static SharedPreferences sp;
    private static int errorCount;

    public FeedFragment() {

    }

    public FeedFragment(int pos) {
        mPos = pos;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null) {
            mPos = savedInstanceState.getInt("mPos");
        }
        final LinearLayout feedlayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_feed, container, false);

        disclaimerText = (TextView) feedlayout
                .findViewById(R.id.risteilyfeed_disclaimer);
        disclaimerButton = (Button) feedlayout
                .findViewById(R.id.risteilyfeed_button_ok);
        if (disclaimerChecked(getActivity())) {
            disclaimerText.setVisibility(TextView.GONE);
            disclaimerButton.setVisibility(Button.GONE);
        }
        disclaimerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disclaimerText.setVisibility(TextView.GONE);
                disclaimerButton.setVisibility(Button.GONE);
                setDisclaimerChecked(getActivity());
                if (!feedHasBeenLoadedOnce(getActivity())) {
                    refreshFeed();
                }
            }
        });

        instagram = (ImageView) feedlayout.findViewById(R.id.image_instagram);
        instagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                 * .parse("http://www.instagram.com/"));
                 * startActivity(browserIntent);
                 */
                String packageName = "com.instagram.android";
                PackageManager packageManager = getActivity()
                        .getPackageManager();
                Intent intent = packageManager
                        .getLaunchIntentForPackage(packageName);
                if (intent == null) {
                    Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=" + packageName));
                    startActivity(intentMarket);
                } else {
                    startActivity(intent);
                }
            }
        });

        feedView = (WebView) feedlayout.findViewById(R.id.feedView);
        feedView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(URL_FEED) || url.equals(URL_FEED_CACHE)) {
                    feedView.loadUrl(url);
                    return true;
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(browserIntent);
                    return true;
                }
            }
        });
        // TODO: THIS IS A DIRTY FIX!!
        errorCount = 0;
        feedView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (consoleMessage.message()
                        .contains("Uncaught ReferenceError")) {
                    Log.i("ro14", "reference error");
                    if (errorCount < 5) {
                        loadFeedFromCache();
                        errorCount++;
                    } else {
                        CharSequence text = "Tapahtui virhe välimuistin käsittelyssä. Päivitä sivu.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getSherlockActivity(),
                                text, duration);
                        toast.show();
                    }
                }
                return super.onConsoleMessage(consoleMessage);
            }

        });
        WebSettings webSettings = feedView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        feedView.setBackgroundColor(getResources().getColor(
                R.color.fragment_tausta));
        webSettings.setAppCachePath(getActivity().getCacheDir()
                .getAbsolutePath());

        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        if (feedHasBeenLoadedOnce(getActivity())) {
            loadFeedFromCache();
        } else {

        }
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
        Log.i("ro14", "cache");
        feedView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        feedView.loadUrl(URL_FEED_CACHE);
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
        feedView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (!isNetworkAvailable() || isRoaming == 1) { // loading offline
            CharSequence text = "Ei verkkoyhteyttä tai verkkovierailu käynnissä";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getSherlockActivity(), text, duration);
            toast.show();
            if (feedHasBeenLoadedOnce(getActivity())) {
                loadFeedFromCache();
            }
        } else {
            CharSequence text = "Ladataan...";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getSherlockActivity(), text, duration);
            toast.show();
            feedView.loadUrl(URL_FEED);
            setLoadedOnceFlag(getActivity());
        }

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

    public static void saveToSP(Context context, String key, String value) {
        sp = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readFromSP(Context context, String key) {
        sp = context.getSharedPreferences(PREFS_NAME, 0);
        return sp.getString(key, null);
    }

    public static boolean feedHasBeenLoadedOnce(Context context) {
        return (readFromSP(context, "FLAG_FEED_LOADED_ONCE") != null);
    }

    public static void setLoadedOnceFlag(Context context) {
        saveToSP(context, "FLAG_FEED_LOADED_ONCE", "1");
    }

    public static boolean disclaimerChecked(Context context) {
        return (readFromSP(context, "FLAG_DISCLAIMER_CHECKED") != null);
    }

    public static void setDisclaimerChecked(Context context) {
        saveToSP(context, "FLAG_DISCLAIMER_CHECKED", "1");
    }
}
