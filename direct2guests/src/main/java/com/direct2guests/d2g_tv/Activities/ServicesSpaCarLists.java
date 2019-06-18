package com.direct2guests.d2g_tv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.direct2guests.d2g_tv.Activities.ChatActivity.FrontDeskChatActivity;
import com.direct2guests.d2g_tv.NonActivity.CarSpaCartListAdapter;
import com.direct2guests.d2g_tv.NonActivity.ListAdapter;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class ServicesSpaCarLists extends LangSelectActivity {
    private Variable vdata;
    private NetworkConnection nc = new NetworkConnection();
    private String url, serviceName, servImage, servicesType;
    private TextView ServicesTitle, QuantityText;
    private ImageView ServicesImage, serviceListBG;
    private Button CartButton;
    private ListView CarSpaList, CarSpaCartList;
    private int cartBTN, cartPosition, cartSelButton, serviceID;
    private Button HomeButtonSC, BackButtonSC;
    private TextView title, description, priceTxt, TypeText, typeText;

    boolean err;

    /* ============== Add to Cart Dialog ============== */
    private TextView ProductName, ProductDescription, ProductQuantity, ProductQuantityPrice, GrandTotalText, ConfirmOrder, ConfirmOrder2, CancelOrder2;
    private ImageView ProductImage;
    private RelativeLayout QuantityLayout, CarSpaCartListLayout;
    private Button DeductButton, AddButton, AddCartButton, CancelButton, DeductButtonCart, AddButtonCart;
    private String name, image, price, desc, priceString, SCrate, prodID, url_add_to_cart, url_quantity_deduct, url_quantity_add, url_delete_product, menu_id, url_confirm;
    private int focusCounter, quantity, quantityPrice, pprice;
    JSONObject dataRes;
    /* ===================== End ===================== */

    /* Chat Notification */
    private TextView notif_number;
    private Button notif_button;
    private JsonRequest jsonRequest;
    private String thread_url;
    private String[] msgs, frm;
    private JSONArray array;
    private JSONObject chatobj;
    private RequestQueue queue;
    private int lastCount, resCount, anotherCount;
    private Ringtone r;
    private Uri notification;
    private Thread t;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public final static String PREFERENCE = "MY_PREFERENCE";
    public final static String CHATCOUNT = "CHAT_COUNT";
    public final static String LASTCOUNT = "LAST_COUNT";
    private BroadcastReceiver broadcast_reciever;
    private RelativeLayout chatNotiff;


    private String imagebg_url;
    private TextView GuestNameTxt, FDTitle, FDClock;
    private RelativeLayout CartLayout;
    private ListView CartList;
    private TextView ConfirmButton2, CartCheckOutTitle, GrandTotal;






    // Font path
    String fontPathBold = "fonts/brandonbold.ttf";
    String fontPathReg = "fonts/brandonreg.ttf";
    private FirebaseAnalytics mFirebaseAnalytics;





    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        setContentView(R.layout.activity_services_spa_car_lists);

        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "ServicesSpaCarLists");
        params.putString("app_event", "Viewing Services Spa Car Lists ");
        mFirebaseAnalytics.logEvent("app_activity", params);



        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        serviceID = Integer.parseInt(getIntent().getStringExtra(ServicesActivity.SERVICE_ID));
        serviceName = getIntent().getStringExtra(ServicesActivity.SERVICE_NAME);
        servImage = getIntent().getStringExtra(ServicesActivity.SERVICE_IMAGE);
        ServicesTitle = findViewById(R.id.SCTitle);
        ServicesImage = findViewById(R.id.SCImage);
        CartButton = findViewById(R.id.CartButtonSC);
        QuantityText = findViewById(R.id.quatityIconTxt);
        CarSpaList= findViewById(R.id.SpaCarListview);


//        CarSpaCartList = findViewById(R.id.CarSpaCartLists);
//        CartLayoutCartLayout = findViewById(R.id.SCCartLayout);
//        GrandTotalText = findViewById(R.id.grandTotal);
//        ConfirmOrder = findViewById(R.id.confirmOrderTxt);



//        ConfirmOrder2 = findViewById(R.id.confirmOrderTxt);
        typeText = findViewById(R.id.typeText);
