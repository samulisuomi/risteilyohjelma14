package org.jouluristeily.risteilyohjelma14.content;

import java.util.List;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;
import org.jouluristeily.risteilyohjelma14.beans.PaikkaItem;
import org.jouluristeily.risteilyohjelma14.helpers.AukioloItemXmlPullParser;
import org.jouluristeily.risteilyohjelma14.helpers.PaikkaItemXmlPullParser;
import org.jouluristeily.risteilyohjelma14.helpers.SeparatedPaikkaAdapter;
import org.jouluristeily.risteilyohjelma14.helpers.TimeHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class AukioloFragment extends SherlockFragment {

    private int mPos = -1;
    private static ListView lv;
    private SeparatedPaikkaAdapter adapter;
    private List<PaikkaItem> paikkaItems_ostokset;
    private List<PaikkaItem> paikkaItems_baarit;
    private List<PaikkaItem> paikkaItems_ravintolat;
    private List<PaikkaItem> paikkaItems_muut;
    private final static int REFRESH_INTERVAL = 1000 * 5; // 5 seconds
    private Handler updateHandler;
    private Runnable updateRunnable;
    private static int XML_RESOURCE_ID_OSTOKSET = R.raw.items_paikka_ostokset;
    private static int XML_RESOURCE_ID_BAARIT = R.raw.items_paikka_baarit;
    private static int XML_RESOURCE_ID_RAVINTOLAT = R.raw.items_paikka_ravintolat;
    private static int XML_RESOURCE_ID_MUUT = R.raw.items_paikka_muut;

    public AukioloFragment() {

    }

    public AukioloFragment(int pos) {
        mPos = pos;
    }

    @Override
    public void onPause() {
        this.stopUpdatingListView();
        super.onPause();
    }

    @Override
    public void onStart() {
        this.startUpdatingListView();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        if (mPos == -1 && savedInstanceState != null) {
            mPos = savedInstanceState.getInt("mPos");
        }

        final LinearLayout aukiololayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_aukiolot, container, false);

        lv = (ListView) aukiololayout.findViewById(R.id.listview_aukiolot);

        if (paikkaItems_ostokset == null) {

            paikkaItems_ostokset = PaikkaItemXmlPullParser
                    .getPaikkaItemsFromXml(getActivity(),
                            XML_RESOURCE_ID_OSTOKSET);
            for (int i = 0; i < paikkaItems_ostokset.size(); i++) {
                PaikkaItem current = paikkaItems_ostokset.get(i);
                if (current.getNimi().equals("Tax Free")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_tax_free));
                } else if (current.getNimi()
                        .equals("Tax Free (alkoholimyynti)")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_tax_free_alkoholi));
                } else if (current.getNimi()
                        .equals("Tax Free (nuuskan myynti)")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_tax_free_nuuska));
                } else if (current.getNimi().equals("Tobacco")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_tobacco));
                } else if (current.getNimi().equals("Gift&Toys")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_muut));
                } else if (current.getNimi().equals("Perfumes")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_muut));
                } else if (current.getNimi().equals("Fashion Street")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ostokset_muut));
                }
            }
        }

        if (paikkaItems_baarit == null) {
            paikkaItems_baarit = PaikkaItemXmlPullParser.getPaikkaItemsFromXml(
                    getActivity(), XML_RESOURCE_ID_BAARIT);
            for (int i = 0; i < paikkaItems_baarit.size(); i++) {
                PaikkaItem current = paikkaItems_baarit.get(i);
                if (current.getNimi().equals("Starlight Palace")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_baarit_starlight));
                } else if (current.getNimi().equals("Iskelmäbaari")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_baarit_iskelma));
                } else if (current.getNimi().equals("Pubi")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_baarit_pubi));
                } else if (current.getNimi().equals("Piano Bar")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_baarit_piano));
                } else if (current.getNimi().equals("Klubi")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_baarit_klubi));
                }
            }
        }

        if (paikkaItems_ravintolat == null) {
            paikkaItems_ravintolat = PaikkaItemXmlPullParser
                    .getPaikkaItemsFromXml(getActivity(),
                            XML_RESOURCE_ID_RAVINTOLAT);
            for (int i = 0; i < paikkaItems_ravintolat.size(); i++) {
                PaikkaItem current = paikkaItems_ravintolat.get(i);
                if (current.getNimi().equals("Buffet Silja Line")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ravintolat_buffet));
                } else if (current.getNimi().equals("Grill House")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ravintolat_grill));
                } else if (current.getNimi().equals("Katarina's Kitchen")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ravintolat_katarina));
                } else if (current.getNimi().equals("Happy Lobster")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ravintolat_happy));
                } else if (current.getNimi().equals("Cafeteria")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_ravintolat_cafeteria));
                }
            }
        }

        if (paikkaItems_muut == null) {
            paikkaItems_muut = PaikkaItemXmlPullParser.getPaikkaItemsFromXml(
                    getActivity(), XML_RESOURCE_ID_MUUT);
            for (int i = 0; i < paikkaItems_muut.size(); i++) {
                PaikkaItem current = paikkaItems_muut.get(i);
                if (current.getNimi().equals("Saunaosasto")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_muut_saunaosasto));
                } else if (current.getNimi().equals("LAL:n ja YKL:n ständit")) {
                    current.setAukiolot(AukioloItemXmlPullParser
                            .getAukioloItemsFromXml(getActivity(),
                                    R.raw.items_ao_muut_standit));
                }
            }
        }

        // custom adapter create
        adapter = new SeparatedPaikkaAdapter(getActivity());
        adapter.addSection(R.drawable.aukiolotsikot_ostokset_keltainen,
                paikkaItems_ostokset, getActivity());
        adapter.addSection(R.drawable.aukiolotsikot_baarit_keltainen,
                paikkaItems_baarit, getActivity());
        adapter.addSection(R.drawable.aukiolotsikot_ravintolat_keltainen,
                paikkaItems_ravintolat, getActivity());
        adapter.addSection(R.drawable.aukiolotsikot_muut_keltainen,
                paikkaItems_muut, getActivity());

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                PaikkaItem klikattuItem = null;
                int trueIndex = -1;
                if (arg2 < 8) {
                    // ostokset
                    trueIndex = arg2 - 1;
                    klikattuItem = paikkaItems_ostokset.get(trueIndex);
                } else if (arg2 < 14) {
                    // baarit
                    trueIndex = arg2 - 9;
                    klikattuItem = paikkaItems_baarit.get(trueIndex);
                } else if (arg2 < 20) {
                    // ravintolat
                    trueIndex = arg2 - 15;
                    klikattuItem = paikkaItems_ravintolat.get(trueIndex);
                } else if (arg2 < 23) {
                    // muut
                    trueIndex = arg2 - 21; // == 0
                    klikattuItem = paikkaItems_muut.get(trueIndex);
                }

                if (klikattuItem != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    builder.setTitle(klikattuItem.getNimi());
                    builder.setMessage(klikattuItem.toString());
                    builder.setPositiveButton(R.string.sulje,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // Just close the dialog
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

        });
        setHasOptionsMenu(true);
        setRetainInstance(true);

        // Initialize handler/runnable to refresh listview periodically
        updateHandler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                updateHandler.postDelayed(updateRunnable, REFRESH_INTERVAL);
            }
        };

        getSherlockActivity().getSupportActionBar().setLogo(
                R.drawable.title_aukiolot);
        StartActivity.sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // Inflate the layout for this fragment
        return aukiololayout;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.stopUpdatingListView();
        outState.putInt("mPos", mPos);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // for testing purposes:
        // inflater.inflate(R.menu.action_bar_aukiolo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_paivita_aukiolot) {
            adapter.notifyDataSetChanged();
            CharSequence text = "Päivitetty: " + TimeHelper.getCurrentTime();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getSherlockActivity(), text, duration);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startUpdatingListView() {
        updateRunnable.run();
    }

    private void stopUpdatingListView() {
        updateHandler.removeCallbacks(updateRunnable);
    }
}
