package com.example.josue.sdkshopify;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientBuilder;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;
import com.shopify.buy.utils.AndroidPayHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.logging.HttpLoggingInterceptor;


public class SampleApplication extends Application {

    // Constants store shop
    public static final String BUY_CLIENT_SHOP = "my4mula.myshopify.com";
    public static final String BUY_CLIENT_API_KEY = "8f9b0a235982255bc8f16a9b334ee1ce";
    public static final String BUY_CLIENT_APP_ID = "8";
    public static final String BUY_CLIENT_APP_NAME = "com.example.josue.sdkshopify";


    private static final String SHOP_PROPERTIES_INSTRUCTION =
            "\n\tDeclarar 'BUY_CLIENT_SHOP, API_KEY and APP_ID':\n" +
                    "\t\tSHOP_DOMAIN=<myshop>.myshopify.com\n" +
                    "\t\tAPI_KEY=0123456789abcdefghijklmnopqrstuvw\n";

    private static com.example.josue.sdkshopify.SampleApplication instance;


    public static BuyClient getBuyClient() {
        return instance.buyClient;
    }

    public static Customer getCustomer() {
        return customer;
    }

    public static Shop getShop() { return instance.shop; }

    public static void setCustomer(Customer customer) {
        com.example.josue.sdkshopify.SampleApplication.customer = customer;
    }

    private static Customer customer;
    private BuyClient buyClient;
    private Checkout checkout;
    private PaymentToken paymentToken;
    private Shop shop;


    public static final String ANDROID_PAY_FLOW = "com.shopify.sample.androidpayflow";

    // Use ENVIRONMENT_TEST for testing
    public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initializeBuyClient();
    }

    public void initializeBuyClient() {
        String shopUrl = BUY_CLIENT_SHOP;
        if (TextUtils.isEmpty(shopUrl)) {
            throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "Error SHOP_DOMAIN");
        }

        String shopifyApiKey = BUY_CLIENT_API_KEY;
        if (TextUtils.isEmpty(shopifyApiKey)) {
            throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "Error API_KEY");
        }

        String shopifyAppId = BUY_CLIENT_APP_ID;
        if (TextUtils.isEmpty(shopifyAppId)) {
            throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "Error APP_ID");
        }

        String applicationName = BUY_CLIENT_APP_NAME;

        //final HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor);

        /**
         * Create the BuyClient
         */

        buyClient = new BuyClientBuilder()
                .shopDomain(shopUrl)
                .apiKey(shopifyApiKey)
                .appId(shopifyAppId)
                .applicationName(applicationName)
                .networkRequestRetryPolicy(3, TimeUnit.MILLISECONDS.toMillis(200), 1.5f)
                .build();

        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop) {
                SampleApplication.this.shop = shop;
            }

            @Override
            public void failure(BuyClientError error) {
                Toast.makeText(SampleApplication.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
