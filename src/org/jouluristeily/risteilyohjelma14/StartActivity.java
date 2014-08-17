package org.jouluristeily.risteilyohjelma14;

import org.jouluristeily.risteilyohjelma14.content.OhjelmaFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class StartActivity extends SlidingFragmentActivity {

    private Fragment contentFragment;
    protected ListFragment mFrag;
    public static SlidingMenu sm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager()
                    .beginTransaction();
            mFrag = new MenuFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (ListFragment) this.getSupportFragmentManager()
                    .findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setBehindScrollScale(0.05f);
        sm.setFadeDegree(0.35f);
        sm.setFadeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // set the Above View (content)
        if (savedInstanceState != null) {
            contentFragment = getSupportFragmentManager().getFragment(
                    savedInstanceState, "contentFragment");
        }
        if (contentFragment == null) {
            contentFragment = new OhjelmaFragment(); // default content
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, contentFragment).commit();

        }

        // set the Above View
        setContentView(R.layout.content_frame);

        // set the Behind View
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment()).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "contentFragment",
                contentFragment);
    }

    public void switchContent(Fragment fragment) {
        contentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (sm.isMenuShowing()) { // If SlidingMenu is showing
                finish(); // -> close the app
            } else { // If SlidingMenu is not showing
                toggle(); // -> open the SlidingMenu, not close the app
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    // Open the menu on activity start
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
            savedInstanceState.putBoolean("SlidingActivityHelper.open", true);
        }
        super.onPostCreate(savedInstanceState);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            // don't care (keyboard stays open)
        }
    }

}
