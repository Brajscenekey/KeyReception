package com.key.keyreception.activity.owner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ActivityAdapter.CardInfoAdapter;
import com.key.keyreception.activity.DetailActivity;
import com.key.keyreception.activity.model.StripeSaveCardResponce;
import com.key.keyreception.connection.RetrofitClient;
import com.key.keyreception.helper.ProgressDialog;
import com.key.keyreception.helper.Utility;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccountCollection;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PaymentdetailActivity extends AppCompatActivity implements View.OnClickListener {

    private StripeSaveCardResponce cardResponce;
    private CardInfoAdapter cardAdapter;
    private ProgressDialog pDialog;
    private Utility utility;
    private RecyclerView recyclerView;
    private Session session;
    private ImageView iv_back;
    private LinearLayout rl_newaddcard, ll_onetime;
    private Intent intent;
    private String paymenttype = "paypal", jobId, receiverId, amount, cardId = "";
    private Button btnpay;
    private int lastClick = 0;


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
        btnpay = findViewById(R.id.btnpay);
        iv_back = findViewById(R.id.iv_back);
        rl_newaddcard.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btnpay.setOnClickListener(this);
        ll_onetime.setOnClickListener(this);
        showCreditCardInfo();

        intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        receiverId = intent.getStringExtra("receiverId");
        amount = session.getamount();
        if (intent.getStringExtra("key").equals("1")) {
            ll_onetime.setVisibility(View.VISIBLE);
            btnpay.setVisibility(View.VISIBLE);
        } else {
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
        switch (view.getId()) {
            case R.id.rl_newaddcard: {
                Intent i = new Intent(PaymentdetailActivity.this, AddPayCardActivity.class);
                startActivityForResult(i, 100);
            }
            break;
            case R.id.ll_onetime: {
                Intent i = new Intent(PaymentdetailActivity.this, MakePaymentCardActivity.class);
                startActivityForResult(i, 100);
            }
            break;
            case R.id.iv_back: {
                onBackPressed();
            }
            break;
            case R.id.btnpay: {
                if (!cardId.isEmpty()) {
                    if (lastClick != R.id.btnpay) {
                        lastClick = R.id.btnpay;

                        jobPaymentApi();
                    }
                } else {
                    alertPayOption();
                }

            }
            break;
        }
    }

    ///""""""""" Saved credit card api """"""""//
    @SuppressLint("StaticFieldLeak")
    protected void showCreditCardInfo() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pDialog.show();
        }
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
                    customer = Customer.retrieve(session.getstripeCustomerId()).getSources().all(cardParams);
                } catch (StripeException e) {
                    pDialog.dismiss();
                }
                return customer;
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
                            cardAdapter = new CardInfoAdapter(PaymentdetailActivity.this, cardResponce.getData(), intent.getStringExtra("key"), new CardInfoAdapter.CardDetailInterface() {
                                @Override
                                //"""""""""" click on holo circle img and then show card details """"""""""//
                                public void oncardDetail(int pos, boolean b) {
                                    for (int j = 0; j < cardResponce.getData().size(); j++) {
                                        if (j == pos) {
                                            cardResponce.getData().get(pos).setMoreDetail(b);
                                            cardId = cardResponce.getData().get(pos).getId();
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


                        customer = Customer.retrieve(session.getstripeCustomerId());
                        customer.getSources().retrieve(id).delete();
                    } catch (StripeException e) {
                        e.printStackTrace();
                        pDialog.show();
                        lastClick =0;
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

    private void jobPaymentApi() {
        pDialog.show();
        String token = session.getAuthtoken();
        String accid = session.getaccountId();
        String cusid = session.getstripeCustomerId();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().jobPayment(token,
                jobId, receiverId, amount, paymenttype, cardId, "card", accid, cusid);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                try {
                    pDialog.dismiss();


                    switch (response.code()) {
                        case 200: {

                            String stresult = Objects.requireNonNull(response.body()).string();
                            Log.d("response", stresult);
                            JSONObject jsonObject = new JSONObject(stresult);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("success")) {
                                Toast.makeText(PaymentdetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PaymentdetailActivity.this, DetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else {
                                lastClick =0;
                                Toast.makeText(PaymentdetailActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        case 400: {
                            @SuppressLint({"NewApi", "LocalSuppress"}) String result = Objects.requireNonNull(response.errorBody()).string();
                            Log.d("response400", result);
                            JSONObject jsonObject = new JSONObject(result);
                            String statusCode = jsonObject.optString("status");
                            String msg = jsonObject.optString("message");
                            if (statusCode.equals("true")) {
                                Toast.makeText(PaymentdetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        case 401:

                            try {
                                session.logout();
                                Toast.makeText(PaymentdetailActivity.this, "Session expired, please login again.", Toast.LENGTH_SHORT).show();
                                Log.d("ResponseInvalid", Objects.requireNonNull(response.errorBody()).string());

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                pDialog.dismiss();


            }
        });

    }

    public void alertPayOption() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentdetailActivity.this);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Please select payment card");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }


}
