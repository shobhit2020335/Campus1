package com.example.campuscravings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//public class payments extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payments);
//    }
//}


//package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.os.Bundle;

public class payments extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {
    private static final String TAG = "PaymentActivityCC";
    private AlertDialog.Builder alertDialogBuilder;
    String sub_tot, disc, tot_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        Intent intent = getIntent();
        sub_tot = intent.getStringExtra("subtotal");

        disc = intent.getStringExtra("discount");
        String tot_amt1 = intent.getStringExtra("total");

        tot_amt="Rs "+tot_amt1+".00";
//        Log.d("subtotal",sub_tot);
        TextView sub_t_View = (TextView) findViewById(R.id.subtotal_amt_payments_act);
        sub_t_View.setText(sub_tot);
        TextView dic_View = (TextView) findViewById(R.id.discount_amt_payments_act);
        dic_View.setText(disc);
        TextView to_pay_view = (TextView) findViewById(R.id.to_pay_amt_payments_act);
        to_pay_view.setText(tot_amt);
        Checkout.preload(getApplicationContext());

        Button button = (Button) findViewById(R.id.proceed_to_pay_btn);
        Button back_btn_pay = findViewById(R.id.back_btn_payments_activity);
        alertDialogBuilder = new AlertDialog.Builder(payments.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
        back_btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payments.super.onBackPressed();
            }
        });
    }

    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();

        String ApiKey = "rzp_test_rqeKJ0nBRLMPng";
        if (!TextUtils.isEmpty(ApiKey)) {
            co.setKeyID(ApiKey);
        }
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Campus Cravings");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("theme.color", "#80ba03");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", getPayValue(tot_amt));
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", false);
            options.put("retry", retryObj);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }


    }

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String pay_id, PaymentData paymentData) {
        try {
            Toast.makeText(getApplicationContext(), "Payment Success!\nPayment ID: " + pay_id, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //add payment id to database
        Intent i = new Intent(getApplicationContext(), order_confirmed.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
            Intent i_fail = new Intent(getApplicationContext(), order_failed.class);
            i_fail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i_fail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getPayValue(String amt) {
        amt = amt.substring(3);
        amt = amt.replace(".", "");
        return amt;
    }
}