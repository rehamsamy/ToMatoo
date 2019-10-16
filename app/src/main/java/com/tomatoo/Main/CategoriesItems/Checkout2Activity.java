package com.tomatoo.Main.CategoriesItems;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.CartProductsModel;
import com.tomatoo.Models.CheckOutListModel;
import com.tomatoo.Models.CheckOutResponseModel;
import com.tomatoo.Models.DeleteCartModel;
import com.tomatoo.Models.PaymentInfoModel;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import com.google.gson.Gson;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;
import com.tomatoo.utils.PreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkout2Activity extends AppCompatActivity {

    @BindView(R.id.drlivery_date_frame_id)
    FrameLayout drlivery_date_frame;
    @BindView(R.id.paymentMethod_frame_id)
    FrameLayout paymentMethod_frame;
    @BindView(R.id.delivery_date_value_txV)
    TextView delivery_date_value_txV;
    @BindView(R.id.payment_method_value_txtV)
    TextView payment_method_value_txtV;
    @BindView(R.id.recycler_order)
    RecyclerView recycler_order;
    @BindView(R.id.price_value_txV)
    TextView price_value_txV;
    @BindView(R.id.delivery_free_value_txV)
    TextView delivery_free_value_txV;
    @BindView(R.id.add_discount_img)
    ImageView add_discount_img;
    @BindView(R.id.discount_value_txtV)
    TextView discount_value_txtV;
    @BindView(R.id.total_price_value_txtV)
    TextView total_price_value_txtV;
    @BindView(R.id.delivery_date_en_arrow_img)
    ImageView date_en_imageV;
    @BindView(R.id.delivery_date_ar_arrow_img)
    ImageView date_ar_imageV;
    @BindView(R.id.payment_method_en_arrow_img)
    ImageView payment_en_imageV;
    @BindView(R.id.payment_method_ar_arrow_img)
    ImageView payment_ar_imageV;

    ArrayList<CheckOutListModel> cart_list;
    ArrayList<CartProductsModel.Product> show_list;
    double items_price, cart_total_cost, delivery_fee;
    private CheckOutRecyclerAdapter checkOutAdapter;
    private DialogUtil dialogUtil;
    private NetworkAvailable networkAvailable;
    private Calendar myCalendar;

    private int payment_request_code = 20;
    private String payment_response = "";
    private int Cart_id;
    String list_obj;
    public static int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        ButterKnife.bind(this);

        dialogUtil = new DialogUtil();
        networkAvailable = new NetworkAvailable(this);
        myCalendar = Calendar.getInstance();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            cart_list = bundle.getParcelableArrayList("card_list");
            show_list = bundle.getParcelableArrayList("show_list");
            items_price = bundle.getDouble("items_price");
            cart_total_cost = bundle.getDouble("order_cost");
            delivery_fee = bundle.getDouble("delivery_fee");
            Cart_id = bundle.getInt("Cart_id");
            Gson gson = new Gson();
            list_obj = gson.toJson(cart_list);
            Log.i("list: ", list_obj);
            Log.i("items_price: ", items_price + "");
            Log.i("cart_total_cost: ", cart_total_cost + "");

            buildOrdersRecycler(cart_list);

            // Set Data To Views ...
            String items_price_AsString = String.format("%.2f", items_price);
            price_value_txV.setText(items_price_AsString);
            price_value_txV.append("$");
            delivery_free_value_txV.setText(String.valueOf(delivery_fee));
            delivery_free_value_txV.append("$");
            cart_total_cost = items_price + delivery_fee;
            String cart_total_cost_AsString = String.format("%.2f", cart_total_cost);
            total_price_value_txtV.setText(cart_total_cost_AsString);
            total_price_value_txtV.append("$");
        }

        if (PreferencesHelper.getSomeStringValue(Checkout2Activity.this).equals("ar")) {
            date_ar_imageV.setVisibility(View.VISIBLE);
            payment_ar_imageV.setVisibility(View.VISIBLE);
            date_en_imageV.setVisibility(View.GONE);
            payment_en_imageV.setVisibility(View.GONE);
        } else {
            date_ar_imageV.setVisibility(View.GONE);
            payment_ar_imageV.setVisibility(View.GONE);
            date_en_imageV.setVisibility(View.VISIBLE);
            payment_en_imageV.setVisibility(View.VISIBLE);
        }
    }

    private void buildOrdersRecycler(ArrayList<CheckOutListModel> cart_list) {
        recycler_order.setLayoutManager(new LinearLayoutManager(this));
        recycler_order.setHasFixedSize(true);

        // Set Adapter ..
        checkOutAdapter = new CheckOutRecyclerAdapter(Checkout2Activity.this, show_list);
        recycler_order.setAdapter(checkOutAdapter);
    }

    @OnClick(R.id.drlivery_date_frame_id)
    void getOrderDate() {
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                delivery_date_value_txV.setText(sdf.format(myCalendar.getTime()));
            }
        };
        DatePickerDialog mDate = new DatePickerDialog(Checkout2Activity.this, datePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDate.show();
    }


    @OnClick(R.id.paymentMethod_frame_id)
    void setPaymentMenthodArrow() {
        Intent intent = new Intent(Checkout2Activity.this, PaymentMethodActivity.class);
        startActivityForResult(intent, payment_request_code);
    }

    @OnClick(R.id.confirm_order_btn)
    void addOrder() {
        if (payment_request_code == 0 || payment_method_value_txtV.getText().toString().equals(getString(R.string.payment_method))) {
            Toast.makeText(this, getString(R.string.choose_payment_first), Toast.LENGTH_LONG).show();
            return;
        }
        if (!payment_response.equals("100") && payment_method_value_txtV.getText().toString().equals(getString(R.string.online_byCreditCard))) {
            Toast.makeText(this, getString(R.string.complete_payment_first), Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog dialog = dialogUtil.showProgressDialog(Checkout2Activity.this, getString(R.string.sending), false);
        Log.i("user_id", MainPageActivity.userData.getId() + "");
        Log.i("token", MainPageActivity.userData.getToken());
        Log.i("items_price", items_price + "");
        Log.i("cart_list", cart_list.size() + "");
        Log.i("paymethod", payment_method_value_txtV.getText().toString());
        Log.i("cart_total_cost", cart_total_cost + "");
        Log.i("list_obj", list_obj);
        Log.i("cart_id", Cart_id + "");
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CheckOutResponseModel> call = serviceInterface.makeCheckOut(MainPageActivity.userData.getId(), items_price, cart_list.size(), payment_method_value_txtV.getText().toString(), cart_total_cost + delivery_fee, MainPageActivity.userData.getToken(), list_obj, Cart_id);
        call.enqueue(new Callback<CheckOutResponseModel>() {
            @Override
            public void onResponse(Call<CheckOutResponseModel> call, Response<CheckOutResponseModel> response) {
                CheckOutResponseModel checkOutResponseModel = response.body();
                dialog.dismiss();
                if (checkOutResponseModel.getStatus()) {
                    order_id = checkOutResponseModel.getOrder_id();
                    MainPageActivity.cart_count = 0;
                    Intent intent = new Intent(Checkout2Activity.this, DoneOrderActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CheckOutResponseModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }


    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == payment_request_code) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String payment_state = data.getStringExtra("payment_state");
                Log.i("payment: ", payment_state);
                if (payment_state.equals("2")) {
                    payment_method_value_txtV.setText(getResources().getString(R.string.cache_on_delivery));
                } else if (payment_state.equals("1")) {
                    final ProgressDialog dialog = dialogUtil.showProgressDialog(Checkout2Activity.this, getString(R.string.loading), false);
                    dialog.show();
                    ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                    Call<PaymentInfoModel> call = apiServiceInterface.getPaymentInfo();
                    call.enqueue(new Callback<PaymentInfoModel>() {
                        @Override
                        public void onResponse(Call<PaymentInfoModel> call, Response<PaymentInfoModel> response) {
                            dialog.dismiss();
                            if (response.body().getStatus()) {
                                List<PaymentInfoModel.PaymentInfo> paymentInfoList = response.body().getPaynment();
                                String merchant_email = paymentInfoList.get(0).getValue();
                                String secret_key = paymentInfoList.get(1).getValue();
                                String site_url = paymentInfoList.get(2).getValue();
                                String ip_merchant = paymentInfoList.get(3).getValue();
                                String secureHash = paymentInfoList.get(4).getValue();
                                openPaymentPage(cart_total_cost, merchant_email, secret_key, site_url, ip_merchant, secureHash);
                            }
                        }

                        @Override
                        public void onFailure(Call<PaymentInfoModel> call, Throwable t) {
                            t.printStackTrace();
                            dialog.dismiss();
                        }
                    });

//                    payment_val_txtV.setText(getResources().getString(R.string.online_byCreditCard));
                } else if (payment_state.equals("3")) {
                    payment_method_value_txtV.setText(getResources().getString(R.string.payment_online_credit));
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE && data != null) {
            Log.e("RESPONSE_CODE: ", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("TRANSACTION_ID: ", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            payment_response = data.getStringExtra(PaymentParams.RESPONSE_CODE);
//            Log.e("AMOUNT: ", data.getDoubleExtra(PaymentParams.AMOUNT, 0.0) + "");
//            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
//                Log.e("TOKEN: ", data.getStringExtra(PaymentParams.TOKEN));
//                Log.e("CUSTOMER_EMAIL: ", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
//                Log.e("CUSTOMER_PASSWORD: ", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
//            }
            if (payment_response.equals("100")) {
                payment_method_value_txtV.setText(getResources().getString(R.string.online_byCreditCard));
                Toast.makeText(this, getString(R.string.payment_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
                deleteCart(Cart_id);
                return;
            }
        } else {
            Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
        }
    }

    //InFlate Payment Page ...
    private void openPaymentPage(Double cart_total_cost, String merchant_email, String
            secret_key, String site_url, String ip_merchant, String secureHash) {
        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, merchant_email); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY, secret_key);//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "PayMent");
        in.putExtra(PaymentParams.AMOUNT, cart_total_cost);

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, MainPageActivity.userData.getPhone());
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, MainPageActivity.userData.getEmail());
        in.putExtra(PaymentParams.ORDER_ID, "12345678");
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

        //Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, getResources().getColor(R.color.colorPrimary));
        in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

        //Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }

    private void deleteCart(int cart_id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DeleteCartModel> call = serviceInterface.deleteCartProducts(cart_id, MainPageActivity.userData.getToken());
        call.enqueue(new Callback<DeleteCartModel>() {
            @Override
            public void onResponse(Call<DeleteCartModel> call, Response<DeleteCartModel> response) {
                DeleteCartModel deleteCartModel = response.body();
                if (deleteCartModel.getStatus()) {
                    Toast.makeText(Checkout2Activity.this, deleteCartModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Checkout2Activity.this, deleteCartModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
