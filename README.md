# country-picker
This is library for showing all countries , anyone can add it to their porject in simple way


![alt tag](https://github.com/ananth10/country-picker/blob/master/screen1.png)
![alt tag](https://github.com/ananth10/country-picker/blob/master/screen2.png)


SET UP 

Just add the library dependency:

    compile 'com.ananth10.library:country-picker:1.3.3'
    
Usage 

```
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
    mCountryPicker = CountryPickerFragment.newInstance("Select Country");
  
  mPickCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        
            getUserCountryInfo();

    }
    ```
  Sample
  
  https://github.com/ananth10/country-picker
 
