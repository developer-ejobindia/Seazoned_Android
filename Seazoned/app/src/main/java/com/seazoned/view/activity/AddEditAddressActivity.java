package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityAddEditAddressBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddEditAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ActivityAddEditAddressBinding mBinding;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mPostalCode;
    private String mode;
    private String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_address);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = getWindow(); // in Activity's onCreate() for instance
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mode = bundle.getString("mode");
            if (mode.equalsIgnoreCase("edit")) {
                addressId = bundle.getString("addressId");
                mBinding.etName.setText(bundle.getString("name"));
                mBinding.etEmail.setText(bundle.getString("email"));
                mBinding.etPhoneNumber.setText(bundle.getString("phoneNo"));
                mBinding.tvAddress.setText(AppData.sSearchAddress=bundle.getString("address"));

                mCity=bundle.getString("city");
                mState=bundle.getString("state");
                mCountry=bundle.getString("country");

                mBinding.tvTitle.setText("Edit Address");

                mBinding.tvSave.setText("Update");
            } else if (mode.equalsIgnoreCase("add")) {
                mBinding.tvSave.setText("save");
                mBinding.tvTitle.setText("Add Address");

            }
        }
        mBinding.tvSave.setOnClickListener(this);
        mBinding.tvAddress.setOnClickListener(this);
        mBinding.lnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvSave) {
            String name = mBinding.etName.getText().toString();
            String email = mBinding.etEmail.getText().toString();
            String phoneNumber = mBinding.etPhoneNumber.getText().toString();
            String address = mBinding.tvAddress.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address)) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Util.isValidEmail(email)) {
                Toast.makeText(this, "Invalid email id.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Util.isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("contact_name", name);
            params.put("street_address", address);
            params.put("city", mCity);
            params.put("state", mState);
            params.put("country", mCountry);
            params.put("contact_number", phoneNumber);
            params.put("email_address", email);
            if (mode.equalsIgnoreCase("add"))
                saveAddressData(Api.sAddAddress, params);
            else if (mode.equalsIgnoreCase("edit")) {
                params.put("address_id", addressId);
                saveAddressData(Api.sEditAddress, params);
            }

        } else if (view == mBinding.tvAddress) {
            try {

                /*Intent intents = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .build(AddEditAddressActivity.this);
                startActivityForResult(intents, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/
                Intent intent=new Intent(AddEditAddressActivity.this,LocationSearchActivity.class);
                startActivityForResult(intent,PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (Exception e) {
                // TODO: Handle the error.
            }
        }
    }

    private void saveAddressData(String url, HashMap<String, String> params) {
        new PostDataParser(AddEditAddressActivity.this, url, params, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        Toast.makeText(AddEditAddressActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")) {
                            Util.closeKeyBoard(AddEditAddressActivity.this);
                            mBinding.lnBack.performClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            //if (resultCode==RESULT_OK){

            mAddress = AppData.sSearchAddress;
            try {
                Geocoder geocoder = new Geocoder(this);
                // String locationName = loc.getText().toString();
                List<Address> addresses = geocoder.getFromLocationName(
                        mAddress, 5);
                mAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                mCity = addresses.get(0).getLocality();
                mState = addresses.get(0).getAdminArea();
                mCountry = addresses.get(0).getCountryName();
                mPostalCode = addresses.get(0).getPostalCode();

                Log.i("address", mAddress);

                mBinding.tvAddress.setText(mAddress);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        /*if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Place:", String.valueOf(place.getLatLng()));
                Log.i("data", place.toString());
                /*//********Lat lan wise address
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                LatLng location = place.getLatLng();
                try {
                    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    mAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    mCity = addresses.get(0).getLocality();
                    mState = addresses.get(0).getAdminArea();
                    mCountry = addresses.get(0).getCountryName();
                    mPostalCode = addresses.get(0).getPostalCode();

                    Log.i("address", mAddress);

                    mBinding.tvAddress.setText(mAddress);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                /*//************

                //finish();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Placeerr:", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }*/
    }
}