//        typeText.setText(serviceName);
        ServicesTitle.setText(serviceName);
        String image_url = vdata.getServerURL() + servImage;
        Picasso.with(getApplicationContext()).load(image_url).fit().into(ServicesImage);
        HomeButtonSC = findViewById(R.id.HomeBtnSC);
        BackButtonSC = findViewById(R.id.BackButtonSC);


        serviceListBG = findViewById(R.id.serviceListBG);
//        FDTitle = findViewById(R.id.FDTitle);
        FDClock = findViewById(R.id.textClock5);

        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);

        servicesType = "services";


        chatNotiff = findViewById(R.id.notif);
        // Loading Font Face

        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);





        //title = findViewById(R.id.title);
        //description = findViewById(R.id.description);
        //priceTxt = findViewById(R.id.priceTxt);


        GuestNameTxt = findViewById(R.id.guestNameTxtV5);

        //Applying font
        ServicesTitle.setTypeface(fontBold);
//        typeText.setTypeface(fontBold);
//        title.setTypeface(fontBold);
//        description.setTypeface(fontReg);
//        priceTxt .setTypeface(fontBold);
        FDClock.setTypeface(fontBold);
//        GrandTotalText.setTypeface(fontBold);


        onFocusIcons();

//        ConfirmOrder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//                    view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.questyellow));
//                    view.setOnKeyListener(new View.OnKeyListener() {
//                        @Override
//                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                            if(i == KeyEvent.KEYCODE_DPAD_UP && KeyEvent.ACTION_DOWN == keyEvent.getAction()){
//                                cartSelButton = 0;
//                                if(CarSpaCartList.getCount() == 0){
//                                    CartButton.requestFocus();
//                                }else {
//                                    cartPosition = CarSpaCartList.getAdapter().getCount() - 1;
//                                }
//                                getCart();
//                            }
//                            return false;
//                        }
//                    });
//                }else{
//                    view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.questpurple));
//                }
//            }
//        });
//        ConfirmOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JSONObject postData = new JSONObject();
//                String[] tol = GrandTotalText.getText().toString().split(" ");
//                int total = (int)Double.parseDouble(tol[1]);
//                try {
//                    postData.put("guest_id", vdata.getGuestID());
//                    postData.put("service_id", serviceID);
//                    postData.put("grand_total", total);
//                    postData.put("hotel_id", vdata.getHotelID());
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//                url_confirm = vdata.getApiUrl() + "confirmservices.php";
//                nc.postdataObject(url_confirm, getApplicationContext(),postData, new VolleyCallback() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        Toast.makeText(getApplicationContext(), "Thank you! Your order has been placed.", Toast.LENGTH_LONG).show();
//                        cartSelButton = 0;
//                        cartPosition = 0;
//                        getCart();
//                    }
//
//                    @Override
//                    public void onError(VolleyError error) {
//
//                    }
//                });
//            }
//        });



        cartBTN = 1; cartPosition = 0; cartSelButton = 0;
        CartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cartBTN == 1){
                    cartBTN = 2;
//                    CarSpaList.setVisibility(View.GONE);
//                    CarSpaCartListLayout.setVisibility(View.VISIBLE);
                    cartPosition = 0;
                    cartSelButton = 0;
                    getCart();
//                    view.setBackgroundResource(R.drawable.back_logo);
//                    QuantityText.setVisibility(View.INVISIBLE);
                }else if(cartBTN == 2){
                    cartBTN = 1;
//                    CarSpaList.setVisibility(View.VISIBLE);
//                    CarSpaCartListLayout.setVisibility(View.INVISIBLE);
//                    view.setBackgroundResource(R.drawable.cart_logo);
//                    QuantityText.setVisibility(View.VISIBLE);
                    setCartNumber();
                }
            }
        });
    }

    private void onFocusIcons(){
        HomeButtonSC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),ServicesSpaCarLists.this);
            }
        });

        BackButtonSC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),ServicesSpaCarLists.this);

            }
        });

        CartButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //vdata.focusIcons(view,hasFocus,getApplicationContext(),ServicesSpaCarLists.this);
                vdata.focusAnim(view,hasFocus,getApplicationContext(),ServicesSpaCarLists.this);
            }
        });
        HomeButtonSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < vdata.getHotelAccess().length; i++) {
                    if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                        t.interrupt();
                    }
                }
                Intent i = new Intent(ServicesSpaCarLists.this, LauncherActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                startActivity(i);
                i = new Intent("finish_services");
                sendBroadcast(i);
                i = new Intent("finish_hotelservices");
                sendBroadcast(i);
                finish();
            }
        });
        BackButtonSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                setNotif();
                chatNotiff.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
