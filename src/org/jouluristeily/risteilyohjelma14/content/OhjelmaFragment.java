package org.jouluristeily.risteilyohjelma14.content;

import java.util.List;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;
import org.jouluristeily.risteilyohjelma14.beans.OhjelmaItem;
import org.jouluristeily.risteilyohjelma14.helpers.OhjelmaItemXmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class OhjelmaFragment extends SherlockFragment {

    private static ListView lv;
    private ArrayAdapter<OhjelmaItem> adapter;
    private int mPos = -1;
    private Handler updateHandler;
    private Runnable updateRunnable;
    private final static int REFRESH_INTERVAL = 1000 * 5; // 5 seconds
    private List<OhjelmaItem> ohjelmaItems;
    private static int XML_RESOURCE_ID = R.raw.ohjelmalistaus_2014;

    public OhjelmaFragment() {

    }

    public OhjelmaFragment(int pos) {
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

    private void startUpdatingListView() {
        updateRunnable.run();
    }

    private void stopUpdatingListView() {
        updateHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null) {
            mPos = savedInstanceState.getInt("mPos");
        }

        final LinearLayout ohjelmalayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_ohjelma, container, false);

        lv = (ListView) ohjelmalayout.findViewById(R.id.listview_ohjelma);

        if (ohjelmaItems == null) {
            ohjelmaItems = OhjelmaItemXmlPullParser.getOhjelmaItemsFromXml(
                    getActivity(), XML_RESOURCE_ID);
        }

        adapter = new OhjelmaAdapter(getActivity(), -1, ohjelmaItems);
        lv.setAdapter(adapter);

        // Initialize handler/runnable to refresh listview periodically
        updateHandler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                updateHandler.postDelayed(updateRunnable, REFRESH_INTERVAL);
            }
        };

        // Jump to first non-ended event
        scrollToSmallestIndexNotEnded();

        setHasOptionsMenu(true);
        setRetainInstance(true);
        getSherlockActivity().getSupportActionBar().setLogo(
                R.drawable.title_ohjelma);
        StartActivity.sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // Inflate the layout for this fragment
        return ohjelmalayout;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_ohjelma, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class OhjelmaAdapter extends ArrayAdapter<OhjelmaItem> {

        private List<OhjelmaItem> list;
        private Activity context;

        public OhjelmaAdapter(Activity context, int textViewResourceId,
                List<OhjelmaItem> list) {
            super(context, R.layout.fragment_ohjelma_listrow, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView alkamisaikaView;
            protected TextView paattymisaikaView;
            protected TextView tapahtumanimiView;
            protected TextView tapahtumapaikkaView;
            protected TextView tapahtumakuvausView;
            protected TextView kansinumeroView;

            protected TextView valiviivaView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                convertView = inflator.inflate(
                        R.layout.fragment_ohjelma_listrow, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.alkamisaikaView = (TextView) convertView
                        .findViewById(R.id.alkamisaika);

                viewHolder.paattymisaikaView = (TextView) convertView
                        .findViewById(R.id.paattymisaika);

                viewHolder.tapahtumanimiView = (TextView) convertView
                        .findViewById(R.id.tapahtumanimi);

                viewHolder.tapahtumapaikkaView = (TextView) convertView
                        .findViewById(R.id.tapahtumapaikka);

                viewHolder.tapahtumakuvausView = (TextView) convertView
                        .findViewById(R.id.tapahtumakuvaus);

                viewHolder.kansinumeroView = (TextView) convertView
                        .findViewById(R.id.kansinumero);

                viewHolder.valiviivaView = (TextView) convertView
                        .findViewById(R.id.valiviivaViewId);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.alkamisaikaView.setText(list.get(position).getAlkaa());

            viewHolder.paattymisaikaView.setText(list.get(position)
                    .getPaattyy());
            if (list.get(position).getPaattyy().length() <= 3) {
                viewHolder.paattymisaikaView.setVisibility(View.GONE);
                viewHolder.valiviivaView.setVisibility(View.GONE);
            } else {
                viewHolder.paattymisaikaView.setVisibility(View.VISIBLE);
                viewHolder.valiviivaView.setVisibility(View.VISIBLE);
            }

            viewHolder.tapahtumanimiView.setText(list.get(position).getNimi());

            viewHolder.tapahtumapaikkaView.setText(list.get(position)
                    .getPaikka());

            // TODO: listan eka/viimeinen
            if (list.get(position).getNimi()
                    .equals("Baltic Princess lähtee Turusta")
                    || list.get(position).getNimi()
                            .equals("Baltic Princess saapuu Turkuun")) {
                viewHolder.tapahtumapaikkaView.setVisibility(TextView.GONE);
            } else {
                viewHolder.tapahtumapaikkaView.setVisibility(TextView.VISIBLE);
            }

            viewHolder.tapahtumakuvausView.setText(list.get(position)
                    .getKuvaus());

            if (list.get(position).getKuvaus().length() <= 3) {
                viewHolder.tapahtumakuvausView.setVisibility(TextView.GONE);
            } else {
                viewHolder.tapahtumakuvausView.setVisibility(TextView.VISIBLE);
            }

            viewHolder.kansinumeroView.setText(list.get(position).getKansi());

            if (list.get(position).onkoPaattynyt()) {
                viewHolder.alkamisaikaView.setTextColor(getResources()
                        .getColor(R.color.grey));
                viewHolder.paattymisaikaView.setTextColor(getResources()
                        .getColor(R.color.grey));
                viewHolder.kansinumeroView.setTextColor(getResources()
                        .getColor(R.color.grey));
                viewHolder.tapahtumakuvausView.setTextColor(getResources()
                        .getColor(R.color.grey));
                viewHolder.tapahtumanimiView.setTextColor(getResources()
                        .getColor(R.color.grey));
                viewHolder.tapahtumapaikkaView.setTextColor(getResources()
                        .getColor(R.color.grey));
                viewHolder.valiviivaView.setTextColor(getResources().getColor(
                        R.color.grey));
            } else {
                viewHolder.alkamisaikaView.setTextColor(getResources()
                        .getColor(R.color.white));
                viewHolder.paattymisaikaView.setTextColor(getResources()
                        .getColor(R.color.white));
                viewHolder.kansinumeroView.setTextColor(getResources()
                        .getColor(R.color.white));
                viewHolder.tapahtumanimiView.setTextColor(getResources()
                        .getColor(R.color.flat_jk_kelt));
                viewHolder.tapahtumapaikkaView.setTextColor(getResources()
                        .getColor(R.color.flat_jk_sin));
                viewHolder.tapahtumakuvausView.setTextColor(getResources()
                        .getColor(R.color.kuvaus_harmaa));
                viewHolder.valiviivaView.setTextColor(getResources().getColor(
                        R.color.white));
            }

            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_paivita_ohjelma) {
            scrollToSmallestIndexNotEnded();
        }
        return super.onOptionsItemSelected(item);
    }

    private int getSmallestIndexOfNotEnded() {
        int firstIndex = -1;
        int i = 0;
        while ((i < ohjelmaItems.size()) && (firstIndex == -1)) {
            if (!ohjelmaItems.get(i).onkoPaattynyt()) {
                firstIndex = i;
            }
            i++;
        }
        return firstIndex;
    }

    private void scrollToSmallestIndexNotEnded() {
        lv.setSelection(getSmallestIndexOfNotEnded());
    }
}
