package com.seazoned.view.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityBookingStepTwoBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.AddressListAdapter;
import com.seazoned.view.adapter.SelectImageAdapter;
import com.seazoned.view.adapter.ServiceFrequencyAdapter;
import com.seazoned.view.fragment.CardListFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BookingStepTwo extends AppCompatActivity implements View.OnClickListener {
    ActivityBookingStepTwoBinding mBinding;
    private AddressListAdapter addressListAdapter;
    private String mServiceDate = "";
    private ServiceFrequencyAdapter serviceFrequencyAdapter = null;
    ArrayList<HashMap<String,String>> arrayList=null;
    private String servicetime="";
    private double tempPrice=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_booking_step_two);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = getWindow(); // in Activity's onCreate() for instance
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        try{
            mBinding.tvTax.setText(Util.getDecimalTwoPoint(Double.parseDouble(AppData.sPercentageRate))+" %");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //show card list
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.lnFragment,new CardListFragment(),"CARD");
        transaction.commit();

        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvAddAddress.setOnClickListener(this);
        mBinding.tvCardList.setOnClickListener(this);
        mBinding.tvDate.setOnClickListener(this);
        mBinding.tvTime.setOnClickListener(this);
        mBinding.tvContinue.setOnClickListener(this);
        mBinding.tvUpperContinue.setOnClickListener(this);

        //serviceFrequencyAdapter
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mBinding.rcvFrequency.setLayoutManager(manager);
        mBinding.rcvFrequency.setNestedScrollingEnabled(false);
        if (AppData.sRecurringServiceList != null && AppData.sRecurringServiceList.size() > 0) {
            arrayList=new ArrayList<>();
            for (int i=0;i<AppData.sRecurringServiceList.size();i++){
                if(AppData.sServiceId.equalsIgnoreCase("1")||AppData.sServiceId.equalsIgnoreCase("6")) {
                    if (AppData.sRecurringServiceList.get(i).get("days").equalsIgnoreCase("just once")) {
                        arrayList.add(AppData.sRecurringServiceList.get(i));
                    } else if (!AppData.sRecurringServiceList.get(i).get("price").equals("")
                            && !AppData.sRecurringServiceList.get(i).get("price").equals("0")
                            && !AppData.sRecurringServiceList.get(i).get("price").equals("0.0")
                            && !AppData.sRecurringServiceList.get(i).get("price").equals("0.00")
                            ) {
                        arrayList.add(AppData.sRecurringServiceList.get(i));
                    }
                }
                else {
                    if (AppData.sRecurringServiceList.get(i).get("days").equalsIgnoreCase("just once")) {
                        arrayList.add(AppData.sRecurringServiceList.get(i));
                    }
                }

            }
            if (arrayList.size()>0) {
                mBinding.tvFrequencyAlert.setVisibility(View.GONE);
                mBinding.rcvFrequency.setVisibility(View.VISIBLE);
                mBinding.rcvFrequency.setAdapter(serviceFrequencyAdapter = new ServiceFrequencyAdapter(this, mBinding, arrayList));
            }
            else {
                mBinding.tvFrequencyAlert.setVisibility(View.VISIBLE);
                mBinding.rcvFrequency.setVisibility(View.GONE);
            }
        } else {
            mBinding.tvFrequencyAlert.setVisibility(View.VISIBLE);
            mBinding.rcvFrequency.setVisibility(View.GONE);
        }


        //address list adapter
        mBinding.rcvAddressList.setLayoutManager(new LinearLayoutManager(this));
        addressListAdapter = new AddressListAdapter(this);
        mBinding.rcvAddressList.setAdapter(addressListAdapter);
        mBinding.rcvAddressList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressDetails();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mBinding.rcvImageList.setLayoutManager(manager);
        mBinding.rcvImageList.setNestedScrollingEnabled(false);
        mBinding.rcvImageList.setAdapter(new SelectImageAdapter(this));
        mBinding.rcvImageList.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvAddAddress) {
            startActivity(new Intent(BookingStepTwo.this, AddEditAddressActivity.class)
                    .putExtra("mode", "add")
            );
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if (view==mBinding.tvCardList){
            startActivity(new Intent(BookingStepTwo.this, AddCard.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if (view == mBinding.tvDate) {
            try {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(BookingStepTwo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = (m + 1) + "/" + d + "/" + y;
                        mServiceDate = Util.changeAnyDateFormat(date, "MM/dd/yyyy", "MM/dd/yyyy");
                        mBinding.tvDate.setText(Util.changeAnyDateFormat(mServiceDate, "MM/dd/yyyy", "MMM-dd-yyyy"));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        } else if (view == mBinding.tvTime) {
            showTimePicker(mBinding.tvTime);
        } else if (view == mBinding.tvContinue) {
            try {
                HashMap<String, String> hashMap1 = null;
                if (serviceFrequencyAdapter != null)
                    hashMap1 = serviceFrequencyAdapter.getServiceData();
                if (hashMap1 == null) {
                    Toast.makeText(this, "Select one service frequency.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price=tempPrice = Double.parseDouble(AppData.sTotalPrice) - Double.parseDouble(hashMap1.get("price"));
                AppData.finalBookingParams.put("service_price_id", hashMap1.get("service_price_id"));
                AppData.finalBookingParams.put("service_price", "" + price);

                if (addressListAdapter.getAddressId().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Select/Add one address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                AppData.finalBookingParams.put("address_book_id", addressListAdapter.getAddressId());

                if (TextUtils.isEmpty(mBinding.tvDate.getText().toString())) {
                    Toast.makeText(this, "Input preferred date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mBinding.tvTime.getText().toString())) {
                    Toast.makeText(this, "Input preferred time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                AppData.finalBookingParams.put("service_date", mServiceDate);
                AppData.finalBookingParams.put("service_time", servicetime);
                AppData.finalBookingParams.put("additional_note", mBinding.etMessage.getText().toString().trim());
                AppData.finalBookingParams.put("booking_time",Util.getSystemDateTime());

                checkPrimaryCard();
                //saveBookingData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (view == mBinding.tvUpperContinue) {
            mBinding.tvContinue.performClick();
        }
    }

    private void checkPrimaryCard() {
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",AppData.sUserId);
        new PostDataParser(BookingStepTwo.this, Api.sCheckPrimaryCard, params, true, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response!=null){
                    try {
                        String success=response.optString("success");
                        String msg=response.optString("msg");
                        if (success.equalsIgnoreCase("1")){
                            String message="Your card will not be charged "+serviceFrequencyAdapter.getPrice()+" until after the provider accepts the booking. Once this happens, it will be placed in an escrow account and will not be paid to the provider until after the work has been completed. Once completed, you will be able to release payment to the provider.\n" +
                                    "\n" +
                                    "In the chance your payment method declines, you will have to manually complete the payment process once the provider accepts your service request. Otherwise your service will not be started until payment is successful.";
                            final AlertDialog.Builder builder=new AlertDialog.Builder(BookingStepTwo.this);
                            //builder.setTitle("Seazoned says");
                            builder.setMessage(message);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveBookingData();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        }
                        else {
                            final AlertDialog.Builder builder=new AlertDialog.Builder(BookingStepTwo.this);
                            //builder.setTitle("Seazoned says");
                            builder.setMessage(msg);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveBookingData() {
        new PostDataParser(BookingStepTwo.this, Api.sConfirmBooking, AppData.finalBookingParams, true, mBinding.tvContinue, new PostDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        String msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {


                            JSONObject data = response.optJSONObject("data");
                            String bookServiceId = data.optString("bookServiceId");
                            if (!TextUtils.isEmpty(bookServiceId)) {
                                saveImage(bookServiceId);
                            }
                        }
                        else {
                            Toast.makeText(BookingStepTwo.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveImage(final String bookServiceId) {
        if (AppData.sImageList != null) {
            int i;
            for ( i=0;i < AppData.sImageList.size(); i++) {
                final String path = AppData.sImageList.get(i).getPath();
                try {
                    if (path != null && !path.equals("")) {
                        if (path != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //String url = AppData.host + "/insertPhoto?from_loc=product&product_id=" + AppData.parking_space_product_id + "&source=android&user_id=" + AppData.user_id;
                                        String url = Api.sAddServiceImage + "bookServiceId=" + bookServiceId + "&upload_by=" + AppData.sUserId + "&source=" + "android";
                                        uploadFile(path, url);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(this, "Please Choose a File First", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please Choose a File First", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (i==AppData.sImageList.size()){
                final AlertDialog.Builder builder = new AlertDialog.Builder(BookingStepTwo.this);
                View view = getLayoutInflater().inflate(R.layout.service_confirmation_dialog, null);
                builder.setView(view);
                builder.setCancelable(false);

                TextView tvDone=(TextView)view.findViewById(R.id.tvDone);

                final AlertDialog mAlert = builder.create();
                mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mAlert.show();
                tvDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(new Intent(BookingStepTwo.this,BookingHistoryActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });


            }
        }
    }

    public int uploadFile(final String selectedFilePath, String SERVER_URL) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("property_image", selectedFilePath);
                // connection.setRequestProperty("user_id","4");


                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"property_image\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                //String serverResponseMessage = connection.getResponseMessage();


                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } else {

                    Toast.makeText(BookingStepTwo.this, "Internet Connection Unavailable", Toast.LENGTH_SHORT).show();


                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(BookingStepTwo.this, "File Not Found", Toast.LENGTH_SHORT).show();


                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();

                Toast.makeText(BookingStepTwo.this, "URL ERROR", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(BookingStepTwo.this, "Cannot Read/Write File", Toast.LENGTH_SHORT).show();

            }

            return serverResponseCode;
        }
    }


    private void getAddressDetails() {

        new GetDataParser(BookingStepTwo.this, Api.sAddressList, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String msg, success;
                if (response != null) {
                    msg = response.optString("msg");
                    success = response.optString("success");
                    if (success.equalsIgnoreCase("1")) {
                        JSONObject data = response.optJSONObject("data");
                        JSONArray addresses = data.optJSONArray("addresses");
                        addressListAdapter.clearData();
                        for (int i = 0; i < addresses.length(); i++) {

                            JSONObject jsonObject1 = addresses.optJSONObject(i);
                            String id = jsonObject1.optString("id").trim();
                            String name = jsonObject1.optString("name").trim();
                            String address = jsonObject1.optString("address").trim();
                            String contact_number = jsonObject1.optString("contact_number").trim();
                            String email_address = jsonObject1.optString("email_address").trim();

                            String city = jsonObject1.optString("city").trim();
                            String state = jsonObject1.optString("state").trim();
                            String country = jsonObject1.optString("country").trim();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("addressId", id);
                            hashMap.put("name", name);
                            hashMap.put("address", address);
                            hashMap.put("phoneNo", contact_number);
                            hashMap.put("email", email_address);
                            hashMap.put("city", city);
                            hashMap.put("state", state);
                            hashMap.put("country", country);
                            addressListAdapter.addRow(hashMap);
                        }
                        mBinding.rcvAddressList.setVisibility(View.VISIBLE);
                        mBinding.tvAlert.setVisibility(View.GONE);
                        addressListAdapter.notifyDataSetChanged();
                    } else {
                        mBinding.rcvAddressList.setVisibility(View.GONE);
                        mBinding.tvAlert.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void showTimePicker(final TextView tvTime) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BookingStepTwo.this);
        View view = getLayoutInflater().inflate(R.layout.custom_timepicker, null);
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog mAlert = builder.create();
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.show();
        TextView tvDone=view.findViewById(R.id.tvDone);
        TextView tvCancel=view.findViewById(R.id.tvCancel);
        final TimePicker timePicker=view.findViewById(R.id.timePicker);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;

                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else{
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                servicetime=hour+":"+minute+":00";
                String showTime = "";
                String showMinute = "";
                if (minute < 10) {
                    showMinute = "0" + minute;
                } else {
                    showMinute = "" + minute;
                }
                //AM
                if (hour == 0) {
                    showTime = "12:" + showMinute + " am";
                } else if (hour != 0 && hour < 12) {
                    if (hour < 10)
                        showTime = "0" + hour + ":" + showMinute + " am";
                    else
                        showTime = hour + ":" + showMinute + " am";
                }
                //PM
                else if (hour==12){
                    showTime = hour + ":" + showMinute + " pm";
                }
                else if (hour>12){
                    hour=hour-12;
                    if (hour < 10)
                        showTime = "0" + hour + ":" + showMinute + " pm";
                    else
                        showTime = hour + ":" + showMinute + " pm";
                }
                tvTime.setText(showTime);
                //tvTime.setText(Util.changeAnyDateFormat((hour + ":" + minute), "hh:mm", "hh:mm a"));
                mAlert.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlert.dismiss();
            }
        });

        /*TimePickerDialog dialog=new TimePickerDialog(BookingStepTwo.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                tvTime.setText(Util.changeAnyDateFormat((i + ":" + i1), "hh:mm", "hh:mm a"));
            }
        }, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), false);
        dialog.show();*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }
}
