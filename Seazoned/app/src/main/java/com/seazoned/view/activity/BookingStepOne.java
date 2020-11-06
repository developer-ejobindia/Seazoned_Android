package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityBookingStepOneBinding;
import com.seazoned.databinding.AerationSearchRowBinding;
import com.seazoned.databinding.LawnTreatmentSearchRowBinding;
import com.seazoned.databinding.LeafRemovalSearchRowBinding;
import com.seazoned.databinding.MowingEdgingSearchRowBinding;
import com.seazoned.databinding.PoolcleaingUpkeepSearchRowBinding;
import com.seazoned.databinding.SnowRemovalSearchRowBinding;
import com.seazoned.databinding.SprinklerWinterizingSearchRowBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.MowingAcrAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingStepOne extends AppCompatActivity implements View.OnClickListener {
    ActivityBookingStepOneBinding mBinding;
    private String addedServiceId;
    private String serviceId;
    ArrayList<HashMap<String, String>> arrayList1 = null;
    ArrayList<HashMap<String, String>> arrayList2 = null;
    ArrayList<HashMap<String, String>> arrayList3 = null;
    ArrayList<HashMap<String, String>> arrayList4 = null;
    ArrayList<CheckBox> snowRemovalCheckBox = null;
    private double aceringPrice = 0.0;
    private double grassPrice = 0.0;
    private double poolTypePrice = 0.0;
    private double poolStatePrice = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_booking_step_one);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = getWindow(); // in Activity's onCreate() for instance
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvContinue.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            serviceId = bundle.getString("serviceId");
            addedServiceId = bundle.getString("addedServiceId");
        }
        mBinding.tvServiceName.setText(AppData.sServiceName + " Total");
        mBinding.lnForm.removeAllViews();
        switch (serviceId) {
            case "1":
                MowingEdgingSearchRowBinding mowingEdgingSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.mowing_edging_search_row, mBinding.lnForm, true);
                getMowingEdgingData(mowingEdgingSearchRowBinding);
                mowingEdgingSearchRowBinding.spMowingEdgingLawn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("lawn_area", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                mowingEdgingSearchRowBinding.spMowingEdgingGrass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList2 != null) {
                            try {
                                AppData.finalBookingParams.put("grass_length", arrayList2.get(i).get("service_field_value"));
                                grassPrice = Double.parseDouble(arrayList2.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case "2":
                LeafRemovalSearchRowBinding leafRemovalSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.leaf_removal_search_row, mBinding.lnForm, true);
                getLeafRemovalData(leafRemovalSearchRowBinding);
                leafRemovalSearchRowBinding.spLeafRemovalLawn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("lawn_area", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                leafRemovalSearchRowBinding.spGrass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList2 != null) {
                            try {
                                AppData.finalBookingParams.put("leaf_accumulation", arrayList2.get(i).get("service_field_value"));
                                grassPrice = Double.parseDouble(arrayList2.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case "3":
                LawnTreatmentSearchRowBinding lawnTreatmentSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.lawn_treatment_search_row, mBinding.lnForm, true);
                getLawnTreatmentData(lawnTreatmentSearchRowBinding);
                lawnTreatmentSearchRowBinding.spLawnTreatmentLawn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("lawn_area", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case "4":
                AerationSearchRowBinding aerationSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.aeration_search_row, mBinding.lnForm, true);
                getAerationdata(aerationSearchRowBinding);
                aerationSearchRowBinding.spAerationLawn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("lawn_area", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case "5":
                SprinklerWinterizingSearchRowBinding sprinklerWinterizingSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.sprinkler_winterizing_search_row, mBinding.lnForm, true);
                getSprinklerData(sprinklerWinterizingSearchRowBinding);
                sprinklerWinterizingSearchRowBinding.spSprinklerWinterizingLawn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("lawn_area", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                sprinklerWinterizingSearchRowBinding.spZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList2 != null) {
                            try {
                                AppData.finalBookingParams.put("no_of_zones", arrayList2.get(i).get("service_field_value"));
                                grassPrice = Double.parseDouble(arrayList2.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case "6":
                PoolcleaingUpkeepSearchRowBinding poolcleaingUpkeepSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.poolcleaing_upkeep_search_row, mBinding.lnForm, true);
                getPoolCleaningData(poolcleaingUpkeepSearchRowBinding);
                poolcleaingUpkeepSearchRowBinding.spPoolWaterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("water_type", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice + poolStatePrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                poolcleaingUpkeepSearchRowBinding.spSpaHotTub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList2 != null) {
                            try {
                                AppData.finalBookingParams.put("include_spa", arrayList2.get(i).get("service_field_value"));
                                grassPrice = Double.parseDouble(arrayList2.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice + poolStatePrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                poolcleaingUpkeepSearchRowBinding.spPoolType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList3 != null) {
                            try {
                                AppData.finalBookingParams.put("pool_type", arrayList3.get(i).get("service_field_value"));
                                poolTypePrice = Double.parseDouble(arrayList3.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice + poolStatePrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                poolcleaingUpkeepSearchRowBinding.spPoolState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList4 != null) {
                            try {
                                AppData.finalBookingParams.put("pool_state", arrayList4.get(i).get("service_field_value"));
                                poolStatePrice = Double.parseDouble(arrayList4.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice + poolStatePrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case "7":
                SnowRemovalSearchRowBinding snowRemovalSearchRowBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.snow_removal_search_row, mBinding.lnForm, true);
                getSnowRemovalData(snowRemovalSearchRowBinding);
                snowRemovalSearchRowBinding.spCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList1 != null) {
                            try {
                                AppData.finalBookingParams.put("no_of_cars", arrayList1.get(i).get("service_field_value"));
                                aceringPrice = Double.parseDouble(arrayList1.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                snowRemovalSearchRowBinding.spDriveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList2 != null) {
                            try {
                                AppData.finalBookingParams.put("driveway_type", arrayList2.get(i).get("service_field_value"));
                                grassPrice = Double.parseDouble(arrayList2.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                snowRemovalSearchRowBinding.spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        /*if (arrayList3 != null) {
                            try {
                                AppData.finalBookingParams.put("service_type", arrayList3.get(i).get("service_field_value"));
                                poolTypePrice = Double.parseDouble(arrayList3.get(i).get("service_field_price"));
                                setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                break;
        }

    }

    private void getSnowRemovalData(final SnowRemovalSearchRowBinding snowRemovalSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray snowCar = data.optJSONArray("snow_car");
                            arrayList1 = new ArrayList<>();
                            for (int i = 0; i < snowCar.length(); i++) {
                                JSONObject jsonObject = snowCar.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList1.add(hashMap);
                            }
                            snowRemovalSearchRowBinding.spCar.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));

                            JSONArray snowDrivewayType = data.optJSONArray("snow_driveway_type");
                            arrayList2 = new ArrayList<>();
                            for (int i = 0; i < snowDrivewayType.length(); i++) {
                                JSONObject jsonObject = snowDrivewayType.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList2.add(hashMap);
                            }
                            snowRemovalSearchRowBinding.spDriveType.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList2));

                            JSONArray snowServiceType = data.optJSONArray("snow_service_type");
                            arrayList3 = new ArrayList<>();
                            snowRemovalCheckBox = new ArrayList<>();
                            snowRemovalSearchRowBinding.lnServices.removeAllViews();
                            for (int i = 0; i < snowServiceType.length(); i++) {
                                JSONObject jsonObject = snowServiceType.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList3.add(hashMap);
                                final CheckBox checkBox = new CheckBox(BookingStepOne.this);
                                checkBox.setText(jsonObject.optString("service_field_value"));
                                snowRemovalSearchRowBinding.lnServices.addView(checkBox);
                                snowRemovalCheckBox.add(checkBox);
                                final int finalI = i;
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            if (arrayList3 != null) {
                                                try {
                                                    //AppData.finalBookingParams.put("service_type", arrayList3.get(finalI).get("service_field_value"));
                                                    poolTypePrice = poolTypePrice + Double.parseDouble(arrayList3.get(finalI).get("service_field_price"));
                                                    //setTotalPrice(String.valueOf(Double.parseDouble(AppData.sTotalPrice) + poolTypePrice));
                                                    setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {
                                            if (arrayList3 != null) {
                                                try {
                                                    //AppData.finalBookingParams.put("service_type", arrayList3.get(finalI).get("service_field_value"));
                                                    poolTypePrice = poolTypePrice - Double.parseDouble(arrayList3.get(finalI).get("service_field_price"));
                                                    setTotalPrice(String.valueOf(aceringPrice + grassPrice + poolTypePrice));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            snowRemovalSearchRowBinding.spService.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList3));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getPoolCleaningData(final PoolcleaingUpkeepSearchRowBinding poolcleaingUpkeepSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray poolWaterType = data.optJSONArray("pool_water_type");
                            arrayList1 = new ArrayList<>();
                            for (int i = 0; i < poolWaterType.length(); i++) {
                                JSONObject jsonObject = poolWaterType.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList1.add(hashMap);
                            }
                            poolcleaingUpkeepSearchRowBinding.spPoolWaterType.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));

                            JSONArray poolSpa = data.optJSONArray("pool_spa");
                            arrayList2 = new ArrayList<>();
                            for (int i = 0; i < poolSpa.length(); i++) {
                                JSONObject jsonObject = poolSpa.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList2.add(hashMap);
                            }
                            poolcleaingUpkeepSearchRowBinding.spSpaHotTub.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList2));

                            JSONArray poolType = data.optJSONArray("pool_type");
                            arrayList3 = new ArrayList<>();
                            for (int i = 0; i < poolType.length(); i++) {
                                JSONObject jsonObject = poolType.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList3.add(hashMap);
                            }
                            poolcleaingUpkeepSearchRowBinding.spPoolType.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList3));

                            JSONArray poolState = data.optJSONArray("pool_state");
                            arrayList4 = new ArrayList<>();
                            for (int i = 0; i < poolState.length(); i++) {
                                JSONObject jsonObject = poolState.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList4.add(hashMap);
                            }
                            poolcleaingUpkeepSearchRowBinding.spPoolState.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList4));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getSprinklerData(final SprinklerWinterizingSearchRowBinding sprinklerWinterizingSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray sprinklerAcre = data.optJSONArray("sprinkler_acre");
                            JSONArray sprinklerZone = data.optJSONArray("sprinkler_zone");
                            if (sprinklerAcre.length() > 0) {
                                arrayList1 = new ArrayList<>();
                                for (int i = 0; i < sprinklerAcre.length(); i++) {
                                    JSONObject jsonObject = sprinklerAcre.optJSONObject(i);
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                    hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                    arrayList1.add(hashMap);
                                }
                                sprinklerWinterizingSearchRowBinding.spSprinklerWinterizingLawn.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));
                                sprinklerWinterizingSearchRowBinding.lnZones.setVisibility(View.GONE);
                            }

                            if (sprinklerZone.length() > 0) {
                                arrayList2 = new ArrayList<>();
                                for (int i = 0; i < sprinklerZone.length(); i++) {
                                    JSONObject jsonObject = sprinklerZone.optJSONObject(i);
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                    hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                    arrayList2.add(hashMap);
                                }
                                sprinklerWinterizingSearchRowBinding.spZones.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList2));
                                sprinklerWinterizingSearchRowBinding.lnAcr.setVisibility(View.GONE);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getAerationdata(final AerationSearchRowBinding aerationSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray leafAcre = data.optJSONArray("aeration_acre");
                            arrayList1 = new ArrayList<>();
                            for (int i = 0; i < leafAcre.length(); i++) {
                                JSONObject jsonObject = leafAcre.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList1.add(hashMap);
                            }
                            aerationSearchRowBinding.spAerationLawn.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getLawnTreatmentData(final LawnTreatmentSearchRowBinding lawnTreatmentSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray leafAcre = data.optJSONArray("lawn_acre");
                            arrayList1 = new ArrayList<>();
                            for (int i = 0; i < leafAcre.length(); i++) {
                                JSONObject jsonObject = leafAcre.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList1.add(hashMap);
                            }
                            lawnTreatmentSearchRowBinding.spLawnTreatmentLawn.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getLeafRemovalData(final LeafRemovalSearchRowBinding leafRemovalSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray leafAcre = data.optJSONArray("leaf_acre");
                            arrayList1 = new ArrayList<>();
                            for (int i = 0; i < leafAcre.length(); i++) {
                                JSONObject jsonObject = leafAcre.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList1.add(hashMap);
                            }
                            leafRemovalSearchRowBinding.spLeafRemovalLawn.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));

                            JSONArray leafAccumulation = data.optJSONArray("leaf_accumulation");
                            arrayList2 = new ArrayList<>();
                            for (int i = 0; i < leafAccumulation.length(); i++) {
                                JSONObject jsonObject = leafAccumulation.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList2.add(hashMap);
                            }
                            leafRemovalSearchRowBinding.spGrass.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList2));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getMowingEdgingData(final MowingEdgingSearchRowBinding mowingEdgingSearchRowBinding) {
        HashMap<String, String> params = new HashMap<>();
        params.put("added_service_id", addedServiceId);
        params.put("service_id", serviceId);
        new PostDataParser(BookingStepOne.this, Api.sViewService, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            JSONArray mowingAcre = data.optJSONArray("mowing_acre");
                            arrayList1 = new ArrayList<>();
                            for (int i = 0; i < mowingAcre.length(); i++) {
                                JSONObject jsonObject = mowingAcre.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList1.add(hashMap);
                            }
                            mowingEdgingSearchRowBinding.spMowingEdgingLawn.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList1));

                            JSONArray mowingGrass = data.optJSONArray("mowing_grass");
                            arrayList2 = new ArrayList<>();
                            for (int i = 0; i < mowingGrass.length(); i++) {
                                JSONObject jsonObject = mowingGrass.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("service_field_value", jsonObject.optString("service_field_value"));
                                hashMap.put("service_field_price", jsonObject.optString("service_field_price"));
                                arrayList2.add(hashMap);
                            }
                            mowingEdgingSearchRowBinding.spMowingEdgingGrass.setAdapter(new MowingAcrAdapter(BookingStepOne.this, R.layout.spinner_item, arrayList2));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void setTotalPrice(String totalPrice) {
        try {
            AppData.sTotalPrice = totalPrice;
            double d = Double.parseDouble(totalPrice);
            double tax = Double.parseDouble(AppData.sPercentageRate);
            mBinding.tvTotalPrice.setText("$" + Util.getDecimalTwoPoint(d+((d*tax)/100)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvContinue) {
            if (serviceId.equalsIgnoreCase("7")) {
                String s = "";
                if (snowRemovalCheckBox != null) {
                    for (int i = 0; i < snowRemovalCheckBox.size(); i++) {
                        if (snowRemovalCheckBox.get(i).isChecked()) {
                            s = s + snowRemovalCheckBox.get(i).getText().toString() + ",";
                        }
                    }
                }
                if (s.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Select at least one additional service.", Toast.LENGTH_SHORT).show();
                    return;
                }
                s = s.substring(0, s.length() - 1);
                AppData.finalBookingParams.put("service_type", s);
            }

            AppData.sImageList = new ArrayList<>();
            startActivity(new Intent(BookingStepOne.this, BookingStepTwo.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }
}
