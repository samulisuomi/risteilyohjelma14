package org.jouluristeily.risteilyohjelma14.content;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;
import org.jouluristeily.risteilyohjelma14.helpers.TouchImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class KarttaFragment extends SherlockFragment {

    private int mPos = -1;
    private static ImageView karttanappi_ostokset;
    private static ImageView karttanappi_baarit;
    private static ImageView karttanappi_ravintolat;
    private static TouchImageView kartta;
    private static boolean[] toggleStates;

    public KarttaFragment() {

    }

    public KarttaFragment(int pos) {
        mPos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null) {
            mPos = savedInstanceState.getInt("mPos");
        }
        final LinearLayout karttalayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_kartta, container, false);
        toggleStates = new boolean[3];
        kartta = (TouchImageView) karttalayout.findViewById(R.id.kartta);

        kartta.setImageResource(R.drawable.kartta_90);

        karttanappi_ostokset = (ImageView) karttalayout
                .findViewById(R.id.kartta_nappi_ostokset);
        karttanappi_ostokset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleStates[0] == false) {
                    highlightImage(0);
                } else {
                    highlightImage(-1);
                }
            }
        });

        karttanappi_baarit = (ImageView) karttalayout
                .findViewById(R.id.kartta_nappi_baarit);
        karttanappi_baarit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleStates[1] == false) {
                    highlightImage(1);
                } else {
                    highlightImage(-1);
                }
            }
        });

        karttanappi_ravintolat = (ImageView) karttalayout
                .findViewById(R.id.kartta_nappi_ravintolat);
        karttanappi_ravintolat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleStates[2] == false) {
                    highlightImage(2);
                } else {
                    highlightImage(-1);
                }
            }
        });
        kartta.setSaveEnabled(true);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getSherlockActivity().getSupportActionBar().setLogo(
                R.drawable.title_kartta);
        StartActivity.sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // Inflate the layout for this fragment
        return karttalayout;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void highlightImage(int karttaToggleIndex) {
        if (karttaToggleIndex == 0) { // ostokset
            emptyAllImageResources();
            karttanappi_ostokset
                    .setImageResource(R.drawable.aukiolotsikot_ostokset_keltainen_90);
            karttanappi_baarit
                    .setImageResource(R.drawable.aukiolotsikot_baarit_90);
            karttanappi_ravintolat
                    .setImageResource(R.drawable.aukiolotsikot_ravintolat_90);
            kartta.setImageResource(R.drawable.kartta_ostokset_90);

            toggleStates[0] = true;
            toggleStates[1] = false;
            toggleStates[2] = false;
        } else if (karttaToggleIndex == 1) { // baarit
            emptyAllImageResources();
            karttanappi_ostokset
                    .setImageResource(R.drawable.aukiolotsikot_ostokset_90);
            karttanappi_baarit
                    .setImageResource(R.drawable.aukiolotsikot_baarit_keltainen_90);
            karttanappi_ravintolat
                    .setImageResource(R.drawable.aukiolotsikot_ravintolat_90);
            kartta.setImageResource(R.drawable.kartta_baarit_90);
            toggleStates[0] = false;
            toggleStates[1] = true;
            toggleStates[2] = false;
        } else if (karttaToggleIndex == 2) { // ravintolat
            emptyAllImageResources();
            karttanappi_ostokset
                    .setImageResource(R.drawable.aukiolotsikot_ostokset_90);
            karttanappi_baarit
                    .setImageResource(R.drawable.aukiolotsikot_baarit_90);
            karttanappi_ravintolat
                    .setImageResource(R.drawable.aukiolotsikot_ravintolat_keltainen_90);
            kartta.setImageResource(R.drawable.kartta_ravintolat_90);
            toggleStates[0] = false;
            toggleStates[1] = false;
            toggleStates[2] = true;
        } else if (karttaToggleIndex == -1) { // ei highlightata mitään
            emptyAllImageResources();
            karttanappi_ostokset
                    .setImageResource(R.drawable.aukiolotsikot_ostokset_90);
            karttanappi_baarit
                    .setImageResource(R.drawable.aukiolotsikot_baarit_90);
            karttanappi_ravintolat
                    .setImageResource(R.drawable.aukiolotsikot_ravintolat_90);
            kartta.setImageResource(R.drawable.kartta_90);
            toggleStates[0] = false;
            toggleStates[1] = false;
            toggleStates[2] = false;
        }

    }

    private void emptyAllImageResources() {
        karttanappi_ostokset.setImageResource(android.R.color.transparent);
        karttanappi_baarit.setImageResource(android.R.color.transparent);
        karttanappi_ravintolat.setImageResource(android.R.color.transparent);
        kartta.setImageResource(android.R.color.transparent);
    }
}
