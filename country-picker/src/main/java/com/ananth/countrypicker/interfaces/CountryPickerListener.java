package com.ananth.countrypicker.interfaces;

/**
 * Created by Babu on 7/2/2016.
 */
public interface CountryPickerListener {
    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID);
}
