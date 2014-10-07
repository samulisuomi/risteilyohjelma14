package org.jouluristeily.risteilyohjelma14.content;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;
import org.jouluristeily.risteilyohjelma14.helpers.TouchImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        final LinearLayout karttalayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_feed, container, false);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        getSherlockActivity().getSupportActionBar().setLogo(
                R.drawable.title_feed);
        StartActivity.sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // Inflate the layout for this fragment
        return karttalayout;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