//        broadcast_reciever = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent intent) {
//                String action = intent.getAction();
//                if (action.equals("finish_servicesdetails")) {
//                    //finishing the activity
//                    finish();
//                }
//            }
//        };
//        registerReceiver(broadcast_reciever, new IntentFilter("finish_servicesdetails"));
        setCartNumber();
        getList();
        CarSpaList.requestFocus();
        if(sharedPreferences.contains(CHATCOUNT)){
            anotherCount = Integer.parseInt(sharedPreferences.getString(CHATCOUNT, ""));
        }
        if(sharedPreferences.contains(LASTCOUNT)){
            lastCount = Integer.parseInt(sharedPreferences.getString(LASTCOUNT, ""));
        }
        if(anotherCount <= 0) {
            notif_number.setVisibility(View.INVISIBLE);
        }else{
            notif_number.setText(Integer.toString(anotherCount));
            notif_number.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                setNotif();
                chatNotiff.setVisibility(View.VISIBLE);
            }
        }
        notif_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notif_number.setVisibility(View.INVISIBLE);
                anotherCount = 0;
                editor.putString(CHATCOUNT, Integer.toString(anotherCount));
                editor.commit();
                t.interrupt();
                Intent i = new Intent(ServicesSpaCarLists.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),ServicesSpaCarLists.this);
            }
        });

    }
    @Override
    public void onBackPressed(){
//        if(cartBTN == 2){
//            cartBTN = 1;
////            CarSpaList.setVisibility(View.VISIBLE);
////            CarSpaCartListLayout.setVisibility(View.GONE);
////            CartButton.setBackgroundResource(R.drawable.cart_logo);
////            QuantityText.setVisibility(View.VISIBLE);
//        }else{
//            /*if(servicesType.equals("spa")){
//                Intent i = new Intent(this, ServicesActivity.class);
//                i.putExtra(Variable.EXTRA, vdata);
//                i.putExtra(ServicesActivity.SERVICES_TYPE, servicesType);
//                startActivity(i);
//            }else if(servicesType.equals("car")){
//                Intent i = new Intent(this, ServicesActivity.class);
//                i.putExtra(Variable.EXTRA, vdata);
//                i.putExtra(ServicesActivity.SERVICES_TYPE, servicesType);
//                startActivity(i);
//            }*/
//            for (int i = 0; i < vdata.getHotelAccess().length; i++) {
//                if (vdata.getHotelAccess()[i].equals("chat_acc")) {
//
//                    t.interrupt();
//                }
//            }
//
//
//        }
        super.onBackPressed();
    }


    // disable home button
    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            ToastHomeButton();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    // end of disable home button


    protected void getList(){
        Locale locale;
        locale = getCurrentLanguage();
        String Lang = String.valueOf(locale);

        if(String.valueOf(Lang).equals("en")){
            url = vdata.getApiUrl() + "servicesmenu.php?service_id=" + serviceID + "&hotel_id=" + vdata.getHotelID();
           // Toast.makeText(getApplicationContext(), String.valueOf(url), Toast.LENGTH_LONG).show();
        } else {
            url = vdata.getApiUrl() + "translations/service_menu_translation_" + serviceID + "_" + vdata.getHotelID() + "_" + Lang + ".json";
           // Toast.makeText(getApplicationContext(), String.valueOf(url), Toast.LENGTH_LONG).show();
        }


        nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(JSONArray response) {
                    if(response.equals(null)){
                        CarSpaList.setAdapter(null);
                    }else {
                        final JSONArray results = response;
                        ArrayList<JSONObject> listItems = getArrayListFromJSONArray(results);
                        String serverss_url = vdata.getServerURL().toString();
                        final ListAdapter adapter = new ListAdapter(getApplicationContext(),vdata, R.layout.list_row_car_spa, servicesType, listItems, serverss_url);

                        CarSpaList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        CarSpaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                Log.d("position", Integer.toString(pos));
                                adapter.setPosition(pos);
                                adapter.notifyDataSetChanged();

                                try {
                                    dataRes = adapter.getItemJSONObject();
                                    image = dataRes.getString("imageurl");
                                    name = dataRes.getString("name");
                                    priceString = dataRes.getString("priceString");
                                    price = dataRes.getString("price");
                                    desc = dataRes.getString("desc");
                                    prodID = dataRes.getString("product_id");
                                    SCrate = dataRes.getString("duration");
                                    focusCounter = 3;
                                    quantity = 1;
                                    Log.d("JSONObject", image + dataRes.toString());
                                    quantityPrice = (int)Double.parseDouble(price);
                                    pprice = (int)Double.parseDouble(price);




                                    final Dialog dialog = new Dialog(ServicesSpaCarLists.this);
                                    dialog.setContentView(R.layout.add_cart_product_dialog);
                                    //start code hide status bar
                                    View decorView = getWindow().getDecorView();
                                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                    decorView.setSystemUiVisibility(uiOptions);




                                    // Loading Font Face
                                    Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
                                    Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

                                    //end code hide status bar
                                    ProductName = dialog.findViewById(R.id.prodName);
                                    ProductDescription = dialog.findViewById(R.id.prodDescription);
                                    ProductQuantity = dialog.findViewById(R.id.prodQuantity);
                                    ProductQuantityPrice = dialog.findViewById(R.id.quantityPrice);
                                    AddCartButton = dialog.findViewById(R.id.addCartButton);
                                    CancelButton = dialog.findViewById(R.id.cancelCart);
                                    //Applying font
                                    ProductName.setTypeface(fontBold);
                                    ProductDescription.setTypeface(fontReg);
                                    ProductQuantity.setTypeface(fontReg);
                                    ProductQuantityPrice.setTypeface(fontBold);
                                    AddCartButton.setTypeface(fontBold);


                                    DeductButton = dialog.findViewById(R.id.deductQuantity);
                                    AddButton = dialog.findViewById(R.id.addQuantity);



                                    ProductImage = dialog.findViewById(R.id.prodImage);
                                    QuantityLayout = dialog.findViewById(R.id.quanta);
                                    QuantityLayout.setVisibility(View.GONE);
                                    if (image.isEmpty()) {
                                        Picasso.with(getApplicationContext()).load(R.drawable.no_image).resize(500, 300).into(ProductImage);
                                    } else {
                                        Log.d("ImageURL", image);
                                        Picasso.with(getApplicationContext()).load(image).resize(500, 300).into(ProductImage);
                                    }
                                    ProductName.setText(name);
                                    ProductDescription.setText(desc);
                                    ProductQuantityPrice.setText(priceString + " / " + SCrate);
                                    Log.d("PRICE ", priceString + " / " + SCrate);

                                    AddButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    AddButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            addQ();
                                            ProductQuantityPrice.setText(vdata.getCurrency()+" " + quantityPrice + SCrate);
                                            ProductQuantity.setText(Integer.toString(quantity));
                                        }
                                    });
                                    DeductButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    DeductButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            deductQ();
                                            ProductQuantityPrice.setText(vdata.getCurrency()+" " + quantityPrice + SCrate);
                                            ProductQuantity.setText(Integer.toString(quantity));
                                        }
                                    });
                                    AddCartButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    AddCartButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            addCart();
                                            dialog.dismiss();
                                            //start code hide status bar
                                            View decorView = getWindow().getDecorView();
                                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                            decorView.setSystemUiVisibility(uiOptions);
                                            //end code hide status bar
                                        }
                                    });
                                    CancelButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    CancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            //start code hide status bar
                                            View decorView = getWindow().getDecorView();
                                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                            decorView.setSystemUiVisibility(uiOptions);
                                            //end code hide status bar
                                        }
                                    });
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                                            // TODO Auto-generated method stub
                                            Log.d("Key Pressed", Integer.toString(keyCode));
                                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                dialog.dismiss();
                                                //start code hide status bar
                                                View decorView = getWindow().getDecorView();
                                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                                decorView.setSystemUiVisibility(uiOptions);
                                                //end code hide status bar
                                            }
                                            return false;
                                        }
                                    });
