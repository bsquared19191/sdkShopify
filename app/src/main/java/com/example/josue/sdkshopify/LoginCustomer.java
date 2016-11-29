package com.example.josue.sdkshopify;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.CancellableTask;
import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.Shop;
import com.shopify.buy.dataprovider.BuyClientBuilder;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

import com.example.josue.sdkshopify.SampleApplication;

import java.lang.ref.WeakReference;

import butterknife.OnClick;

public class LoginCustomer extends AppCompatActivity {

    public static final String EXTRAS_PENDING_ACTIVITY_INTENT = "pending_activity_intent";
    private static final String LOG_TAG = LoginCustomer.class.getSimpleName();

    Button button_login;
    EditText inputEmail, inputPass;


    private CancellableTask loginCustomerTask;
    private CancellableTask fetchCustomerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_customer);

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPass = (EditText) findViewById(R.id.inputPass);
        button_login = (Button) findViewById(R.id.button_login);

        //presenter.attach(this);
        button_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v)
            {
                String email = inputEmail.getText().toString();
                String pass = inputPass.getText().toString();


                try {
                   final AccountCredentials Credentials = new AccountCredentials(inputEmail.getText().toString(), inputPass.getText().toString());
                    loginCustomerTask = SampleApplication.getBuyClient().loginCustomer(Credentials, new Callback<Customer>() {
                        @Override
                        public void success(final Customer token) {
                            System.out.println("login");
                        }

                        @Override
                        public void failure(final BuyClientError error) {
                            //Log.e("login error:", error.getRetrofitErrorBody());
                            //Toast.makeText(getApplicationContext(), "Fallo el inicio de sesión", Toast.LENGTH_LONG).show();
                            onRequestError(error);
                        }
                    });

                }catch (Exception e)
                {

                }

            }
        });
    }

    private void onFetchCustomerToken(final CustomerToken customerToken) {

        fetchCustomerTask = SampleApplication.getBuyClient().getCustomer(new Callback<Customer>() {
            @Override
            public void success(final Customer customer) {
                onFetchCustomerSuccess(customer);

            }

            @Override
            public void failure(final BuyClientError error) {
                onRequestError(error);
            }
        });
    }

    private void onFetchCustomerSuccess(final Customer customer) {
        SampleApplication.setCustomer(customer);
        Intent myintent = new Intent(LoginCustomer.this,
                LoggedActivity.class);
        startActivity(myintent);
    }

    private void onRequestError(final Throwable t) {

    }
}
