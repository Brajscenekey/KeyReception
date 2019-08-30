package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.CardInfoAdapter;
import com.key.keyreception.activity.model.StripeSaveCardResponce;
import com.key.keyreception.helper.PDialog;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.helper.Utility;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccountCollection;

import java.util.HashMap;
import java.util.Map;

public class PaymentdetailActivity extends AppCompatActivity implements View.OnClickListener {

    private StripeSaveCardResponce cardResponce;
    private CardInfoAdapter cardAdapter;
    private ProgressDialog pDialog;
    private Utility utility;
    private RecyclerView recyclerView;
    private Session session;
    private ImageView iv_back;
    private LinearLayout rl_newaddcard,ll_onetime;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentdetail);
        init();
    }


    private void init() {
        session = new Session(this);
        utility = new Utility();
        pDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recycler_view);
        rl_newaddcard = findViewById(R.id.rl_newaddcard);
        ll_onetime = findViewById(R.id.ll_onetime);
        iv_back = findViewById(R.id.iv_back);
        rl_newaddcard.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_onetime.setOnClickListener(this);
        showCreditCardInfo();

        intent = getIntent();

        if (intent.getStringExtra("key").equals("1"))
        {
            ll_onetime.setVisibility(View.VISIBLE);
        }
        else {
            ll_onetime.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {

            showCreditCardInfo();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_newaddcard:
            {
                Intent i = new Intent(PaymentdetailActivity.this, AddPayCardActivity.class);
                startActivityForResult(i, 100);
            }
            break;
            case R.id.ll_onetime:
            {
                Intent i = new Intent(PaymentdetailActivity.this, MakePaymentCardActivity.class);
                startActivityForResult(i, 100);
            }
            break;
            case R.id.iv_back:{
                onBackPressed();
            }
            break;
        }
    }

    ///""""""""" Saved credit card api """"""""//
    @SuppressLint("StaticFieldLeak")
    protected void showCreditCardInfo() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pDialog.show(); }
        cardResponce = new StripeSaveCardResponce();
        new AsyncTask<Void, Void, ExternalAccountCollection>() {
            @Override
            protected ExternalAccountCollection doInBackground(Void... voids) {
                Stripe.apiKey = getResources().getString(R.string.sk_test);
                ExternalAccountCollection customer = null;
                try {
//                    String customer_stripe_id = session.getstripe_customer_id();
                    Map<String, Object> cardParams = new HashMap<String, Object>();
//                  cardParams.put("limit", 5);
                    cardParams.put("object", "card");
                    customer = Customer.retrieve("cus_FiBETGHziAzHtS").getSources().all(cardParams);
                    } catch (StripeException e) {
                    pDialog.dismiss();
                }return customer;
            }

            @Override
            protected void onPostExecute(final ExternalAccountCollection externalAccountCollection) {
                super.onPostExecute(externalAccountCollection);
                pDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (externalAccountCollection != null) {
                            cardResponce = new Gson().fromJson(externalAccountCollection.toJson(), StripeSaveCardResponce.class);
                            Log.e("Size: ", "" + cardResponce.getData().size());
                            if (cardResponce.getData().size() != 0) {
                            }
                            for (int i = 0; i < cardResponce.getData().size(); i++) {
                                cardResponce.getData().get(i).setMoreDetail(true);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaymentdetailActivity.this);
                            cardAdapter = new CardInfoAdapter(PaymentdetailActivity.this, cardResponce.getData(),intent.getStringExtra("key"), new CardInfoAdapter.CardDetailInterface() {
                                @Override
                                //"""""""""" click on holo circle img and then show card details """"""""""//
                                public void oncardDetail(int pos, boolean b) {
                                    for (int j = 0; j < cardResponce.getData().size(); j++) {
                                        if (j == pos) {
                                            cardResponce.getData().get(pos).setMoreDetail(b);
//                                            cardId = cardResponce.getData().get(pos).getId();
                                        } else {
                                            cardResponce.getData().get(j).setMoreDetail(true);
                                        }
                                    }
                                    cardAdapter.notifyDataSetChanged();
                                }

                            }, new CardInfoAdapter.DeleteSaveCard() {
                                @Override
                                public void ondeleteSaveCard(String id) {
                                    removedSaveCardApi(id);

                                }
                            });

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(cardAdapter);

                        }

                    }
                });

            }

        }.execute();
    }


    //""""""""""  Remove Saved credit card """"""""""""//
    @SuppressLint("StaticFieldLeak")
    private void removedSaveCardApi(final String id) {
        if (utility.checkInternetConnection(PaymentdetailActivity.this)) {
            pDialog.show();
            new AsyncTask<Void, Void, Customer>() {
                @Override
                protected Customer doInBackground(Void... voids) {
                    Stripe.apiKey = getResources().getString(R.string.sk_test);

                    Customer customer = null;
                    try {


                        customer = Customer.retrieve("cus_FiBETGHziAzHtS");
                        customer.getSources().retrieve(id).delete();
                    } catch (StripeException e) {
                        e.printStackTrace();
                        pDialog.show();
                        Toast.makeText(PaymentdetailActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    return customer;
                }

                @Override
                protected void onPostExecute(Customer customer) {
                    super.onPostExecute(customer);
                    pDialog.show();
                    if (customer != null) {
                        showCreditCardInfo();
                    }
                }
            }.execute();
        }
    }

}