//                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.show();
                                    AddCartButton.requestFocus();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        CarSpaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                adapter.setPosition(i);
                                adapter.notifyDataSetChanged();

                                try {
                                    imagebg_url = listItems.get(i).getString("img_path");
                                    String imageLink = vdata.getServerURL() + imagebg_url;
                                    Picasso.with(getApplicationContext()).load(imageLink).fit().into(serviceListBG);
//                                    GuestNameTxt.setText(imageLink);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void getCart(){
        url = vdata.getApiUrl() + "servicescart.php?guest_id=" + vdata.getGuestID() + "&service_id=" + serviceID;
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.equals(null)){
                        CarSpaCartList.setAdapter(null);
                    }else {

                        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
                        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);


                        ///// BUANG KA SA INYONG BALAY!!!!
                        final Dialog dialog2 = new Dialog(ServicesSpaCarLists.this);
                        dialog2.setContentView(R.layout.cart_confirm_dialog);
                        //start code hide status bar
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                        //end code hide status bar

                        CartLayout = dialog2.findViewById(R.id.confCartLayout);
                        CarSpaCartList = dialog2.findViewById(R.id.confCarSpaCartLists);
                        ConfirmOrder2 = dialog2.findViewById(R.id.confConfirmOrderTxt);
                        CancelOrder2 = dialog2.findViewById(R.id.confCancelOrderTxt);
                        CartCheckOutTitle = dialog2.findViewById(R.id.confCartTxt);
                        GrandTotalText = dialog2.findViewById(R.id.confGrandTotal);



                        CartCheckOutTitle.setTypeface(fontBold);
                        ConfirmOrder2.setTypeface(fontBold);
                        CancelOrder2.setTypeface(fontBold);
                        GrandTotalText.setTypeface(fontBold);



                        JSONObject results = response;
                        final JSONArray products = results.getJSONArray("cart");
                        GrandTotalText .setText("P " + results.getString("cart_grand_total") + ".00");
                        ArrayList<JSONObject> listItems = getArrayListFromJSONArray(products);
                        final CarSpaCartListAdapter cartAdapter = new CarSpaCartListAdapter(getApplicationContext(),vdata, R.layout.list_row_cart_car_spa, servicesType, listItems);
                        cartAdapter.setButton(cartSelButton);
                        cartAdapter.setPosition(cartPosition);
                        CarSpaCartList.setAdapter(cartAdapter);
                        cartAdapter.notifyDataSetChanged();
                        CarSpaCartList.setSelection(cartPosition);




                        CarSpaCartList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                cartAdapter.setPosition(i);
                                cartAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                cartPosition = 0;
                            }
                        });
                        CarSpaCartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                                final int viewID = view.getId();
