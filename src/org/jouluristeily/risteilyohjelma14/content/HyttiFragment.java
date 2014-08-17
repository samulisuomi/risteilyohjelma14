package org.jouluristeily.risteilyohjelma14.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jouluristeily.risteilyohjelma14.R;
import org.jouluristeily.risteilyohjelma14.StartActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class HyttiFragment extends SherlockFragment {

    public static final String PREFS_NAME = "HyttiFile";
    private int mPos = -1;
    private static SharedPreferences sp;
    private static ListView lv;
    private ArrayAdapter<ListBean> adapter;
    private ArrayList<ListBean> poistettavat;
    private static TextView helpText;

    public HyttiFragment() {

    }

    public HyttiFragment(int pos) {
        mPos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null) {
            mPos = savedInstanceState.getInt("mPos");
        }

        // Inflate the layout for this fragment
        final LinearLayout hyttilayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_notes, container, false);

        lv = (ListView) hyttilayout.findViewById(R.id.listview_hytit);
        helpText = (TextView) hyttilayout.findViewById(R.id.hytti_ohje);
        populateListView();

        LinearLayout tallennalayout = (LinearLayout) hyttilayout
                .findViewById(R.id.hytinlisayspalkki);

        final EditText hyttinumero = (EditText) tallennalayout
                .findViewById(R.id.edittext_hyttinumero);
        final EditText hyttilaiset = (EditText) tallennalayout
                .findViewById(R.id.edittext_hyttiosallistujat);
        Button tallenna = (Button) tallennalayout
                .findViewById(R.id.button_tallennaHytti);

        hyttilaiset.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tallennaKentat(hyttilayout, hyttinumero, hyttilaiset);
                    hyttinumero.requestFocus();
                }
                return false;
            }
        });

        tallenna.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tallennaKentat(v, hyttinumero, hyttilaiset);
                hyttinumero.requestFocus();
            }

        });
        poistettavat = new ArrayList<ListBean>();
        StartActivity.sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getSherlockActivity().getSupportActionBar().setLogo(
                R.drawable.title_hyttimuistio);

        return hyttilayout;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_hytti, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.btn_poista_valitut:

            if (poistettavat.size() > 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setMessage(R.string.dialog_haluatko_poistaa_hytit)
                        .setTitle(R.string.dialog_hytti_title);
                builder.setPositiveButton(R.string.kylla,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Remove the selected ones and update listview
                                for (int i = 0; i < poistettavat.size(); i++) {
                                    removeFromSP(getActivity(), poistettavat
                                            .get(i).getNro());
                                }
                                poistettavat.clear();
                                CharSequence text = "Poistettu!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(
                                        getSherlockActivity(), text, duration);
                                toast.show();
                                populateListView();
                            }
                        });
                builder.setNegativeButton(R.string.peruuta,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Just close the dialog
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                CharSequence text = "Valitse poistettavat hytit.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getSherlockActivity(), text,
                        duration);
                toast.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mPos", mPos);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    public void tallennaKentat(View v, EditText nro, EditText htl) {
        Context context = v.getContext();
        if (nro.getText().toString().length() == 0) {
            CharSequence text = "Anna hyttinumero!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (nro.getText().toString().length() < 4) {
            CharSequence text = "Hyttinumeron on oltava 4- tai 5-numeroinen!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (readFromSP(context, nro.getText().toString()) != null) {
            CharSequence text = "Sama hyttinumero on jo olemassa!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Save to shared preferences
            saveToSP(context, nro.getText().toString(), htl.getText()
                    .toString());
            CharSequence text = "Tallennettu!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            // Refresh the ListView
            populateListView();
            nro.setText("");
            htl.setText("");
        }
    }

    public void populateListView() {
        List<ListBean> uusiLista = convertSPToBeans();
        adapter = new HyttiAdapter(getActivity(), uusiLista);
        lv.setAdapter(adapter);
        if (uusiLista.size() == 0) {
            helpText.setVisibility(TextView.VISIBLE);
        } else {
            helpText.setVisibility(TextView.GONE);
        }

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

    public static Map<String, ?> readAllDataFromSP(Context context) {
        sp = context.getSharedPreferences(PREFS_NAME, 0);
        return sp.getAll();
    }

    public static void removeFromSP(Context context, String key) {
        sp = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clearSP(Context context) {
        sp = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    private class ListBean {

        private String nro;
        private String teksti;
        private boolean selected;

        public ListBean(String nro, String teksti) {
            this.nro = nro;
            this.teksti = teksti;
            this.selected = false;
        }

        public String getNro() {
            return nro;
        }

        public String getTeksti() {
            return teksti;
        }

        public void setNro(String nro) {
            this.nro = nro;
        }

        public void setTeksti(String teksti) {
            this.teksti = teksti;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;

            if (selected) {
                poistettavat.add(this);
            } else {
                poistettavat.remove(this);
            }
        }

    }

    private class HyttiAdapter extends ArrayAdapter<ListBean> {

        private final List<ListBean> list;
        private final Activity context;

        public HyttiAdapter(Activity context, List<ListBean> list) {
            super(context, R.layout.fragment_notes_listrow, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            protected TextView nroF;
            protected TextView tekstiF;
            protected CheckBox checkbox;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflator = context.getLayoutInflater();
                view = inflator.inflate(R.layout.fragment_notes_listrow,
                        parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.nroF = (TextView) view.findViewById(R.id.nroLabel);
                viewHolder.tekstiF = (TextView) view
                        .findViewById(R.id.tekstiLabel);
                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
                viewHolder.checkbox
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(
                                    CompoundButton buttonView, boolean isChecked) {
                                ListBean element = (ListBean) viewHolder.checkbox
                                        .getTag();
                                element.setSelected(buttonView.isChecked());
                            }
                        });
                view.setTag(viewHolder);
                viewHolder.checkbox.setTag(list.get(position));
            } else {
                view = convertView;
                ((ViewHolder) view.getTag()).checkbox
                        .setTag(list.get(position));
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.nroF.setText(list.get(position).getNro());
            holder.tekstiF.setText(list.get(position).getTeksti());
            holder.checkbox.setChecked(list.get(position).isSelected());

            return view;
        }
    }

    // This reads all the key-value pairs from the SP by key and returns them as
    // a list of ListBeans
    private List<ListBean> convertSPToBeans() {
        List<ListBean> list = new ArrayList<ListBean>();
        Map<String, ?> map = readAllDataFromSP(getActivity());
        TreeMap<String, ?> keys = new TreeMap<String, Object>(map);

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            list.add(new ListBean(entry.getKey(), entry.getValue().toString()));
        }
        return list;
    }

}
