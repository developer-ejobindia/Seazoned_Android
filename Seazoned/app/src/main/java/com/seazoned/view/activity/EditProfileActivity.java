package com.seazoned.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.databinding.ActivityEditProfileBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.preference.SharedPreferenceHelper;
import com.seazoned.service.util.Util;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditProfileBinding mBinding;
    private String mDateOfBirth = "";
    String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String monthNo[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String day[] = new String[31];
    //String year[] = new String[100];
    ArrayList<String> years = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        Util.setPadding(this, mBinding.mainLayout);

        SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(getApplicationContext());
        String loginType = helper.getUserLoginType();
        if (loginType.equalsIgnoreCase("appLogin")) {
            mBinding.lnChangePwd.setVisibility(View.VISIBLE);
        } else {
            mBinding.lnChangePwd.setVisibility(View.GONE);
        }

        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvDateOfBirth.setOnClickListener(this);
        mBinding.tvUpdate.setOnClickListener(this);
        mBinding.tvChangePwd.setOnClickListener(this);
        setMonth();
        setDay();
        setYear();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fastName = bundle.getString("fname");
            String lastName = bundle.getString("lname");
            String dateofbirth = mDateOfBirth = bundle.getString("dateofbirth");
            String phonenumber = bundle.getString("phonenumber");

            mBinding.etFirstName.setText(fastName);
            mBinding.etLastName.setText(lastName);
            mBinding.etPhoneNumber.setText(phonenumber);
            try {

                String date[] = mDateOfBirth.split("-");
                String _year = date[0];
                String _month = date[1];
                String _day = date[2];
                /*for (int i = 0; i < year.length; i++) {
                    if (year[i].equalsIgnoreCase(_year)) {
                        mBinding.spYear.setSelection(i);
                        break;
                    }
                }*/
                for(int i=0;i<years.size();i++){
                    if (years.get(i).equalsIgnoreCase(_year)) {
                        mBinding.spYear.setSelection(i);
                        break;
                    }
                }

                for (int i = 0; i < monthNo.length; i++) {
                    if (monthNo[i].equalsIgnoreCase(_month)) {
                        mBinding.spMonth.setSelection(i);
                        break;
                    }
                }
                for (int i = 0; i < day.length; i++) {

                    if (day[i].equalsIgnoreCase(_day)) {
                        mBinding.spDay.setSelection(i);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mBinding.tvDateOfBirth.setText(Util.changeAnyDateFormat(dateofbirth, "yyyy-MM-dd", "MMM-dd-yyyy"));
        }

    }

    private void setYear() {
        /*int y = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 100; i++) {
            year[i] = "" + (y - i);
        }*/

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        Collections.sort(years, Collections.reverseOrder());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, years);
        mBinding.spYear.setAdapter(adapter);
    }

    private void setDay() {
        for (int i = 0; i < 31; i++) {
            if (i < 9)
                day[i] = "0" + (i + 1);
            else
                day[i] = "" + (i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, day);
        mBinding.spDay.setAdapter(adapter);
    }

    private void setMonth() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, month);
        mBinding.spMonth.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            startActivity(new Intent(EditProfileActivity.this, ViewProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvDateOfBirth) {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = (m + 1) + "/" + d + "/" + y;
                    mDateOfBirth = Util.changeAnyDateFormat(date, "MM/dd/yyyy", "MM/dd/yyyy");
                    mBinding.tvDateOfBirth.setText(Util.changeAnyDateFormat(mDateOfBirth, "MM/dd/yyyy", "MMM-dd-yyyy"));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        } else if (view == mBinding.tvUpdate) {
            String mFirstname = mBinding.etFirstName.getText().toString();
            String mLastName = mBinding.etLastName.getText().toString();
            String mPhoneNo = mBinding.etPhoneNumber.getText().toString();
            if (TextUtils.isEmpty(mFirstname)) {
                Toast.makeText(this, "Enter first name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mLastName)) {
                Toast.makeText(this, "Enter last name.", Toast.LENGTH_SHORT).show();
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
            mDateOfBirth = monthNo[mBinding.spMonth.getSelectedItemPosition()] + "/" + mBinding.spDay.getSelectedItem().toString() + "/" + mBinding.spYear.getSelectedItem().toString();
            if (TextUtils.isEmpty(mDateOfBirth)) {
                Toast.makeText(this, "Enter date of birth.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Util.isValidBirthday(mDateOfBirth)) {
                Toast.makeText(this, "Enter valid date of birth.", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("first_name", mFirstname);
            params.put("last_name", mLastName);
            params.put("tel", mPhoneNo);
            params.put("dob", mDateOfBirth);
            getUserEditInfo(params);


        } else if (view == mBinding.tvChangePwd) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setCancelable(false);
            final View v = getLayoutInflater().inflate(R.layout.change_password, null);
            final EditText etOldPwd = (EditText) v.findViewById(R.id.etOldPassword);
            final EditText etNewPwd = (EditText) v.findViewById(R.id.etNewPassword);
            final EditText etCnfPwd = (EditText) v.findViewById(R.id.etCnfPassword);
            final TextView tvCancel = (TextView) v.findViewById(R.id.tvCancel);
            final TextView tvsave = (TextView) v.findViewById(R.id.tvSave);


            builder.setView(v);
            builder.setCancelable(false);
            final AlertDialog mAlert = builder.create();
            mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mAlert.show();
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlert.cancel();
                }
            });
            tvsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mOldPwd = etOldPwd.getText().toString();
                    String mNewPwd = etNewPwd.getText().toString();
                    String mConfPwd = etCnfPwd.getText().toString();

                    if (TextUtils.isEmpty(mOldPwd)) {
                        Toast.makeText(EditProfileActivity.this, "Input old password.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mNewPwd)) {
                        Toast.makeText(EditProfileActivity.this, "Input new password.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mConfPwd)) {
                        Toast.makeText(EditProfileActivity.this, "Input confirm password.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!mNewPwd.equals(mConfPwd)) {
                        Toast.makeText(EditProfileActivity.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HashMap<String, String> params = new HashMap<>();
                    params.put("old_pw", mOldPwd);
                    params.put("new_pw", mNewPwd);
                    params.put("conf_pw", mConfPwd);

                    new PostDataParser(EditProfileActivity.this, Api.sChangePassword, params, true, new PostDataParser.OnGetResponseListner() {
                        public String msg;
                        public String success;

                        @Override
                        public void onGetResponse(JSONObject response) {

                            if (response != null) {
                                try {
                                    success = response.optString("success");
                                    msg = response.optString("msg");
                                    if (success.equalsIgnoreCase("1")) {
                                        mAlert.cancel();
                                    }
                                    Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });

        }
    }

    private void getUserEditInfo(final HashMap<String, String> params) {
        new PostDataParser(EditProfileActivity.this, Api.sUserInfoEdit, params, true, mBinding.tvUpdate, new PostDataParser.OnGetResponseListner() {
            public String mStatus;
            public String mSuccess;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        mSuccess = response.optString("success");
                        mStatus = response.optString("msg");
                        if (mSuccess.equalsIgnoreCase("1")) {
                            mBinding.lnBack.performClick();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "" + mStatus, Toast.LENGTH_SHORT).show();
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
}
