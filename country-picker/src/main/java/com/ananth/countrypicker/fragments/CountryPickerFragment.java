package com.ananth.countrypicker.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ananth.countrypicker.R;
import com.ananth.countrypicker.adapter.CountryAdapter;
import com.ananth.countrypicker.constants.Constants;
import com.ananth.countrypicker.interfaces.CountryPickerListener;
import com.ananth.countrypicker.models.CountryItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by Babu on 7/2/2016.
 */
public class CountryPickerFragment extends DialogFragment implements Comparator<CountryItem> {

    private EditText searchEditText;
    private ListView countryListView;
    private CountryAdapter adapter;
    private List<CountryItem> allCountriesList;
    private List<CountryItem> selectedCountriesList;
    private CountryPickerListener listener;
    private Context context;
    private TextView mCancel;

    public void setListener(CountryPickerListener listener) {
        this.listener = listener;
    }

    public EditText getSearchEditText() {
        return searchEditText;
    }

    public ListView getCountryListView() {
        return countryListView;
    }

    public static Currency getCurrencyCode(String countryCode) {
        try {
            return Currency.getInstance(new Locale("en", countryCode));
        } catch (Exception ignored) {
        }
        return null;
    }

    private List<CountryItem> getAllCountries() {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<>();
                String allCountriesCode = readEncodedJsonString();
//                System.out.println("country decoded :"+allCountriesCode);
                JSONArray countryArray = new JSONArray(allCountriesCode);
//                System.out.println("country decoded 11:"+countryArray);
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject jsonObject = countryArray.getJSONObject(i);
                    String countryName = jsonObject.getString("name");
                    String countryDialCode = jsonObject.getString("dial_code");
                    String countryCode = jsonObject.getString("code");
                    CountryItem country = new CountryItem();
                    country.setmCode(countryCode);
                    country.setmName(countryName);
                    country.setmDialCode(countryDialCode);
                    allCountriesList.add(country);
                }
                Collections.sort(allCountriesList, this);
                selectedCountriesList = new ArrayList<>();
                selectedCountriesList.addAll(allCountriesList);
                return allCountriesList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String readEncodedJsonString() throws java.io.IOException {
        byte[] data = Base64.decode(Constants.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    /**
     * To support show as dialog
     */
    public static CountryPickerFragment newInstance(String dialogTitle) {
        CountryPickerFragment picker = new CountryPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        mCancel=(TextView)view.findViewById(R.id.cancel);
        Bundle args = getArguments();
        if (args != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }
        getAllCountries();
        searchEditText = (EditText) view.findViewById(R.id.country_code_picker_search);
        countryListView = (ListView) view.findViewById(R.id.country_code_picker_listview);

        adapter = new CountryAdapter(getActivity(), selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    CountryItem country = selectedCountriesList.get(position);
                    listener.onSelectCountry(country.getmName(), country.getmCode(), country.getmDialCode(),
                            country.getmFlag());
                    searchEditText.setText("");
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        return view;
    }

    @SuppressLint("DefaultLocale")
    private void search(String text) {
        selectedCountriesList.clear();
        for (CountryItem country : allCountriesList) {
            if (country.getmName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public int compare(CountryItem lhs, CountryItem rhs) {
        return lhs.getmName().compareTo(rhs.getmName());
    }

    public CountryItem getUserCountryInfo(Context context) {
        this.context = context;
        getAllCountries();
        String countryIsoCode;
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!(telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)) {
            countryIsoCode = telephonyManager.getSimCountryIso();
            for (int i = 0; i < allCountriesList.size(); i++) {
                CountryItem country = allCountriesList.get(i);
                if (country.getmCode().equalsIgnoreCase(countryIsoCode)) {
                    country.setmFlag(getFlagResId(country.getmCode()));
                    return country;
                }
            }
        }
        return defaultCountry();
    }

    public CountryItem matchedCountry(String iso)
    {
        for (int i = 0; i < allCountriesList.size(); i++) {
            CountryItem country = allCountriesList.get(i);
            if (country.getmCode().equalsIgnoreCase(iso)) {
                country.setmFlag(getFlagResId(country.getmCode()));
                return country;
            }
        }
        return defaultCountry();
    }

    private CountryItem defaultCountry() {
        CountryItem country = new CountryItem();
        country.setmCode("US");
        country.setmName("United States");
        country.setmDialCode("1");
        country.setmFlag(R.drawable.flag_us);
        return country;
    }

    private int getFlagResId(String drawable) {
        try {
            return context.getResources()
                    .getIdentifier("flag_" + drawable.toLowerCase(Locale.ENGLISH), "drawable",
                            context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
