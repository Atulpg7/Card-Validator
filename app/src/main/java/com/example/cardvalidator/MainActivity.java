package com.example.cardvalidator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {



    //Url for request
    final static String MY_URL="https://lookup.binlist.net/";



    //Edit text for card numbers
    static EditText card_number_et;


    //Text views for details
    TextView card_txt,card_type_txt,bank_name_txt;


    //Button
    static Button btn_submit;


    //Object of CardDetails class for storing details
    CardDetails details;


    //Layout of details if get data successfully
    static LinearLayout layout_details;


    //Progress Dialog
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Getting references and Initializing
        card_number_et=findViewById(R.id.card_number_et);
        card_txt=findViewById(R.id.card_txt);
        card_type_txt=findViewById(R.id.card_type_txt);
        bank_name_txt=findViewById(R.id.bank_name_txt);
        btn_submit=findViewById(R.id.btn_submit);
        layout_details=findViewById(R.id.layout_details);
        progressDialog=new ProgressDialog(MainActivity.this);
        details=new CardDetails();



        //Using On text change listener for Edittext box
        card_number_et.addTextChangedListener(new TextChangeEditText());




        //PERFORMING BUTTON CLICK FOR GETTING DATA
        btn_submit.setOnClickListener(view -> {


            //Hiding details layout
            layout_details.setVisibility(View.GONE);

            String number = card_number_et.getText().toString().replaceAll("\\s","");
            progressDialog.setMessage("Validating card...");
            progressDialog.show();
            progressDialog.setIndeterminate(false);


            //Fetching data function called
            fetchData(number);


        });

    }



    //Setting data fetched from server
    private void setData() {


        //Setting all details received from server to text boxes
        card_txt.setText(details.getCard());
        card_type_txt.setText(details.getType());
        bank_name_txt.setText(details.getBank());
        layout_details.setVisibility(View.VISIBLE);



        //Again setting null to edit text and image of card
        card_number_et.setText("");
        card_number_et
                .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_card, 0);


        progressDialog.dismiss();

    }




    //Fetching data from server
    private void fetchData(String card_number) {


        //Url to hit with adding card number
        String final_url=MY_URL+card_number;



        //Creating Request object
        StringRequest request = new StringRequest(Request.Method.GET, final_url, response -> {

            try {

                JSONObject object=new JSONObject(response);
                Log.e("Object ==>",object.toString());


                //Getting card type if debit then Toasting
                String type=object.getString("type");
                if (type.equals("debit")){
                    Toast.makeText(this, "Debit cards not allowed!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


                //Getting Card brand
                String card=object.getString("scheme").toUpperCase();
                details.setCard(card);



                //Getting bank name
                JSONObject bank_object= (JSONObject) object.get("bank");
                if (bank_object.length()==0)
                    details.setBank("Not Available");
                else
                    details.setBank(bank_object.getString("name"));



                //Setting card type
                details.setType(type.toUpperCase());



                //Setting the Data
                setData();


            } catch (JSONException e) {
                progressDialog.dismiss();
                Toast.makeText(this, "Json error: "+e, Toast.LENGTH_SHORT).show();
            }


        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Check your card details !! ", Toast.LENGTH_SHORT).show();
        });



        //Sending Request to server
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

}
