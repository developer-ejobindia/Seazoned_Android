package com.seazoned.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityServiceHistoryDetailsBinding;
import com.seazoned.databinding.ServiceHistoryDetailsRowBinding;
import com.seazoned.other.MyCustomProgressDialog;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.PaymentInfoAdapter;
import com.seazoned.view.adapter.RatingListAdapter;
import com.seazoned.view.adapter.RcvImageOneAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ServiceHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityServiceHistoryDetailsBinding mBinding;
    ArrayList<HashMap<String, String>> imageList = null;
    private ArrayList<HashMap<String, String>> mCardList;
    private ArrayList<HashMap<String, String>> mRatingList;
    private String landscaper_id;

    private DecimalFormat _df;
    private ProgressDialog _progressDialog;
    private boolean _progressDialogRunning = false;
    final static public int PAYPAL_BUTTON_ID = 10001;
    private static final int REQUEST_PAYPAL_CHECKOUT = 2;
    private String landscaperPaypalAccount = "";
    private String adminPaypalAccount = "debdas.admin@gmail.com";

    AlertDialog dialog;
    private double percentage = 0;
    private double totalPrice = 0;
    private double commission = 0;
    private String orderNo = "";
    private String bookingId = "";
    private PaymentInfoAdapter cardAdpater = null;

    int currentPage;
    int NUM_PAGES;
    int landscaper_currentPage;
    int landscaper_NUM_PAGES;
    private String favourite_status = "0";
    private int expcount = 0;

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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_service_history_details);
        Util.setPadding(this, mBinding.mainLayout);

        mBinding.lnBack.setOnClickListener(this);
        mBinding.ivFav.setOnClickListener(this);
        mBinding.tvProceed.setOnClickListener(this);
        mBinding.tvAddCard.setOnClickListener(this);
        mBinding.tvPaypal.setOnClickListener(this);
        mBinding.tvPayCard.setOnClickListener(this);
        mBinding.tvConfirm.setOnClickListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mBinding.rcvCardList.setLayoutManager(manager);
        mBinding.rcvCardList.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(this);
        mBinding.rcvRatingList.setLayoutManager(manager1);
        mBinding.rcvRatingList.setNestedScrollingEnabled(false);


        bookingId = getIntent().getExtras().getString("bookingId");
        landscaper_id = getIntent().getExtras().getString("landscaper_id");
        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", bookingId);
        getBookingHistoryDetails(params);

        //get landscaper paypal account details
        HashMap<String, String> params1 = new HashMap<>();
        params1.put("landscaper_id", landscaper_id);
        getLandscaperPaypalAccount(params1);

        //get admin paypal account details
        getAdminAccountInfo();
        mBinding.etExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (expcount <= mBinding.etExpiryDate.getText().toString().length()
                        && (mBinding.etExpiryDate.getText().toString().length() == 2)) {
                    mBinding.etExpiryDate.setText(mBinding.etExpiryDate.getText().toString() + "/");
                    int pos = mBinding.etExpiryDate.getText().length();
                    mBinding.etExpiryDate.setSelection(pos);
                } else if (expcount >= mBinding.etExpiryDate.getText().toString().length()
                        && (mBinding.etExpiryDate.getText().toString().length() == 2)) {
                    mBinding.etExpiryDate.setText(mBinding.etExpiryDate.getText().toString().substring(0, mBinding.etExpiryDate.getText().toString().length() - 1));
                    int pos = mBinding.etExpiryDate.getText().length();
                    mBinding.etExpiryDate.setSelection(pos);
                }
                expcount = mBinding.etExpiryDate.getText().toString().length();
            }
        });

    }

    private void getAdminAccountInfo() {
        new GetDataParser(ServiceHistoryDetailsActivity.this, Api.sAdminAccountInfo, false, true, new GetDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");
                            JSONArray admin_payment_details = data.optJSONArray("admin_payment_details");
                            for (int i = 0; i < admin_payment_details.length(); i++) {
                                JSONObject jsonObject = admin_payment_details.optJSONObject(i);
                                adminPaypalAccount = jsonObject.optString("account_email");
                            }
                            JSONArray admin_percentages = data.optJSONArray("admin_percentages");
                            for (int i = 0; i < admin_percentages.length(); i++) {
                                JSONObject jsonObject = admin_percentages.optJSONObject(i);
                                percentage = jsonObject.optDouble("percentage");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getLandscaperPaypalAccount(HashMap<String, String> params) {
        new PostDataParser(ServiceHistoryDetailsActivity.this, Api.sLandscaperAccountInfo, params, true, true, new PostDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");
                            JSONArray payment_accounts = data.optJSONArray("payment_accounts");
                            for (int i = 0; i < payment_accounts.length(); i++) {
                                JSONObject jsonObject = payment_accounts.optJSONObject(i);
                                landscaperPaypalAccount = jsonObject.optString("account_email");
                                //landscaperPaypalAccount = jsonObject.optString("account_details");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCardList();

    }

    private void getCardList() {
        new GetDataParser(ServiceHistoryDetailsActivity.this, Api.sCardList, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    String msg, success;
                    if (response != null) {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            mCardList = new ArrayList<>();
                            JSONObject data = response.optJSONObject("data");
                            JSONArray payment_accounts = data.optJSONArray("payment_accounts");
                            for (int i = 0; i < payment_accounts.length(); i++) {
                                JSONObject jsonObject1 = payment_accounts.optJSONObject(i);

                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put("card_id", jsonObject1.optString("id"));
                                hashMap.put("name", jsonObject1.optString("name"));
                                hashMap.put("card_brand", jsonObject1.optString("card_brand"));
                                hashMap.put("card_no", jsonObject1.optString("card_no"));
                                hashMap.put("month", jsonObject1.optString("month"));
                                hashMap.put("year", jsonObject1.optString("year"));
                                hashMap.put("cvv_no", jsonObject1.optString("cvv_no"));
                                hashMap.put("is_primary", jsonObject1.optString("is_primary"));

                                mCardList.add(hashMap);
                            }
                            mBinding.rcvCardList.setAdapter(cardAdpater = new PaymentInfoAdapter(ServiceHistoryDetailsActivity.this, mCardList));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvProceed) {
            if (mBinding.ratingBar.getRating() <= 0) {
                Toast.makeText(this, "Please rate the service.", Toast.LENGTH_SHORT).show();
                return;
            }
            String review = mBinding.etReview.getText().toString();
            if (TextUtils.isEmpty(review)) {
                Toast.makeText(this, "Input your review.", Toast.LENGTH_SHORT).show();
                return;
            }
            String rating = "" + mBinding.ratingBar.getRating();
            HashMap<String, String> params = new HashMap<>();
            params.put("landscaper_id", landscaper_id);
            params.put("rating", rating);
            params.put("review", review);
            params.put("rating_time", Util.getSystemDateTime());
            params.put("order_id", bookingId);
            giveReview(Api.sGiveReview, params);

        } else if (view == mBinding.tvAddCard) {
            startActivity(new Intent(ServiceHistoryDetailsActivity.this, AddCard.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.tvPaypal) {
            makePaymentWithPaypal();

        } else if (view == mBinding.tvPayCard) {
            HashMap<String, String> params = new HashMap<>();
            params.put("order_id", bookingId);
            params.put("payment_time", Util.getSystemDateTime());
            HashMap<String, String> hashMap = null;
            if (cardAdpater != null && cardAdpater.getItemCount() > 0) {
                hashMap = cardAdpater.getCardData();
            }
            if (hashMap != null) {
                if (TextUtils.isEmpty(hashMap.get("cvv_no"))){
                    Toast.makeText(this, "Enter cvv no", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("customer_name", hashMap.get("name"));
                params.put("card_number", hashMap.get("card_no"));
                params.put("month", hashMap.get("month"));
                params.put("year", hashMap.get("year"));
                params.put("cvv", hashMap.get("cvv_no"));
            } else {
                String year = "";
                String month = "";
                String cardHolderName = mBinding.etCardHolderName.getText().toString().trim();
                String cardNumber = mBinding.etCardNumber.getText().toString().trim();
                String expiryDate = mBinding.etExpiryDate.getText().toString().trim();
                if (TextUtils.isEmpty(cardHolderName) || TextUtils.isEmpty(cardNumber) ||
                        TextUtils.isEmpty(expiryDate)) {
                    Toast.makeText(this, "Select your saved card or input card details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (expiryDate.contains("/")) {
                    try {
                        String date[] = expiryDate.split("/");

                        month = date[0];
                        if (date.length > 1)
                            year = date[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (year.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Invalid expiry date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("customer_name", cardHolderName);
                params.put("card_number", cardNumber);
                params.put("month", month);
                params.put("year", year);
                params.put("cvv", mBinding.etCVV.getText().toString());
            }

            makPaymentWithCard(params);

        } else if (view == mBinding.ivFav) {
            if (favourite_status.equalsIgnoreCase("0")) {

                HashMap<String, String> params = new HashMap<>();
                params.put("landscaper_id", landscaper_id);
                params.put("status", "1");
                editHeartFavorite(Api.sAddFavorite, params);

            } else if (favourite_status.equalsIgnoreCase("1")) {
                HashMap<String, String> params = new HashMap<>();
                params.put("landscaper_id", landscaper_id);
                params.put("status", "0");
                editHeartFavorite(Api.sRemoveFavorite, params);
            }
        }
        else if (view==mBinding.tvConfirm){
            final android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(ServiceHistoryDetailsActivity.this);
            //builder.setTitle("Seazoned says");
            builder.setMessage("Accepting the job will release payment to the provider. If you do not click accept job completion, the funds will automatically release to the provider 24 hours after the provider marks the service as complete. If you have questions or concerns with the work that was performed, please contact customer service.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    HashMap<String,String> params=new HashMap<>();
                    params.put("order_no",orderNo);
                    params.put("is_completed","2");
                    new PostDataParser(ServiceHistoryDetailsActivity.this, Api.sConfirm, params, true, new PostDataParser.OnGetResponseListner() {
                        @Override
                        public void onGetResponse(JSONObject response) {
                            if (response!=null){
                                try {
                                    String success=response.optString("success");
                                    String msg=response.optString("msg");
                                    Toast.makeText(ServiceHistoryDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    if (success.equalsIgnoreCase("1")){
                                        //mBinding.lnBack.performClick();
                                        HashMap<String, String> params1 = new HashMap<>();
                                        params1.put("order_id", bookingId);
                                        getBookingHistoryDetails(params1);
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }
    }

    private void editHeartFavorite(String url, final HashMap<String, String> params) {
        new PostDataParser(ServiceHistoryDetailsActivity.this, url, params, false, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String success, msg;
                if (response != null) {
                    try {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("order_id", bookingId);
                            getBookingHistoryDetails(params);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void makPaymentWithCard(HashMap<String, String> params) {
        new PostDataParser(ServiceHistoryDetailsActivity.this, Api.sPayUsingCard, params, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        Toast.makeText(ServiceHistoryDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")) {
                            mBinding.lnBack.performClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void makePaymentWithPaypal() {
        //String url = "https://svcs.sandbox.paypal.com/AdaptivePayments/Pay";
        String url = "https://svcs.paypal.com/AdaptivePayments/Pay";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("actionType", "PAY");
            jsonObject.put("currencyCode", "USD");
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("errorLanguage", "");

            jsonObject.put("requestEnvelope", jsonObject1);

            jsonObject.put("returnUrl", Api.sTransactionApproved);
            jsonObject.put("cancelUrl", Api.sTransactionDeclined);

            JSONArray jsonArray = new JSONArray();

            //admin
            //commission = (totalPrice * percentage) / 100;
            commission = totalPrice;
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("amount", commission);
            jsonObject2.put("email", adminPaypalAccount);
            jsonArray.put(jsonObject2);

            //landscaper
            /*JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("amount", totalPrice - commission);
            jsonObject3.put("email", landscaperPaypalAccount);
            jsonArray.put(jsonObject3);*/

            JSONObject jsonObject4 = new JSONObject();
            jsonObject4.put("receiver", jsonArray);

            jsonObject.put("receiverList", jsonObject4);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Util.isConnected(ServiceHistoryDetailsActivity.this)) {
            Util.showSnakBar(ServiceHistoryDetailsActivity.this, getResources().getString(R.string.internectconnectionerror));
            //TastyToast.makeText(context, "No internet connections.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        dialog = MyCustomProgressDialog.ctor(ServiceHistoryDetailsActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        showpDialog();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String paykey = response.optString("payKey");
                    finish();
                    startActivity(new Intent(ServiceHistoryDetailsActivity.this, PaypalActivity.class)
                            .putExtra("paykey", paykey)
                            .putExtra("landscaper_amount", "" + (totalPrice - commission))
                            .putExtra("admin_amount", "" + commission)
                            .putExtra("order_no", orderNo)
                            .putExtra("order_id", bookingId)
                    );
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                Util.showSnakBar(ServiceHistoryDetailsActivity.this, getResources().getString(R.string.networkerror));
                VolleyLog.d("Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("X-PAYPAL-SECURITY-USERID", "info_api1.seazoned.com");
                hashMap.put("X-PAYPAL-SECURITY-PASSWORD", "FWVRDFLQYACNVL9Z");
                hashMap.put("X-PAYPAL-SECURITY-SIGNATURE", "ArrtUvgPwlEBUA7Ks.xJQYk3p3J7AimiArq18xUp3DkUK8d3-AlRCU.D");
                hashMap.put("X-PAYPAL-REQUEST-DATA-FORMAT", "JSON");
                hashMap.put("X-PAYPAL-RESPONSE-DATA-FORMAT", "JSON");
                hashMap.put("X-PAYPAL-APPLICATION-ID", "APP-09F61578JD2993335");

                /*hashMap.put("X-PAYPAL-SECURITY-USERID", "info-facilitator_api1.seazoned.com");
                hashMap.put("X-PAYPAL-SECURITY-PASSWORD", "XZ4V5MSD9Z399UT9");
                hashMap.put("X-PAYPAL-SECURITY-SIGNATURE", "Am82Np6AEAAE8c8ZFN8ZSYXcXhsZAoEeBM273yoDvExLBnXEe-J6zi.L");
                hashMap.put("X-PAYPAL-REQUEST-DATA-FORMAT", "JSON");
                hashMap.put("X-PAYPAL-RESPONSE-DATA-FORMAT", "JSON");
                hashMap.put("X-PAYPAL-APPLICATION-ID", "APP-80W284485P519543T");*/
                return hashMap;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void giveReview(String url, HashMap<String, String> params) {
        new PostDataParser(ServiceHistoryDetailsActivity.this, url, params, true, mBinding.tvProceed, new PostDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            mBinding.lnRating.setVisibility(View.GONE);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("order_id", bookingId);
                            getBookingHistoryDetails(params);
                            //mBinding.lnPaypal.setVisibility(View.VISIBLE);
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

    private void getBookingHistoryDetails(Map<String, String> params) {
        new PostDataParser(ServiceHistoryDetailsActivity.this, Api.sBookingHistoryDetails, params, true, new PostDataParser.OnGetResponseListner() {
            String success;
            String msg;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {
                            JSONObject data = response.optJSONObject("data");

                            favourite_status = data.optString("favourite_status");
                            if (favourite_status.equalsIgnoreCase("1")) {
                                mBinding.ivFav.setImageResource(R.drawable.ic_heart);
                            } else {
                                mBinding.ivFav.setImageResource(R.drawable.ic_favourite_grey);

                            }


                            JSONObject order_details = data.optJSONObject("order_details");

                            mBinding.tvFrequency.setText(order_details.optString("service_frequency"));

                            String service_booked_price = order_details.optString("service_booked_price").trim();

                            String workingStatus = order_details.optString("status").trim();
                            String transaction_id = order_details.optString("transaction_id").trim();

                            String is_completed = order_details.optString("is_completed").trim();

                            mBinding.tvConfirm.setVisibility(View.GONE);

                            if (workingStatus.equalsIgnoreCase("0")&&is_completed.equalsIgnoreCase("0")){
                                //sent or reject
                                mBinding.lnPaypal.setVisibility(View.GONE);//gone
                                mBinding.lnRating.setVisibility(View.GONE);
                            }
                            else if (workingStatus.equalsIgnoreCase("1")&&is_completed.equalsIgnoreCase("0")) {
                                //request accept
                                mBinding.lnPaypal.setVisibility(View.VISIBLE);
                                mBinding.lnRating.setVisibility(View.GONE);
                            }
                            else if (workingStatus.equalsIgnoreCase("2")&&is_completed.equalsIgnoreCase("1")) {
                                mBinding.lnPaypal.setVisibility(View.GONE);
                                mBinding.lnRating.setVisibility(View.GONE);
                            }
                            else if (workingStatus.equalsIgnoreCase("3")&&is_completed.equalsIgnoreCase("1")) {
                                mBinding.lnPaypal.setVisibility(View.GONE);
                                mBinding.lnRating.setVisibility(View.GONE);
                                mBinding.tvConfirm.setVisibility(View.VISIBLE);
                            }
                            else if (workingStatus.equalsIgnoreCase("3")&&is_completed.equalsIgnoreCase("2")) {
                                mBinding.lnPaypal.setVisibility(View.GONE);
                                if (data.optJSONArray("rating_review") != null && data.optJSONArray("rating_review").length() == 0)
                                    mBinding.lnRating.setVisibility(View.VISIBLE);
                                else {
                                    mBinding.lnRating.setVisibility(View.GONE);
                                }
                            }


                            /*if (workingStatus.equalsIgnoreCase("0") || workingStatus.equalsIgnoreCase("-1")

                                    ) {
                                //sent or reject
                                mBinding.lnPaypal.setVisibility(View.GONE);//gone
                                mBinding.lnRating.setVisibility(View.GONE);
                            } else if (workingStatus.equalsIgnoreCase("1")) {
                                //request accept
                                mBinding.lnPaypal.setVisibility(View.VISIBLE);
                                mBinding.lnRating.setVisibility(View.GONE);
                            } else if (workingStatus.equalsIgnoreCase("2")) {

                                mBinding.lnPaypal.setVisibility(View.GONE);
                                mBinding.lnRating.setVisibility(View.GONE);//visible
                            } else if (workingStatus.equalsIgnoreCase("3")) {
                                mBinding.lnPaypal.setVisibility(View.GONE);
                                if (data.optJSONArray("rating_review") != null && data.optJSONArray("rating_review").length() == 0)
                                    mBinding.lnRating.setVisibility(View.VISIBLE);
                                else {
                                    mBinding.lnRating.setVisibility(View.GONE);
                                }
                            }*/


                            orderNo = order_details.optString("order_no").trim();
                            //String landscaper_name = order_details.optString("lanscaper_name").trim();
                            String landscaper_name = order_details.optString("landscaper_business_name").trim();

                            String service_id = order_details.optString("service_id").trim();

                            mBinding.lnServiceDetails.removeAllViews();
                            ServiceHistoryDetailsRowBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.service_history_details_row, mBinding.lnServiceDetails, true);
                            if (service_id.equalsIgnoreCase("1")) {
                                String lawn_area = order_details.optString("lawn_area").trim();
                                String grass_length = order_details.optString("grass_length").trim();
                                binding.tvTitle1.setText("Lawn Size");
                                binding.tvValue1.setText(lawn_area);
                                binding.tvTitle2.setText("Grass Length");
                                binding.tvValue2.setText(grass_length);
                            } else if (service_id.equalsIgnoreCase("2")) {
                                String lawn_area = order_details.optString("lawn_area").trim();
                                String leaf_accumulation = order_details.optString("leaf_accumulation").trim();
                                binding.tvTitle1.setText("Lawn Size");
                                binding.tvValue1.setText(lawn_area);
                                binding.tvTitle2.setText("Leaf Accumulation");
                                binding.tvValue2.setText(leaf_accumulation);
                            } else if (service_id.equalsIgnoreCase("3") || service_id.equalsIgnoreCase("4")) {
                                String lawn_area = order_details.optString("lawn_area").trim();
                                binding.tvTitle1.setText("Lawn Size");
                                binding.tvValue1.setText(lawn_area);
                            } else if (service_id.equalsIgnoreCase("5")) {
                                String lawn_area = order_details.optString("lawn_area").trim();
                                String no_of_zones = order_details.optString("no_of_zones").trim();

                                binding.tvTitle1.setText("Lawn Size");
                                if (lawn_area.equalsIgnoreCase("null") || lawn_area.equalsIgnoreCase("")) {
                                    binding.tvValue1.setText("---");
                                } else {
                                    binding.tvValue1.setText(lawn_area);
                                }

                                binding.tvTitle2.setText("No of Zones");
                                if (no_of_zones.equalsIgnoreCase("null") || no_of_zones.equalsIgnoreCase("")) {
                                    binding.tvValue2.setText("---");
                                } else {
                                    binding.tvValue2.setText(no_of_zones);
                                }
                            } else if (service_id.equalsIgnoreCase("6")) {
                                String water_type = order_details.optString("water_type").trim();
                                String include_spa = order_details.optString("include_spa").trim();
                                String pool_type = order_details.optString("pool_type").trim();
                                String pool_state = order_details.optString("pool_state").trim();
                                binding.tvTitle1.setText("Water Type");
                                binding.tvValue1.setText(water_type);
                                binding.tvTitle2.setText("Include Spa/Hot tub");
                                binding.tvValue2.setText(include_spa);
                                ServiceHistoryDetailsRowBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.service_history_details_row, mBinding.lnServiceDetails, true);
                                binding1.tvTitle1.setText("Pool Type");
                                binding1.tvValue1.setText(pool_type);
                                binding1.tvTitle2.setText("Pool State");
                                binding1.tvValue2.setText(pool_state);
                            } else if (service_id.equalsIgnoreCase("7")) {
                                String no_of_cars = order_details.optString("no_of_cars").trim();
                                String driveway_type = order_details.optString("driveway_type").trim();
                                String service_type = order_details.optString("service_type").trim();
                                binding.tvTitle1.setText("No of Cars");
                                binding.tvValue1.setText(no_of_cars);
                                binding.tvTitle2.setText("Driveway Type");
                                binding.tvValue2.setText(driveway_type);
                                ServiceHistoryDetailsRowBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.service_history_details_row, mBinding.lnServiceDetails, true);
                                binding1.tvTitle1.setText("Service Type");
                                if (service_type.contains(",")) {
                                    service_type = service_type.replace(",", "\n");
                                }
                                binding1.tvValue1.setText(service_type);
                            }


                            String additional_note = order_details.optString("additional_note").trim();
                            String request_date = order_details.optString("service_date").trim();
                            String request_time = order_details.optString("service_time").trim();
                            String completion_date = order_details.optString("completion_date").trim();
                            final String profile_image = order_details.optString("profile_image").trim();
                            String grandtotal = order_details.optString("service_price").trim();
                            grandtotal = Util.getDecimalTwoPoint(Double.parseDouble(grandtotal));
                            totalPrice = order_details.optDouble("service_price");
                            String serviceName = order_details.optString("service_name").trim();

                            JSONObject service_address = data.optJSONObject("service_address");

                            String name = service_address.optString("name").trim();
                            String address = service_address.optString("address").trim();
                            String contact_number = service_address.optString("contact_number").trim();
                            String email_address = service_address.optString("email_address").trim();

                            final JSONArray imageArray = data.optJSONArray("service_images");
                        /*for (int i = 0; i < service_images.length(); i++) {
                            JSONObject jsonObject = service_images.optJSONObject(i);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("image", jsonObject.optString("service_image"));
                            imageList.add(hashMap);
                        }*/
                            if (imageArray.length() > 0) {

                                //commented by me --kamalika

                                /*ArrayList<String> imageList = new ArrayList<>();
                                mBinding.imgDetails.setVisibility(View.GONE);
                                mBinding.vpImage.setVisibility(View.VISIBLE);
                                for (int j = 0; j < imageArray.length(); j++) {
                                    imageList.add(imageArray.optJSONObject(j).getString("service_image"));
                                }
                                setImage(imageList);*/

                                //for recycler view

                                final ArrayList<String> imageList = new ArrayList<>();
                                for (int i = 0; i < imageArray.length(); i++) {
                                    imageList.add(imageArray.optJSONObject(i).getString("service_image"));
                                }
                                RcvImageOneAdapter image1Adapter;

                                mBinding.rcvImageOne.setVisibility(View.VISIBLE);
                                mBinding.rcvImageOne.setLayoutManager(new GridLayoutManager(ServiceHistoryDetailsActivity.this, 2));
                                mBinding.rcvImageOne.setNestedScrollingEnabled(false);
                                mBinding.rcvImageOne.setAdapter(image1Adapter = new RcvImageOneAdapter(ServiceHistoryDetailsActivity.this, imageList));
                                mBinding.tvNoDataFound.setVisibility(View.GONE);
                                image1Adapter.setOnItemClickListner(new RcvImageOneAdapter.OnItemClickListner() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Util.getPopUpWindow(ServiceHistoryDetailsActivity.this, imageList.get(position));

                                    }
                                });

                            } else {

                                mBinding.rcvImageOne.setVisibility(View.GONE);
                                mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                            }

                            JSONArray landscaper_imageArray = data.optJSONArray("landscaper_service_images");
                        /*for (int i = 0; i < service_images.length(); i++) {
                            JSONObject jsonObject = service_images.optJSONObject(i);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("image", jsonObject.optString("service_image"));
                            imageList.add(hashMap);
                        }*/
                            if (landscaper_imageArray.length() > 0) {
                                /*ArrayList<String> imageList1 = new ArrayList<>();
                                 mBinding.imgDetails1.setVisibility(View.GONE);
                                mBinding.vpImage1.setVisibility(View.VISIBLE);
                                for (int j = 0; j < imageArray.length(); j++) {
                                    imageList1.add(landscaper_imageArray.optJSONObject(j).getString("service_image"));
                                }
                                setImage1(imageList1);*/


                                //done by me kamalika

                                final ArrayList<String> imageList1 = new ArrayList<>();

                                for (int i = 0; i < landscaper_imageArray.length(); i++) {
                                    imageList1.add(landscaper_imageArray.optJSONObject(i).getString("service_image"));
                                }
                                RcvImageOneAdapter image2Adapter;

                                mBinding.rcvImageSecond.setVisibility(View.VISIBLE);
                                mBinding.rcvImageSecond.setLayoutManager(new GridLayoutManager(ServiceHistoryDetailsActivity.this, 2));
                                mBinding.rcvImageSecond.setNestedScrollingEnabled(false);
                                mBinding.rcvImageSecond.setAdapter(image2Adapter = new RcvImageOneAdapter(ServiceHistoryDetailsActivity.this, imageList1));
                                mBinding.tvNoDataFoundSecond.setVisibility(View.GONE);
                                image2Adapter.setOnItemClickListner(new RcvImageOneAdapter.OnItemClickListner() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Util.getPopUpWindow(ServiceHistoryDetailsActivity.this, imageList1.get(position));

                                    }
                                });
                            } else {
                                mBinding.tvNoDataFoundSecond.setVisibility(View.VISIBLE);
                                mBinding.rcvImageSecond.setVisibility(View.GONE);

                            }

                            //add image to

                            mBinding.tvServiceName.setText(serviceName);
                            mBinding.tvOrderNo.setText(orderNo);
                            mBinding.tvServiceProviderName.setText(landscaper_name);
                            mBinding.tvServiceRequestDate.setText(Util.changeAnyDateFormat(request_date, "yyyy-MM-dd", "MMM-dd-yyyy") + " " +
                                    Util.changeAnyDateFormat(request_time, "hh:mm:ss", "hh:mm a"));
                            if (completion_date.equalsIgnoreCase("") ||
                                    completion_date.equalsIgnoreCase("null") ||
                                    completion_date.equalsIgnoreCase(null))
                                mBinding.tvCompleteDate.setText("---");
                            else
                                mBinding.tvCompleteDate.setText(Util.changeAnyDateFormat(completion_date, "yyyy-MM-dd", "MMM-dd-yyyy"));
                            mBinding.tvAdditionalNote.setText(additional_note);
                            if (profile_image.equalsIgnoreCase("") || profile_image.equalsIgnoreCase("null") ||
                                    profile_image.equalsIgnoreCase(null))
                                Picasso.with(ServiceHistoryDetailsActivity.this).load(R.drawable.ic_user).placeholder(R.drawable.ic_user).into(mBinding.ivProfile);
                            else {
                                mBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Util.getPopUpWindow(ServiceHistoryDetailsActivity.this, profile_image);
                                    }
                                });
                                Picasso.with(ServiceHistoryDetailsActivity.this).load(profile_image).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).resize(150, 150).into(mBinding.ivProfile);
                            }
                            mBinding.tvServicePrice.setText("$ " + Util.getDecimalTwoPoint(Double.parseDouble(service_booked_price)));
                            mBinding.tvTax.setText(Util.getDecimalTwoPoint(Double.parseDouble(AppData.sPercentageRate))+" %");
                            mBinding.tvGrandToatl.setText("$ " + grandtotal);
                            mBinding.tvTotalPrice.setText("$ " + grandtotal);
                            mBinding.tvPaypal.setText("Pay " + "$" + grandtotal + " with paypal");
                            mBinding.tvPayCard.setText("Pay " + "$" + grandtotal + " now");

                            mBinding.tvName.setText(name);
                            mBinding.tvLandscaperName.setText(landscaper_name);
                            mBinding.tvAddress.setText(address);
                            mBinding.tvPhone.setText(contact_number);
                            mBinding.tvEmail.setText(email_address);

                            //rating
                            JSONArray ratingReview = data.optJSONArray("rating_review");
                            mRatingList = new ArrayList<>();
                            for (int i = 0; i < ratingReview.length(); i++) {
                                JSONObject jsonObject = ratingReview.optJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("name", jsonObject.optString("name"));
                                hashMap.put("profile_picture", jsonObject.optString("profile_picture"));
                                hashMap.put("rating", jsonObject.optString("rating"));
                                hashMap.put("review", jsonObject.optString("review"));
                                hashMap.put("date", jsonObject.optString("date"));
                                mRatingList.add(hashMap);
                            }


                            mBinding.rcvRatingList.setAdapter(new RatingListAdapter(ServiceHistoryDetailsActivity.this, mRatingList));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    //commented by me --kamalika

   /* private void setImage(ArrayList<String> images) {

        mBinding.vpImage.setAdapter(new SlidingImage_Adapter(ServiceHistoryDetailsActivity.this, images));
        mBinding.vpImage.setPageTransformer(true, new AccordionTransformer());

        NUM_PAGES = images.size();
        mBinding.vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mBinding.vpImage.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);


    }*/

    //commented by me kamalika


   /* private void setImage1(ArrayList<String> images) {

        mBinding.vpImage1.setAdapter(new SlidingImage_Adapter(ServiceHistoryDetailsActivity.this, images));
        mBinding.vpImage1.setPageTransformer(true, new AccordionTransformer());

        landscaper_NUM_PAGES = images.size();
        mBinding.vpImage1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                landscaper_currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (landscaper_currentPage == landscaper_NUM_PAGES) {
                    landscaper_currentPage = 0;
                }
                mBinding.vpImage1.setCurrentItem(landscaper_currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);


    }*/


}