//                                if (viewID == R.id.deductButton) {
//                                    JSONObject data = cartAdapter.getItemJSONObject();
//                                    try {
//                                        data.put("service_id", serviceID);
//                                        data.put("guest_id", vdata.getGuestID());
//                                        data.put("hotel_id", vdata.getHotelID());
//                                    }catch (JSONException e){
//                                        e.printStackTrace();
//                                    }
//                                    cartSelButton = 0;
//                                    cartPosition = i;
//                                    if (servicesType == "services") {
//                                        url_quantity_deduct = vdata.getApiUrl() + "deductservicecart.php";
//                                    }
//                                    nc.postdataObject(url_quantity_deduct, getApplicationContext(), data, new VolleyCallback() {
//                                        @Override
//                                        public void onSuccess(JSONObject response) {
//                                            try {
//                                                err = response.getBoolean("error");
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            if (!err) {
//                                                getCart();
//                                            } else {
//                                                new AlertDialog.Builder(ServicesSpaCarLists.this)
//                                                        .setTitle("Alert")
//                                                        .setMessage("Minimum of 1 item.")
//                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int i) {
//                                                                dialog.dismiss();
//                                                                getCart();
//                                                                //start code hide status bar
//                                                                View decorView = getWindow().getDecorView();
//                                                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//                                                                decorView.setSystemUiVisibility(uiOptions);
//                                                                //end code hide status bar
//
//                                                            }
//                                                        }).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onError(VolleyError error) {
//
//                                        }
//                                    });
//                                } else if (viewID == R.id.addButton) {
//                                    JSONObject data = cartAdapter.getItemJSONObject();
//                                    try {
//                                        data.put("service_id", serviceID);
//                                        data.put("guest_id", vdata.getGuestID());
//                                        data.put("hotel_id", vdata.getHotelID());
//                                    }catch (JSONException e){
//                                        e.printStackTrace();
//                                    }
//                                    cartSelButton = 1;
//                                    cartPosition = i;
//                                    url_quantity_add = vdata.getApiUrl() + "addservicescart.php";
//
//                                    nc.postdataObject(url_quantity_add, getApplicationContext(), data, new VolleyCallback() {
//                                        @Override
//                                        public void onSuccess(JSONObject response) {
//
//                                            getCart();
//                                        }
//
//                                        @Override
//                                        public void onError(VolleyError error) {
//
//                                        }
//                                    });
//                                } else




                                new AlertDialog.Builder(ServicesSpaCarLists.this)
                                        .setTitle("Delete Service Ordered")
                                        .setMessage("Press Ok to Delete Service Ordered")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {

                                                if (viewID == R.id.deleteButton) {
                                                    JSONObject data = cartAdapter.getItemJSONObject();
                                                    try {
                                                        data.put("service_id", serviceID);
                                                        data.put("guest_id", vdata.getGuestID());
                                                        data.put("hotel_id", vdata.getHotelID());
                                                    }catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                    url_delete_product = vdata.getApiUrl() + "deleteservicecart.php";
                                                    cartSelButton = 0;
                                                    cartPosition = 0;
                                                    nc.postdataObject(url_delete_product, getApplicationContext(), data, new VolleyCallback() {
                                                        @Override
                                                        public void onSuccess(JSONObject response) {
                                                            getCart();
                                                            setCartNumber();
                                                            Log.d("Delete Product", "Successful");
                                                        }

                                                        @Override
                                                        public void onError(VolleyError error) {

                                                        }
                                                    });
                                                }

                                            }
                                        }).show();









                            }
                        });

                        CancelOrder2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                if (b) {
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.crimsonred));

                                } else {
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.crimsonblack));

                                }
                            }
                        });
                        CancelOrder2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                ServicesSpaCarLists.super.onBackPressed();
                            }
                        });

                        //// BUANG KA SA INYONG BALAY!!!
                        ConfirmOrder2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                if(b){
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.crimsonred));
                                    view.setOnKeyListener(new View.OnKeyListener() {
                                        @Override
                                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                            if(i == KeyEvent.KEYCODE_DPAD_UP && KeyEvent.ACTION_DOWN == keyEvent.getAction()){
                                                cartSelButton = 0;
                                                if(CarSpaCartList.getCount() == 0){
                                                    CartButton.requestFocus();
                                                }else {
                                                    cartPosition = CarSpaCartList.getAdapter().getCount() - 1;
                                                }
                                                getCart();
                                            }
                                            return false;
                                        }
                                    });

                                }else{
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(ServicesSpaCarLists.this, R.color.crimsonblack));
                                }
                            }
                        });
                        ConfirmOrder2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                new AlertDialog.Builder(ServicesSpaCarLists.this)
                                        .setTitle("Confirm Service Order")
                                        .setMessage("Press Ok to confirm service ordered")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                dialog2.dismiss();



                                        JSONObject postData = new JSONObject();
                                        String[] tol = GrandTotalText.getText().toString().split(" ");
                                        int total = (int)Double.parseDouble(tol[1]);
                                        try {
                                            postData.put("guest_id", vdata.getGuestID());
                                            postData.put("service_id", serviceID);
                                            postData.put("grand_total", total);
                                            postData.put("hotel_id", vdata.getHotelID());
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                        url_confirm = vdata.getApiUrl() + "confirmservices.php";
                                        nc.postdataObject(url_confirm, getApplicationContext(),postData, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject response) {
//                                                Toast.makeText(getApplicationContext(), "Thank you! Your order has been placed.", Toast.LENGTH_LONG).show();

                                                cartSelButton = 0;
                                                cartPosition = 0;
                                                cartBTN = 1;

                                                setCartNumber();
                                                getCart();
                                                ServicesSpaCarLists.super.onBackPressed();

                                                Log.d("onSuccess", "log");
                                            }

                                            @Override
                                            public void onError(VolleyError error) {
                                            }
                                        });

                                      }
                                     }).show();

                            }

                        });
                        //// BUANG KA SA INYONG BALAY!!!

                        dialog2.setCancelable(false);
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.setOnKeyListener(new Dialog.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                Log.d("Key Pressed", Integer.toString(keyCode));
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                                    dialog2.dismiss();
//                                    //start code hide status bar
//                                    View decorView = getWindow().getDecorView();
//                                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//                                    decorView.setSystemUiVisibility(uiOptions);
//                                    //end code hide status bar
                                }
                                return false;
                            }
                        });

                        dialog2.show();
                        //// BUANG KA SA INYONG BALAY!!!

                    }
                }catch (JSONException e){
                    CarSpaCartList.setAdapter(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

        ArrayList<JSONObject> aList=new ArrayList<JSONObject>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        }catch (JSONException je){je.printStackTrace();}
        return  aList;
    }

    private void deductQ(){
        quantity--;
        quantity = quantity <= 1 ? 1:quantity;
        quantityPrice = (quantity * pprice);

    }

    private void addQ(){
        quantity++;
        quantity = quantity <= 1 ? 1:quantity;
        quantityPrice = (quantity * pprice);
    }

    private void addCart(){

        url_add_to_cart = vdata.getApiUrl() + "addservicescart.php";
        JSONObject postData = new JSONObject();
        try {
            postData.put("menu_id", prodID);
            postData.put("guest_id", vdata.getGuestID());
            postData.put("hotel_id", vdata.getHotelID());
            postData.put("service_id", serviceID);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("JSON DATA",postData.toString());
        nc.postdataObject(url_add_to_cart, getApplicationContext(), postData, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
//                Toast.makeText(getApplicationContext(),"Item has been added to your cart.",Toast.LENGTH_SHORT).show();
                setCartNumber();
            }

            @Override
            public void onError(VolleyError error) {
//                Toast.makeText(getApplicationContext(),"Item has not been added to your cart.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCartNumber(){
        url = vdata.getApiUrl() + "servicescart.php?guest_id=" + vdata.getGuestID() + "&service_id=" + serviceID;
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    JSONObject results = response;
                    JSONArray products = results.getJSONArray("cart");
                    QuantityText.setText(String.valueOf(products.length()) + " item(s) in Cart");
                    QuantityText.setVisibility(View.VISIBLE);
                }catch (JSONException e){
                    QuantityText.setText("0 item(s) in Cart");
                    QuantityText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void setNotif(){
        //if(chatNotiff.getVisibility() == View.VISIBLE) {
        thread_url = vdata.getApiUrl() + "chats.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(6000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                jsonRequest = new JsonArrayRequest(Request.Method.GET, thread_url, null,
                                        new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                try {
                                                    array = response;
                                                    if (array.length() > 0) {
                                                        msgs = new String[array.length()];
                                                        frm = new String[array.length()];
                                                        resCount = 0;
                                                        anotherCount = 0;
                                                        if (array != null) {
                                                            for (int i = 0; i < array.length(); i++) {
                                                                chatobj = array.getJSONObject(i);
                                                                if (chatobj.getString("status").equals("unseen") && chatobj.getString("msg_from").equals("admin")) {
                                                                    resCount++;
                                                                }
                                                            }
                                                        }
                                                        Log.d("resCount :", Integer.toString(resCount));
                                                        if (lastCount < resCount) {
                                                            if (notif_number.getVisibility() == View.INVISIBLE) {
                                                                notif_number.setVisibility(View.VISIBLE);
                                                            }
                                                            notif_number.setText(Integer.toString(lastCount + (resCount - lastCount)));
                                                            lastCount = resCount;
                                                            anotherCount = Integer.parseInt(notif_number.getText().toString());
                                                            editor.putString(CHATCOUNT, Integer.toString(anotherCount));
                                                            editor.putString(LASTCOUNT, Integer.toString(lastCount));
                                                            editor.commit();
                                                            try {
                                                                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                                r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                                r.play();
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    Log.d("JSONException", "Unable to parse JSONArray");
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("Error.Response", error.getLocalizedMessage());
                                            }
                                        }
                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        String creds = String.format("%s:%s", "api", "qwerty!@#123");
                                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                        params.put("Authorization", auth);
                                        return params;
                                    }
                                };
                                queue.add(jsonRequest);

                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        //}
    }


    public void ToastHomeButton() {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_container,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView ToastText = (TextView) layout.findViewById(R.id.toastText);
        ToastText.setText("  Home button disabled!  ");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }


}
