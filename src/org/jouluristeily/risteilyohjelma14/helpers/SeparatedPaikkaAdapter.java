package org.jouluristeily.risteilyohjelma14.helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.beans.PaikkaItem;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Inspired by Jeff Sharkey
 * (http://jsharkey.org/blog/2008/08/18/separating-lists
 * -with-headers-in-android-09/)
 */
public class SeparatedPaikkaAdapter extends BaseAdapter {

    public final Map<Integer, ArrayAdapter<PaikkaItem>> sections = new LinkedHashMap<Integer, ArrayAdapter<PaikkaItem>>();

    public final ArrayAdapter<Integer> headers;
    private ArrayList<Integer> header_ids;
    public final static int TYPE_SECTION_HEADER = 0;

    public SeparatedPaikkaAdapter(Activity context) {
        header_ids = new ArrayList<Integer>();
        headers = new OtsikkoAdapter(context, -1, header_ids);
    }

    public void addSection(int otsikkoId, List<PaikkaItem> sektionPaikkaItemit,
            Activity context) {
        this.header_ids.add(otsikkoId);
        ArrayAdapter<PaikkaItem> lisattavaAdapteri = new SisaltoAdapter(
                context, -1, sektionPaikkaItemit);
        this.sections.put(otsikkoId, lisattavaAdapteri);

    }

    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) {
                return section;
            }
            if (position < size) {
                return adapter.getItem(position - 1);
            }

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }

    public int getCount() {
        // total together all sections, plus one for each section header
        int total = 0;
        for (Adapter adapter : this.sections.values()) {
            total += adapter.getCount() + 1;
        }
        return total;
    }

    @Override
    public int getViewTypeCount() {
        // assume that headers count as one, then sum all sections up
        int total = 1;
        for (Adapter adapter : this.sections.values()) {
            total += adapter.getViewTypeCount();
        }
        return total;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) {
                return TYPE_SECTION_HEADER;
            }
            if (position < size) {
                return type + adapter.getItemViewType(position - 1);
            }

            // otherwise jump into next section
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) {
                return headers.getView(sectionnum, convertView, parent);
            }
            if (position < size) {
                return adapter.getView(position - 1, convertView, parent);
            }

            // otherwise jump into next section
            position -= size;
            sectionnum++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class OtsikkoAdapter extends ArrayAdapter<Integer> {

        private List<Integer> list;
        private Activity context;

        public OtsikkoAdapter(Activity context, int textViewResourceId,
                List<Integer> list) {
            super(context, R.layout.fragment_aukiolot_listheaders, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected ImageView otsikko;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                convertView = inflator.inflate(
                        R.layout.fragment_aukiolot_listheaders, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.otsikko = (ImageView) convertView
                        .findViewById(R.id.otsikkokuvanid);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.otsikko.setImageResource(list.get(position));

            return convertView;
        }
    }

    private class SisaltoAdapter extends ArrayAdapter<PaikkaItem> {

        private List<PaikkaItem> list;
        private Activity context;

        public SisaltoAdapter(Activity context, int textViewResourceId,
                List<PaikkaItem> list) {
            super(context, R.layout.fragment_aukiolot_listrow, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected ImageView aukivaiei;
            protected TextView nimi;
            protected TextView kansi;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                convertView = inflator.inflate(
                        R.layout.fragment_aukiolot_listrow, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.aukivaiei = (ImageView) convertView
                        .findViewById(R.id.list_row_circle);

                viewHolder.nimi = (TextView) convertView
                        .findViewById(R.id.aukiolopaikannimi);

                viewHolder.kansi = (TextView) convertView
                        .findViewById(R.id.kansinumero);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // The green circles on / off:
            if (list.get(position).onkoAvoinnaNyt(TimeHelper.getCurrentTime())) {
                viewHolder.aukivaiei.setImageResource(R.drawable.circle_on);
            } else {
                viewHolder.aukivaiei.setImageResource(R.drawable.circle_off);
            }
            viewHolder.nimi.setText(list.get(position).getNimi());
            viewHolder.kansi.setText(list.get(position).getKansi());

            return convertView;
        }
    }
}