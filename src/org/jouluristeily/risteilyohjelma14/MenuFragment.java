package org.jouluristeily.risteilyohjelma14;

import org.jouluristeily.risteilyohjelma14.content.AukioloFragment;
import org.jouluristeily.risteilyohjelma14.content.FeedFragment;
import org.jouluristeily.risteilyohjelma14.content.HyttiFragment;
import org.jouluristeily.risteilyohjelma14.content.KarttaFragment;
import org.jouluristeily.risteilyohjelma14.content.OhjelmaFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MenuFragment extends SherlockListFragment {

    private OhjelmaFragment oF;
    private AukioloFragment aF;
    private KarttaFragment kF;
    private HyttiFragment hF;
    private FeedFragment fF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        ListView lv = getListView();

        // header
        View header = inflater.inflate(
                R.layout.menu_list_header,
                (ViewGroup) getActivity().findViewById(
                        R.id.menu_list_header_root));
        // make header clicks invisible:
        header.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg1, MotionEvent motionEvent) {
                return true;
            }
        });
        lv.addHeaderView(header);

        // list content
        String[] contents = getResources()
                .getStringArray(R.array.content_names);
        MenuAdapter adapter = new MenuAdapter(getActivity());
        for (int i = 0; i < contents.length; i++) {
            int buttonImgRes = -1;
            switch (i) {
            case 0: // ohjelmafragment
                buttonImgRes = R.layout.menu_button_selector_ohjelma;
                break;
            case 1: // aukiolofragment
                buttonImgRes = R.layout.menu_button_selector_aukiolot;
                break;
            case 2: // karttafragment
                buttonImgRes = R.layout.menu_button_selector_kartta;
                break;
            case 3: // hyttifragment
                buttonImgRes = R.layout.menu_button_selector_hyttimuistio;
                break;
            case 4: // feedfragment
                buttonImgRes = R.layout.menu_button_selector_feed;
                break;
            }
            adapter.add(new MenuItem(contents[i], buttonImgRes));
        }

        setListAdapter(adapter);
    }

    // selecting the fragment to switch
    public void switchContent(int buttonId) {
        Fragment newContent = null;
        switch (buttonId) {
        case R.layout.menu_button_selector_ohjelma:
            if (oF == null) {
                newContent = new OhjelmaFragment();
            } else {
                newContent = oF;
            }
            break;
        case R.layout.menu_button_selector_aukiolot:
            if (aF == null) {
                newContent = new AukioloFragment();
            } else {
                newContent = aF;
            }
            break;
        case R.layout.menu_button_selector_kartta:
            if (kF == null) {
                newContent = new KarttaFragment();
            } else {
                newContent = kF;
            }
            break;
        case R.layout.menu_button_selector_hyttimuistio:
            if (hF == null) {
                newContent = new HyttiFragment();
            } else {
                newContent = hF;
            }
            break;
        case R.layout.menu_button_selector_feed:
            if (hF == null) {
                newContent = new FeedFragment();
            } else {
                newContent = fF;
            }
            break;
        }
        if (newContent != null) {
            switchFragment(newContent);
            StartActivity.hideSoftKeyboard(getActivity());
        }
    }

    private void switchFragment(Fragment fragment) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof StartActivity) {
            StartActivity fca = (StartActivity) getActivity();
            fca.switchContent(fragment);
        }
    }

    private class MenuItem {
        // public String tag;
        public int iconRes;

        public MenuItem(String tag, int iconRes) {
            // this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class MenuAdapter extends ArrayAdapter<MenuItem> {

        public MenuAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.menu_row, null);
            }
            ImageButton button = (ImageButton) convertView
                    .findViewById(R.id.row_button);
            button.setImageResource(getItem(position).iconRes);
            button.setTag(getItem(position).iconRes);
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ImageButton nyk = (ImageButton) v;
                    switchContent((Integer) nyk.getTag());
                }
            });

            return convertView;
        }
    }

}
