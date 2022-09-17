package com.example.inapppurchase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    ProgressBar progressBar;
    Button btn_buy;
    List<ProductDetails> productDetailsList;
    Activity activity;
    Prefs prefs;
    ProductDetails productDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("product_id_example")
                                                .setProductType(BillingClient.ProductType.SUBS)
                                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(BillingResult billingResult,
                                                         List<ProductDetails> productDetailsList) {
                        // check billingResult
                        // process returned productDetailsList
                    }
                }
        );
        ImmutableList productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(productDetails)
                                // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
                                // for a list of offers that are available to the user
                                .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

// Launch the billing flow
        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
//        btn_buy = findViewById(R.id.btn_buy);
//        btn_buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                billingClient = BillingClient.newBuilder(MainActivity.this)
//                        .enablePendingPurchases()
//                        .setListener(new PurchasesUpdatedListener() {
//                            @Override
//                            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
//                                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null){
//                                    for (Purchase purchase : list){
//                                        verifySubPurchase(purchase);
//                                    }
//                                }
//                            }
//                        }).build();
//                establishConnection();
//            }
//        });
//    }
//
//    public void establishConnection(){
//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingServiceDisconnected() {
//
//            }
//
//            @Override
//            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
//                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
//                    showProducts();
//                }
//            }
//        });
//    }
//
//    public void showProducts(){
//        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
//                QueryProductDetailsParams.Product.newBuilder()
//                        .setProductId("")
//                        .setProductType(BillingClient.ProductType.SUBS)
//                        .build()
//
//        );
//        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
//                .setProductList(productList)
//                .build();
//
//        billingClient.queryProductDetailsAsync(
//                params,
//                ((billingResult, list) -> {
//                    // Process the result
//                    productDetailsList.clear();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.INVISIBLE);
//                            productDetailsList.addAll(list);
//
//
//                        }
//                    },2000);
//                }
//        ));
//    }
//
//    public void launchPurchasFlow(ProductDetails productDetails){
//        assert productDetails.getSubscriptionOfferDetails() != null;
//        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
//                ImmutableList.of(
//                        BillingFlowParams.ProductDetailsParams.newBuilder()
//                                .setProductDetails(productDetails)
//                                .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
//                                .build()
//                );
//        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
//                .setProductDetailsParamsList(productDetailsParamsList)
//                .build();
//
//        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
//    }
//
////    public void onResume() {
////
////        super.onResume();
////        billingClient.queryPurchasesAsync(
////                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
////                (billingResult, list) -> {
////                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
////                        for (Purchase purchase : list) {
////                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
////                                verifySubPurchase(purchase);
////                            }
////                        }
////                    }
////                }
////        );
////    }
//
//    public void verifySubPurchase(Purchase purchase){
//            AcknowledgePurchaseParams acknowledgePurchaseParams  = AcknowledgePurchaseParams
//                    .newBuilder()
//                    .setPurchaseToken(purchase.getPurchaseToken())
//                    .build();
//
//        billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
//            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                //user prefs to set premium
//                Toast.makeText(MainActivity.this, "Subscription activated, Enjoy!", Toast.LENGTH_SHORT).show();
//                //Setting premium to 1
//                // 1 - premium
//                // 0 - no premium
//                prefs.setPremium(1);
//                startActivity(new Intent(this,MainActivity.class));
//                finish();            }
//        });
//        }

    }
    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
        }
    };
    private BillingClient billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build();


    void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }

    private void handlePurchase(Purchase purchase) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
    }
}