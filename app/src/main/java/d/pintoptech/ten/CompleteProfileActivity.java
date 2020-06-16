package d.pintoptech.ten;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.ui.AddBeneficiaries;
import d.pintoptech.ten.util.MaterialSpinnerModelForBank;
import d.pintoptech.ten.util.SpinnerForCountries;

public class CompleteProfileActivity extends AppCompatActivity {

    private CircularProgressButton circularProgressButton;
    private UserInfo userInfo;

    private ArrayList<MaterialSpinnerModelForBank> goodModelArrayList;
    private ArrayList<String> names = new ArrayList<String>();

    private ArrayList<SpinnerForCountries> countriesArrayList;
    private ArrayList<String> countries = new ArrayList<String>();

    private MaterialSpinner spinner,gender,emp,nationality,kids,mstatus;
    private String Bank, Gender, Emp, Nationality, Kids, Mstatus;
    private RelativeLayout UPLOAD_PASSPORT,UPLOAD_ID;
    private EditText DOB, BANK_NUMBER, STATE_ORIGIN, STATE_RESIDENCE, BVN, LG;

    final Calendar myCalendar = Calendar.getInstance();

    private String imageType;

    public Boolean passport = false;
    private Boolean identity = false;
    private String thePassport, theIdentity;
    private ImageView uploaded_passport,uploaded_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userInfo = new UserInfo(this);
        spinner = findViewById(R.id.spinner);
        gender = findViewById(R.id.gender);
        gender.setItems("Select your gender", "Male", "Female", "Others");
        emp = findViewById(R.id.emp);
        emp.setItems("Employment status", "Student", "Unemployed", "Private sector", "Public sector", "self-employed");
        nationality = findViewById(R.id.nationality);
        kids = findViewById(R.id.kids);
        kids.setItems("Number of children", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        mstatus = findViewById(R.id.mstatus);
        mstatus.setItems("Marital status", "Single", "Married", "Widowed", "Engaged", "Divorced");
        DOB = findViewById(R.id.dob);
        BANK_NUMBER = findViewById(R.id.bank_number);
        STATE_ORIGIN = findViewById(R.id.state_of_origin);
        STATE_RESIDENCE = findViewById(R.id.state_of_residence);
        UPLOAD_ID = findViewById(R.id.upload_id);
        UPLOAD_PASSPORT = findViewById(R.id.upload_passport);
        uploaded_id = findViewById(R.id.uploaded_id);
        uploaded_passport = findViewById(R.id.uploaded_passport);
        BVN = findViewById(R.id.bvn);
        LG = findViewById(R.id.lg);

        getBanks();
        getCountries();

        circularProgressButton = findViewById(R.id.submit);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Bank = item;
            }
        });

        gender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Gender = item;
            }
        });

        emp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Emp = item;
            }
        });

        nationality.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Nationality = item;
            }
        });

        kids.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Kids = item;
            }
        });

        mstatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Mstatus = item;
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CompleteProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        UPLOAD_PASSPORT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage("passport");
            }
        });
        UPLOAD_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage("id");
            }
        });
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitdata();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DOB.setText(sdf.format(myCalendar.getTime()));
    }

    private void getBanks(){
        final String email_address = userInfo.getKeyEmail();
        spinner.setItems("Loading banks.....");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_BANK_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                goodModelArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    MaterialSpinnerModelForBank playerModel = new MaterialSpinnerModelForBank();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    playerModel.setName(dataobj.getString("name"));

                                    goodModelArrayList.add(playerModel);

                                }

                                for (int i = 0; i < goodModelArrayList.size(); i++){
                                    names.add(goodModelArrayList.get(i).getName().toString());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, names);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spinner.setAdapter(spinnerArrayAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompleteProfileActivity.this, "Please ensure you have a working internet!", Toast.LENGTH_LONG).show();
                        spinner.setItems("Error loading banks");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(CompleteProfileActivity.this).addToRequestQueue(stringRequest);
    }

    private void getCountries(){
        final String email_address = userInfo.getKeyEmail();
        nationality.setItems("Loading countries");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_COUNTRY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            //if(obj.optString("code").equals(200)){
                            //Log.e("TAG", response);

                            countriesArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("result");

                            for (int i = 0; i < dataArray.length(); i++) {

                                SpinnerForCountries spinnerForCountries = new SpinnerForCountries();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                spinnerForCountries.setName(dataobj.getString("name"));

                                countriesArrayList.add(spinnerForCountries);

                            }

                            for (int i = 0; i < countriesArrayList.size(); i++){
                                countries.add(countriesArrayList.get(i).getName().toString());
                            }

                            try{
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, countries);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                nationality.setAdapter(arrayAdapter);
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                            // }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompleteProfileActivity.this, "Please ensure you have a working internet!", Toast.LENGTH_LONG).show();
                        nationality.setItems("error loading countries");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(CompleteProfileActivity.this).addToRequestQueue(stringRequest);
    }

    private void captureImage(String type){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
        /*
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);*/
        imageType = type;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(imageType.equals("passport")){
                findViewById(R.id.textPassport).setVisibility(View.GONE);
                uploaded_passport = findViewById(R.id.uploaded_passport);
                uploaded_passport.setImageBitmap(bitmap);
                uploaded_passport.setVisibility(View.VISIBLE);
                passport = true;
            }else {
                findViewById(R.id.textId).setVisibility(View.GONE);
                uploaded_id = findViewById(R.id.uploaded_id);
                uploaded_id.setImageBitmap(bitmap);
                uploaded_id.setVisibility(View.VISIBLE);
                identity = true;
            }

        }
    }


    private void submitdata(){
        final String bank_number = BANK_NUMBER.getText().toString();
        final String dob = DOB.getText().toString();
        final String state_of_origin = STATE_ORIGIN.getText().toString();
        final String state_of_residence = STATE_RESIDENCE.getText().toString();
        final String bvn = BVN.getText().toString();
        final String lg = LG.getText().toString();


        if(!passport){
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please capture a passport photograph");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(bvn) || bvn.length() != 11) {
            BVN.setError("Please enter valid bvn");
            BVN.requestFocus();
        }else if(!identity){
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please capture at least one national identity");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(Bank) || Bank.equals("Loading banks.....") || Bank.equals("Error loading banks")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please select your bank");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(bank_number)) {
            BANK_NUMBER.setError("Please enter account number");
            BANK_NUMBER.requestFocus();
        }else if (TextUtils.isEmpty(Gender) || Gender.equals("Select your gender")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please select your gender");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(Emp) || Emp.equals("Employment status")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please select employment status");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(dob)) {
            DOB.setError("Please enter date of birth");
            DOB.requestFocus();
        }else if (TextUtils.isEmpty(Nationality) || Nationality.equals("Loading countries") || Nationality.equals("error loading countries")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please select your nationality");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(state_of_origin)) {
            STATE_ORIGIN.setError("Please enter state of origin");
            STATE_ORIGIN.requestFocus();
        }else if (TextUtils.isEmpty(state_of_residence)) {
            STATE_RESIDENCE.setError("Please enter state of residence");
            STATE_RESIDENCE.requestFocus();
        }else if (TextUtils.isEmpty(lg)) {
            LG.setError("Please enter your local government area");
            LG.requestFocus();
        }else if (TextUtils.isEmpty(Kids) || Kids.equals("Number of children")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please enter number of kids");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if (TextUtils.isEmpty(Mstatus) || Mstatus.equals("Marital status")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
            builder.setMessage("Please select marital status");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else {
            if(passport) convertImagePassport();
            if(identity) convertImageId();

            startAnimation();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.COMPLETE_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            endAnimation();

                            //Log.e("TAG", response);

                            if(response.equalsIgnoreCase("success")){
                                startAnimation();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
                                        builder.setMessage("Account updated successfully");
                                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                startActivity(new Intent(CompleteProfileActivity.this, CreatePinScreen.class));
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                }, 4000);
                            }else {
                                String errorMsg = response;
                                AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
                                builder.setMessage(Html.fromHtml(errorMsg));
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this);
                            builder.setMessage("Please ensure you have a working internet!");
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                            endAnimation();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("passport", thePassport);
                    params.put("user", userInfo.getKeyEmail());
                    params.put("identity", theIdentity);
                    params.put("bank", Bank);
                    params.put("bank_number", bank_number);
                    params.put("gender", Gender);
                    params.put("emp", Emp);
                    params.put("dob", dob);
                    params.put("nationality", Nationality);
                    params.put("state_of_origin", state_of_origin);
                    params.put("state_of_residence", state_of_residence);
                    params.put("kids", Kids);
                    params.put("mstatus", Mstatus);
                    params.put("bvn", bvn);
                    params.put("lg", lg);

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(CompleteProfileActivity.this).addToRequestQueue(stringRequest);
        }


    }

    private void convertImagePassport(){
        uploaded_passport.buildDrawingCache();
        Bitmap bitmap = uploaded_passport.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        thePassport = Base64.encodeToString(image, 0);

    }

    private void convertImageId(){
        uploaded_id.buildDrawingCache();
        Bitmap bitmap = uploaded_id.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        theIdentity = Base64.encodeToString(image, 0);

    }

    public void startAnimation(){
        UPLOAD_PASSPORT.setClickable(false);

        BVN.setClickable(false);
        BVN.setFocusableInTouchMode(false);
        BVN.setFocusable(false);

        LG.setClickable(false);
        LG.setFocusableInTouchMode(false);
        LG.setFocusable(false);

        UPLOAD_ID.setClickable(false);

        spinner.setClickable(false);
        spinner.setFocusableInTouchMode(false);
        spinner.setFocusable(false);

        BANK_NUMBER.setClickable(false);
        BANK_NUMBER.setFocusableInTouchMode(false);
        BANK_NUMBER.setFocusable(false);

        gender.setClickable(false);
        gender.setFocusableInTouchMode(false);
        gender.setFocusable(false);

        emp.setClickable(false);
        emp.setFocusableInTouchMode(false);
        emp.setFocusable(false);

        DOB.setClickable(false);
        DOB.setFocusableInTouchMode(false);
        DOB.setFocusable(false);

        nationality.setClickable(false);
        nationality.setFocusableInTouchMode(false);
        nationality.setFocusable(false);

        STATE_ORIGIN.setClickable(false);
        STATE_ORIGIN.setFocusableInTouchMode(false);
        STATE_ORIGIN.setFocusable(false);

        STATE_RESIDENCE.setClickable(false);
        STATE_RESIDENCE.setFocusableInTouchMode(false);
        STATE_RESIDENCE.setFocusable(false);

        kids.setClickable(false);
        kids.setFocusableInTouchMode(false);
        kids.setFocusable(false);

        mstatus.setClickable(false);
        mstatus.setFocusableInTouchMode(false);
        mstatus.setFocusable(false);

        circularProgressButton.startAnimation();
    }

    public void endAnimation(){
        UPLOAD_PASSPORT.setClickable(true);

        BVN.setClickable(true);
        BVN.setFocusableInTouchMode(true);
        BVN.setFocusable(true);

        LG.setClickable(true);
        LG.setFocusableInTouchMode(true);
        LG.setFocusable(true);

        UPLOAD_ID.setClickable(true);

        spinner.setClickable(true);
        spinner.setFocusableInTouchMode(true);
        spinner.setFocusable(true);

        BANK_NUMBER.setClickable(true);
        BANK_NUMBER.setFocusableInTouchMode(true);
        BANK_NUMBER.setFocusable(true);

        gender.setClickable(true);
        gender.setFocusableInTouchMode(true);
        gender.setFocusable(true);

        emp.setClickable(true);
        emp.setFocusableInTouchMode(true);
        emp.setFocusable(true);

        DOB.setClickable(true);
        DOB.setFocusableInTouchMode(true);
        DOB.setFocusable(true);

        nationality.setClickable(true);
        nationality.setFocusableInTouchMode(true);
        nationality.setFocusable(true);

        STATE_ORIGIN.setClickable(true);
        STATE_ORIGIN.setFocusableInTouchMode(true);
        STATE_ORIGIN.setFocusable(true);

        STATE_RESIDENCE.setClickable(true);
        STATE_RESIDENCE.setFocusableInTouchMode(true);
        STATE_RESIDENCE.setFocusable(true);

        kids.setClickable(true);
        kids.setFocusableInTouchMode(true);
        kids.setFocusable(true);

        mstatus.setClickable(true);
        mstatus.setFocusableInTouchMode(true);
        mstatus.setFocusable(true);

        circularProgressButton.revertAnimation();
    }
}