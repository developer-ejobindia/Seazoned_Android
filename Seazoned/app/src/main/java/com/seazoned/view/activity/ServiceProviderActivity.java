package com.seazoned.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.telecom.GatewayInfo;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityServiceProviderBinding;
import com.seazoned.model.ServiceProviderModel;
import com.seazoned.other.GPSTracker;
import com.seazoned.other.MyCustomProgressDialog;
import com.seazoned.other.RangeSeekBar;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.ServiceProviderAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ServiceProviderActivity extends AppCompatActivity {
    private ActivityServiceProviderBinding mBinding;
    private Toast toast = null;
    private ProgressDialog dialog;
    private ArrayList<ServiceProviderModel> providerArrayList = null;
    private Location location = null;
    private GPSTracker gps;
    private double my_longitude = 0.0;
    private double my_latitude = 0.0;
    private String sAddress, sCity, sState, sCountry, sPostalCode;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private double lat = 0.0;
    private double lang = 0.0;
    private int minRating = 0;
    private int maxrating = 5;
    String priceArray[] = {"None", "Low to High", "High to Low"};
    private String priceFilter = "";


    private void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.llBack.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_service_provider);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_service_provider);
        Util.setPadding(this, mBinding.mainLayout);

        mBinding.tvServiceName.setText(getIntent().getExtras().getString("ServiceName"));
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mBinding.rcvServiceProviderList.setLayoutManager(manager);
        mBinding.rcvServiceProviderList.setNestedScrollingEnabled(false);

        //*******
        getCurrentAddress();
        mBinding.ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentAddress();
            }
        });

        mBinding.editWorkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searching();

            }
        });
        mBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(ServiceProviderActivity.this,DashBoardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        mBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ServiceProviderActivity.this);
                View view = getLayoutInflater().inflate(R.layout.activity_search_filter, null);
                builder.setView(view);
                builder.setCancelable(false);

                LinearLayout lnCross = view.findViewById(R.id.lnCross);
                final RangeSeekBar rangeSeekBar = view.findViewById(R.id.rangeSeekbar);
                final TextView tvMin = view.findViewById(R.id.tvMin);
                final TextView tvMax = view.findViewById(R.id.tvMax);
                final TextView tvApplyFilter = view.findViewById(R.id.tvApplyFilter);
                final Spinner spprice = view.findViewById(R.id.spPrice);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ServiceProviderActivity.this, R.layout.spinner_item, priceArray);
                spprice.setAdapter(arrayAdapter);

                if (priceFilter.equalsIgnoreCase("l")) {
                    spprice.setSelection(1);
                } else if (priceFilter.equalsIgnoreCase("h")) {
                    spprice.setSelection(2);
                }


                final AlertDialog mAlert = builder.create();
                mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mAlert.show();

                lnCross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlert.dismiss();

                    }
                });

                rangeSeekBar.setRangeValues(0, 5);
                rangeSeekBar.setSelectedMinValue(minRating);
                rangeSeekBar.setSelectedMaxValue(maxrating);
                tvMin.setText("" + minRating);
                tvMax.setText("" + maxrating);
                rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                        int minRating1 = Integer.parseInt("" + rangeSeekBar.getSelectedMinValue());

                        int maxRating1 = Integer.parseInt("" + rangeSeekBar.getSelectedMaxValue());

                        tvMin.setText("" + minRating1);
                        tvMax.setText("" + maxRating1);
                    }
                });
                tvApplyFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spprice.getSelectedItemPosition() == 0) {
                            priceFilter = "";
                        } else if (spprice.getSelectedItemPosition() == 1) {
                            priceFilter = "l";
                        } else if (spprice.getSelectedItemPosition() == 2) {
                            priceFilter = "h";
                        }
                        mAlert.dismiss();
                        minRating = Integer.parseInt("" + rangeSeekBar.getSelectedMinValue());
                        maxrating = Integer.parseInt("" + rangeSeekBar.getSelectedMaxValue());
                        HashMap<String, String> params = new HashMap<>();
                        params.put("service_id", AppData.sServiceId);
                        params.put("latitude", "" + lat);
                        params.put("longitude", "" + lang);
                        params.put("min_rate", "" + minRating);
                        params.put("max_rate", "" + maxrating);
                        params.put("filter_price", priceFilter);
                        getServiceProviderList(Api.sProviderListByServiceLocation, params);
                    }
                });

            }
        });
        mBinding.ivBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(ServiceProviderActivity.this, Notification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void getCurrentAddress() {
        gps = new GPSTracker(ServiceProviderActivity.this);
        if (gps.canGetLocation) {
            lang = AppData.sSearchLongitiude = my_longitude = gps.getLongitude();
            lat = AppData.sSearchLatitiude = my_latitude = gps.getLatitude();
            sAddress = gps.getAddressLine(ServiceProviderActivity.this); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            sCity = gps.getLocality(ServiceProviderActivity.this);
            sState = gps.getAdminArea(ServiceProviderActivity.this);
            sCountry = gps.getCountryName(ServiceProviderActivity.this);
            sPostalCode = gps.getPostalCode(ServiceProviderActivity.this);
            AppData.sSearchAddress = sAddress;
            mBinding.editWorkLocation.setText(sAddress);
            getData();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotiStatus();
    }
    private void getNotiStatus() {

        new GetDataParser(ServiceProviderActivity.this, Api.sGetNotificationStatus, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {

                if (response != null) {
                    try {

                        String success = response.optString("success");
                        String notification_count = response.optString("notification_count");

                        if (success.equalsIgnoreCase("1")) {

                            if (notification_count.length() > 0) {
                                mBinding.ivNotiDot.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.ivNotiDot.setVisibility(View.GONE);
                            }

                        } else {
                            mBinding.ivNotiDot.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void getData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("service_id", AppData.sServiceId);
        params.put("latitude", "" + lat);
        params.put("longitude", "" + lang);
        params.put("min_rate", "" + minRating);
        params.put("max_rate", "" + maxrating);
        params.put("filter_price", priceFilter);
        getServiceProviderList(Api.sProviderListByServiceLocation, params);
    }

    private void getServiceProviderList(String sUrl, final HashMap<String, String> params) {
        if (!Util.isConnected(ServiceProviderActivity.this)) {
            Util.showSnakBar(ServiceProviderActivity.this, getResources().getString(R.string.internectconnectionerror));
            return;
        }
        dialog = MyCustomProgressDialog.ctor(ServiceProviderActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        showpDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, sUrl,
                new Response.Listener<String>() {
                    public String mStatus;
                    public String mSuccess;

                    @Override
                    public void onResponse(String data) {
                        try {
                            Util util = new Util();
                            JSONObject response = util.getjsonobject(data);

                            Log.i("@@Response", String.valueOf(response));
                            mSuccess = response.optString("success");
                            mStatus = response.optString("msg");
                            mBinding.tvServiceMsg.setText(mStatus);
                            if (mSuccess.equalsIgnoreCase("1")) {
                                providerArrayList = new ArrayList<>();
                                JSONArray jarrData = response.getJSONArray("data");
                                for (int i = 0; i < jarrData.length(); i++) {
                                    JSONObject jsonObject = jarrData.getJSONObject(i);
                                    String sId = jsonObject.optString("lanscaper_id");
                                    String sUserId = jsonObject.optString("user_id");
                                    String sServiceId = jsonObject.optString("service_id");
                                    String sName = jsonObject.optString("name");
                                    String sDescription = jsonObject.optString("description");
                                    String sProfileImage = jsonObject.optString("feature_image");
                                    String sLocation = jsonObject.optString("address");
                                    String city = jsonObject.optString("city");
                                    String state = jsonObject.optString("state");
                                    String sRating = jsonObject.optString("rating");
                                    String min_price = jsonObject.optString("min_price");
                                    String sUserCount = jsonObject.optString("usercount");
                                    String sFavoriteStatus = jsonObject.optString("favorite_status");
                                    ServiceProviderModel objServiceProviderModel =
                                            new ServiceProviderModel(sId, sUserId, sServiceId, sName, sDescription, sProfileImage, sLocation,city,state, sRating, min_price, sUserCount, sFavoriteStatus);
                                    providerArrayList.add(objServiceProviderModel);

                                }
                                if (providerArrayList.size() > 0) {
                                    mBinding.rcvServiceProviderList.setVisibility(View.VISIBLE);
                                    //Log.i("@@ArraySize", String.valueOf(providerArrayList.size()));
                                    mBinding.rcvServiceProviderList.setAdapter(new ServiceProviderAdapter(ServiceProviderActivity.this, providerArrayList));
                                } else {
                                    mBinding.rcvServiceProviderList.setVisibility(View.GONE);
                                }

                            } else {
                                mBinding.rcvServiceProviderList.setVisibility(View.GONE);
                                Toast.makeText(ServiceProviderActivity.this, "" + mStatus, Toast.LENGTH_SHORT).show();
                            }
                            hidepDialog();


                        } catch (Exception e) {
                            e.printStackTrace();
                            // Toast.makeText(ServiceProviderActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                Util.showSnakBar(ServiceProviderActivity.this, getResources().getString(R.string.networkerror));
                VolleyLog.d("Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (AppData.sToken != null) {
                    headers.put("token", AppData.sToken);
                }
                return headers;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(ServiceProviderActivity.this).add(postRequest);


    }

    private void searching() {
        try {
            /*Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/

            Intent intents = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(ServiceProviderActivity.this);
            startActivityForResult(intents, PLACE_AUTOCOMPLETE_REQUEST_CODE);
           /*Intent intent=new Intent(ServiceProviderActivity.this,LocationSearchActivity.class);
           startActivityForResult(intent,PLACE_AUTOCOMPLETE_REQUEST_CODE);*/
        } catch (Exception e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            //if (resultCode==RESULT_OK){
                /*lat=AppData.sSearchLatitiude;
                lang=AppData.sSearchLongitiude;
                sAddress=AppData.sSearchAddress;
                mBinding.editWorkLocation.setText(sAddress);
                HashMap<String, String> params = new HashMap<>();
                params.put("service_id", AppData.sServiceId);
                params.put("latitude", "" + lat);
                params.put("longitude", "" + lang);
                params.put("min_rate", "" + minRating);
                params.put("max_rate", "" + maxrating);
                params.put("filter_price", priceFilter);
                getServiceProviderList(Api.sProviderListByServiceLocation, params);*/
            //}

            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Place:", String.valueOf(place.getLatLng()));
                Log.i("data", place.toString());
                //********Lat lan wise address
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                LatLng location = place.getLatLng();
                try {
                    lat = location.latitude;
                    lang = location.longitude;
                    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String sAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String sCity = addresses.get(0).getLocality();
                    String sState = addresses.get(0).getAdminArea();
                    String sCountry = addresses.get(0).getCountryName();
                    String sPostalCode = addresses.get(0).getPostalCode();

                    Log.i("address", sAddress);

                    mBinding.editWorkLocation.setText(sAddress);
                    HashMap<String, String> params = new HashMap<>();
                    params.put("service_id", AppData.sServiceId);
                    params.put("latitude", "" + lat);
                    params.put("longitude", "" + lang);
                    params.put("min_rate", "" + minRating);
                    params.put("max_rate", "" + maxrating);
                    params.put("filter_price", priceFilter);
                    getServiceProviderList(Api.sProviderListByServiceLocation, params);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                //************

                //finish();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Placeerr:", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}

