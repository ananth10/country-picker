package com.ananth.countrypickersample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ananth.countrypicker.fragments.CountryPickerFragment;
import com.ananth.countrypicker.interfaces.CountryPickerListener;
import com.ananth.countrypicker.models.CountryItem;
import com.ananth.countrypickersample.R;
public class SelectCountry extends AppCompatActivity {

    private TextView mCountryNameTextView, mCountryIsoCodeTextView, mCountryDialCodeTextView;
    private ImageView mCountryFlagImageView;
    private Button mPickCountryButton;
    private CountryPickerFragment mCountryPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        init();
        setPickListener();
    }

    private void setPickListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override public void onSelectCountry(String name, String code, String dialCode,
                                                  int flagDrawableResID) {
                mCountryNameTextView.setText(name);
                mCountryIsoCodeTextView.setText(code);
                mCountryDialCodeTextView.setText(dialCode);
                mCountryFlagImageView.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });
        mPickCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        getUserCountryInfo();
    }
    private void init() {
        mCountryNameTextView = (TextView) findViewById(R.id.country_name);
        mCountryIsoCodeTextView = (TextView) findViewById(R.id.country_iso);
        mCountryDialCodeTextView = (TextView) findViewById(R.id.country_code);
        mPickCountryButton = (Button) findViewById(R.id.select_country);
        mCountryFlagImageView = (ImageView) findViewById(R.id.country_flag);
        mCountryPicker = CountryPickerFragment.newInstance("Select Country");
    }

    private void getUserCountryInfo() {
        CountryItem country = mCountryPicker.getUserCountryInfo(this);
        mCountryFlagImageView.setImageResource(country.getmFlag());
        mCountryDialCodeTextView.setText(country.getmDialCode());
        mCountryIsoCodeTextView.setText(country.getmCode());
        mCountryNameTextView.setText(country.getmName());
    }
}
