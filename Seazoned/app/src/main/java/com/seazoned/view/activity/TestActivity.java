package com.seazoned.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seazoned.R;
import com.seazoned.databinding.ActivityTestBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_test);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://fcm.googleapis.com/fcm/send";
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("to","ctL9UO-Li-4:APA91bGDR2X1ndPs9iT-OCYeJuAX7PsAYtl_-cSrQCEsdyOIIP_hfrh4r36WQV2gu98fYD97wxbbVbG5lJ6-diSIl-gXUB5R1V78nykV4BanUD_7tQjGpTsd3AS2f906qsrXjsvpPlubDnGULSQ3_2aayxEqaN1Y_A");
                    JSONObject data=new JSONObject();
                    data.put("status_name","hi");
                    jsonObject.put("data",data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                    public String success;

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response!=null){
                            try {
                                success=response.optString("success");
                                if (success.equalsIgnoreCase("1")){

                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String,String> header=new HashMap<>();
                        header.put("Content-Type","application/json");
                        header.put("Authorization","key=AIzaSyDuOl77FuwtYyxzXzQ6LSyZJQiBgJc12IU");
                        return header;
                    }

                };
                Volley.newRequestQueue(TestActivity.this).add(jsonObjectRequest);
            }
        });
    }
}
