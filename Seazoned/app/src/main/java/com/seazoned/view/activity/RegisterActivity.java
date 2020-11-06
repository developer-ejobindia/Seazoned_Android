package com.seazoned.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityRegisterBinding;
import com.seazoned.other.MyCustomProgressDialog;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.CountryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ActivityRegisterBinding mBinding;
    private ProgressDialog dialog;
    private String mDateOfBirth = "";
    private ArrayList<HashMap<String, String>> arrayList;
    private String mAddress = "";
    private String mCity = "";
    private String mState = "";
    private String mCountry = "";
    private String mPostalCode = "";
    String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String monthNo[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String day[] = new String[31];
    // String year[] = new String[100];
    String year[];


    private void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        //Util.setPadding(this, mBinding.mainLayout);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = getWindow(); // in Activity's onCreate() for instance
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        mBinding.tvClickhere.setOnClickListener(this);
        mBinding.tvDateOfBirth.setOnClickListener(this);
        mBinding.tvSignUp.setOnClickListener(this);
        mBinding.tvAddress.setOnClickListener(this);
        mBinding.ivHidePwd.setOnClickListener(this);
        mBinding.ivHideCnfPwd.setOnClickListener(this);
        mBinding.ivShowCnfPwd.setOnClickListener(this);
        mBinding.ivShowPwd.setOnClickListener(this);
        mBinding.tvTerms.setOnClickListener(this);

        setMonth();
        setDay();
        setYear();
        //getCountryDetails();
    }

    private void setYear() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        Collections.sort(years, Collections.reverseOrder());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, years);
        mBinding.spYear.setAdapter(adapter);
    }

    private void setDay() {
        for (int i = 0; i < 31; i++) {
            if (i < 9)
                day[i] = "0" + (i + 1);
            else
                day[i] = "" + (i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, day);
        mBinding.spDay.setAdapter(adapter);
    }

    private void setMonth() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, month);
        mBinding.spMonth.setAdapter(adapter);
    }

    private void getCountryDetails() {
        new GetDataParser(RegisterActivity.this, Api.sCountryList, true, new GetDataParser.OnGetResponseListner() {
            public String mStatus;
            public String mSuccess;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        mSuccess = response.optString("success");
                        mStatus = response.optString("msg");
                        if (mSuccess.equalsIgnoreCase("1")) {
                            arrayList = new ArrayList<>();
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", jsonObject.optString("id"));
                                hashMap.put("country_name", jsonObject.optString("country_name"));
                                arrayList.add(hashMap);
                            }
                            //set country adapter
                            mBinding.spCountry.setAdapter(new CountryAdapter(RegisterActivity.this, R.layout.spinner_item, arrayList));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.tvClickhere) {
            Bundle bundle=getIntent().getExtras();
            if (bundle!=null) {
                finish();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
            else {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (view == mBinding.tvDateOfBirth) {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = (m + 1) + "/" + d + "/" + y;
                    mDateOfBirth = Util.changeAnyDateFormat(date, "MM/dd/yyyy", "MM/dd/yyyy");
                    mBinding.tvDateOfBirth.setText(Util.changeAnyDateFormat(mDateOfBirth, "MM/dd/yyyy", "MMM-dd-yyyy"));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        } else if (view == mBinding.tvAddress) {

            try {
            /* Intent intent =
                     new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                             .build(this);
             startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/

                /*Intent intents = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .build(RegisterActivity.this);
                startActivityForResult(intents, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/
                Intent intent = new Intent(RegisterActivity.this, LocationSearchActivity.class);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (Exception e) {
                // TODO: Handle the error.
            }
        } else if (view == mBinding.tvSignUp) {
            mDateOfBirth = monthNo[mBinding.spMonth.getSelectedItemPosition()] + "/" + mBinding.spDay.getSelectedItem().toString() + "/" + mBinding.spYear.getSelectedItem().toString();
            String mFirstname = mBinding.etFirstName.getText().toString();
            String mLastName = mBinding.etLastName.getText().toString();
            String mEmail = mBinding.etEmail.getText().toString();
            String mPhoneNo = mBinding.etPhoneNumber.getText().toString();
            //String mDateOfBirth = mBinding.tvDateOfBirth.getText().toString();
            //String mLocation = mBinding.etLocation.getText().toString();
            String mLocation = mBinding.tvAddress.getText().toString();
            /*String mCity = mBinding.etCity.getText().toString();
            String mState = mBinding.etState.getText().toString();*/
            //String mCountryId = arrayList.get(mBinding.spCountry.getSelectedItemPosition()).get("id");

            String mPassword = mBinding.etPassword.getText().toString();
            String mCnfPassword = mBinding.etConfirmPassword.getText().toString();
            if (TextUtils.isEmpty(mFirstname)) {
                Toast.makeText(this, "Enter first name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mLastName)) {
                Toast.makeText(this, "Enter last name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mEmail)) {
                Toast.makeText(this, "Enter email.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Util.isValidEmail(mEmail)) {
                Toast.makeText(this, "Enter valid email.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mPhoneNo)) {
                Toast.makeText(this, "Enter phone number.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Util.isValidPhoneNumber(mPhoneNo)) {
                Toast.makeText(this, "Enter valid phone number.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mDateOfBirth)) {
                Toast.makeText(this, "Enter date of birth.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Util.isValidBirthday(mDateOfBirth)) {
                Toast.makeText(this, "Enter valid date of birth.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mLocation)) {
                Toast.makeText(this, "Enter address name.", Toast.LENGTH_SHORT).show();
                return;
            }
            /*if (TextUtils.isEmpty(mCity)) {
                Toast.makeText(this, "Enter city name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mState)) {
                Toast.makeText(this, "Enter state name.", Toast.LENGTH_SHORT).show();
                return;
            }*/
            /*if (TextUtils.isEmpty(mCountryId)) {
                Toast.makeText(this, "Enter country name.", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (TextUtils.isEmpty(mPassword)) {
                Toast.makeText(this, "Enter password.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mCnfPassword)) {
                Toast.makeText(this, "Enter confirm password.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!mPassword.equals(mCnfPassword)) {
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!mBinding.cbTerms.isChecked()) {
                Toast.makeText(this, "Sorry, you must accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("first_name", mFirstname);
            params.put("last_name", mLastName);
            params.put("email", mEmail);
            params.put("tel", mPhoneNo);
            params.put("dob", mDateOfBirth);
            params.put("street", mLocation);
            params.put("city", mCity);
            params.put("state", mState);
            params.put("country", "193");
            params.put("password", mCnfPassword);
            getUserRegisterInfo(params);


        } else if (view == mBinding.ivHidePwd) {
            mBinding.ivHidePwd.setVisibility(View.GONE);
            mBinding.ivShowPwd.setVisibility(View.VISIBLE);
            mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
        } else if (view == mBinding.ivShowPwd) {
            mBinding.ivShowPwd.setVisibility(View.GONE);
            mBinding.ivHidePwd.setVisibility(View.VISIBLE);
            mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etPassword.setSelection(mBinding.etPassword.getText().length());
        } else if (view == mBinding.ivHideCnfPwd) {
            mBinding.ivHideCnfPwd.setVisibility(View.GONE);
            mBinding.ivShowCnfPwd.setVisibility(View.VISIBLE);
            mBinding.etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mBinding.etConfirmPassword.setSelection(mBinding.etConfirmPassword.getText().length());
        } else if (view == mBinding.ivShowCnfPwd) {
            mBinding.ivShowCnfPwd.setVisibility(View.GONE);
            mBinding.ivHideCnfPwd.setVisibility(View.VISIBLE);
            mBinding.etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etConfirmPassword.setSelection(mBinding.etConfirmPassword.getText().length());
        } else if (view == mBinding.tvTerms) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.seazoned.com/customer-terms-conditions"));
            startActivity(intent);
        }
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
            } catch (Exception e) {
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


    private void getUserRegisterInfo(final HashMap<String, String> params) {
        if (!Util.isConnected(RegisterActivity.this)) {
            Util.showSnakBar(RegisterActivity.this, getResources().getString(R.string.internectconnectionerror), mBinding.tvSignUp);
            return;
        }
        dialog = MyCustomProgressDialog.ctor(RegisterActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        showpDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.sUserRegistration,
                new Response.Listener<String>() {
                    public String mStatus;
                    public String mSuccess;

                    @Override
                    public void onResponse(String data) {
                        try {
                            Util util = new Util();
                            JSONObject response = util.getjsonobject(data);
                            mSuccess = response.optString("success");
                            mStatus = response.optString("msg");
                            Toast.makeText(RegisterActivity.this, "" + mStatus, Toast.LENGTH_SHORT).show();
                            if (mSuccess.equalsIgnoreCase("1")) {
                                mBinding.tvClickhere.performClick();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                Util.showSnakBar(RegisterActivity.this, getResources().getString(R.string.networkerror));
                VolleyLog.d("Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(RegisterActivity.this).add(postRequest);


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.tvClickhere.performClick();
    }
}
