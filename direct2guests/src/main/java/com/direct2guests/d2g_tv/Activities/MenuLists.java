package com.direct2guests.d2g_tv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class MenuLists extends LangSelectActivity {




    private Variable vdata;
    private NetworkConnection nc = new NetworkConnection();
    public final static String PREFERENCE = "MY_PREFERENCE";
    public final static String CHATCOUNT = "CHAT_COUNT";
    public final static String LASTCOUNT = "LAST_COUNT";


    private TextView restaurantTitle, ConfirmButton, ConfirmButton2, GrandTotal, CartNumber, cartNameList, cartPriceList, CartCheckOutTitle, CancelOrder2;
    private ImageView restaurantImage, menuListBG;
    private RelativeLayout CartLayout;
    private ListView CartList, MenuList;
    private Button CartBackButton, HomeButton, BackButton;

    private String restoID, restoName, restoDesc, restoImage, url_restaurants, servicesType, url, rescount;
    private JSONObject dataRes;
    private String image, name, priceString, price, desc, prodID, SCrate, url_add_to_cart, url_quantity_deduct, url_quantity_add, url_delete_product, menu_id, url_confirm, err1;
    private int focusCounter, quantity, quantityPrice,pprice, cartBTN, cartPosition, cartSelButton;
    boolean err;
    private TextView ProductName, ProductDescription, ProductQuantity, ProductQuantityPrice, typeText;
    private ImageView ProductImage;
    private RelativeLayout QuantityLayout;
    private Button DeductButton, AddButton, AddCartButton, CancelButton;

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
    private BroadcastReceiver broadcast_reciever;
    private RelativeLayout chatNotiff;
    private String imagebg_url;

    private TextView GuestNameTxt;
    private FirebaseAnalytics mFirebaseAnalytics;



    // Font path

    String fontPathBold = "fonts/brandonbold.ttf";
    String fontPathReg = "fonts/brandonreg.ttf";

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);


        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        setContentView(R.layout.activity_fnb_list);


        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "MenuListsActivity");
        params.putString("app_event", "Viewing Food and Drinks Category");
        mFirebaseAnalytics.logEvent("app_activity", params);




        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        restoID = getIntent().getStringExtra(FoodDrinksActivity.RESTAURANT_ID);
        restoName = getIntent().getStringExtra(FoodDrinksActivity.RESTAURANT_NAME);
        restoDesc = getIntent().getStringExtra(FoodDrinksActivity.RESTAURANT_DESC);
        restoImage = getIntent().getStringExtra(FoodDrinksActivity.RESTAURANT_IMAGE);
        rescount = getIntent().getStringExtra(FoodDrinksActivity.RESTAURANT_COUNT);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();

        GuestNameTxt = findViewById(R.id.guestNameTxtV5);







        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);

        // Loading Font Face

        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

        restaurantTitle = findViewById(R.id.SCTitle);
        typeText = findViewById(R.id.typeText);
        restaurantImage = findViewById(R.id.SCImage);
        menuListBG = findViewById(R.id.menuListBG);



//        CartLayout = findViewById(R.id.SCCartLayout);
//        CartList = findViewById(R.id.CarSpaCartLists);
//        ConfirmButton = findViewById(R.id.confirmOrderTxt);
//        GrandTotal = findViewById(R.id.grandTotal);



        MenuList = findViewById(R.id.SpaCarListview);
        CartNumber = findViewById(R.id.quatityIconTxt);
        CartBackButton = findViewById(R.id.CartButtonSC);
        HomeButton = findViewById(R.id.HomeBtnSC);
        BackButton = findViewById(R.id.BackButtonSC);

        chatNotiff = findViewById(R.id.notif);
        restaurantTitle.setText(restoName);
        restaurantTitle.setTypeface(fontBold);

        typeText.setText("Food and Drinks");

        //Applying font
//        restaurantTitle.setTypeface(fontReg);
//        ConfirmButton.setTypeface(fontBold);
//        GrandTotal.setTypeface(fontBold);
//        typeText.setTypeface(fontBold);

        servicesType = "restaurant";

        typeText.setTypeface(fontBold);
        if(restoImage.equals("")){
            Picasso.with(getApplicationContext()).load(R.drawable.menus).resize(240,240).into(restaurantImage);
        }else {
            String image_url = vdata.getServerURL() + restoImage;
            Picasso.with(getApplicationContext()).load(image_url).resize(240,240).into(restaurantImage);
        }


        cartBTN = 1; cartPosition = 0; cartSelButton = 0;

        CartBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartBTN == 1){
                    cartBTN = 2;
//                    MenuList.setVisibility(View.GONE);
//                    CartLayout.setVisibility(View.VISIBLE);

                    cartPosition = 0;
                    cartSelButton = 0;
                    loadRestaurantCart();
//                    view.setBackgroundResource(R.color.transparent);
//                    CartNumber.setVisibility(View.INVISIBLE);
                }else if(cartBTN == 2){
                    cartBTN = 1;
//                    MenuList.setVisibility(View.VISIBLE);
//                    CartLayout.setVisibility(View.INVISIBLE);
//                    view.setBackgroundResource(R.drawable.cart_logo);
//                    CartNumber.setVisibility(View.VISIBLE);
                    setCartNumber();
                }
            }
        });





//        ConfirmButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//                    view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.questyellow));
//
//                    view.setOnKeyListener(new View.OnKeyListener() {
//                        @Override
//                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                            if(i == KeyEvent.KEYCODE_DPAD_UP && KeyEvent.ACTION_DOWN == keyEvent.getAction()){
//                                cartSelButton = 0;
//                                if(CartList.getCount() == 0){
//                                    CartBackButton.requestFocus();
//                                    MenuList.clearFocus();
//                                }else{
//                                    cartPosition = CartList.getAdapter().getCount()-1;
//                                }
//
//                                loadRestaurantCart();
//                            }
//                            return false;
//                        }
//                    });
//                }else{
//                    view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.cartbuttonfocus));
//
//                }
//            }
//        });










//        ConfirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JSONObject postData = new JSONObject();
//                String[] tol = GrandTotal.getText().toString().split(" ");
//                int total = (int)Double.parseDouble(tol[1]);
//                try {
//                    postData.put("guest_id", vdata.getGuestID());
//                    postData.put("restaurant_id", restoID);
//                    postData.put("grand_total", total);
//                    postData.put("hotel_id", vdata.getHotelID());
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//                if(servicesType.equals("restaurant")){
//                    url_confirm = vdata.getApiUrl() + "confirmmenu.php";
//                }
//
//
//                Intent i = new Intent(MenuLists.this, FoodDrinksActivity.class);
//                i.putExtra(Variable.EXTRA, vdata);
//                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
//                startActivity(i);
//
//
//
//                nc.postdataObject(url_confirm, getApplicationContext(), postData, new VolleyCallback() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//
//
//
//                        View view;
//                        Toast toast = Toast.makeText(getApplicationContext(), "    Thank you! Your order has been placed.    ", Toast.LENGTH_SHORT);
//                        view = toast.getView();
//                        view.setBackgroundColor(getResources().getColor(R.color.questpurple));
//                        toast.show();
//
//
//
//                        cartSelButton = 0;
//                        cartPosition = 0;
//                        cartBTN = 1;
////                        MenuList.setVisibility(View.VISIBLE);
////                        CartLayout.setVisibility(View.INVISIBLE);
////                        CartBackButton.setBackgroundResource(R.drawable.cart_logo);
////                        CartNumber.setVisibility(View.VISIBLE);
//                        setCartNumber();
//                        loadRestaurantMenu();
//
//                        Log.d("onSuccess", "log");
//
//
//
//
//
//
//                    }
//
//                    @Override
//                    public void onError(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                });
//            }
//        });
        setFocusClick();


    }
    @Override
    protected void onStart() {
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
//                if (action.equals("finish_menulists")) {
//                    //finishing the activity
//                    finish();
//                }
//            }
//        };
//        registerReceiver(broadcast_reciever, new IntentFilter("finish_menulists"));


        loadRestaurantMenu();
        MenuList.requestFocus();
        setCartNumber();
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
                Intent i = new Intent(MenuLists.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),MenuLists.this);
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



    @Override
    public void onBackPressed(){
//        Toast.makeText(getApplicationContext(),"If you have placed and Order, confirm at the cart Icon above", Toast.LENGTH_LONG).show();


//        View view;
//        Toast toast = Toast.makeText(getApplicationContext(), "  If you have placed and Order, confirm at the cart Icon..  ", Toast.LENGTH_SHORT);
//        view = toast.getView();
//        view.setBackgroundColor(getResources().getColor(R.color.questpurple));
//        toast.show();

        if(cartBTN == 2){
            cartBTN = 1;
//            MenuList.setVisibility(View.VISIBLE);
//            CartLayout.setVisibility(View.GONE);
//            CartBackButton.setBackgroundResource(R.drawable.cart_logo);
//            CartNumber.setVisibility(View.VISIBLE);
            setCartNumber();
        }else{
            for (int i = 0; i < vdata.getHotelAccess().length; i++) {
                if (vdata.getHotelAccess()[i].equals("chat_acc")) {

                    t.interrupt();
                }
            }
            super.onBackPressed();
        }
    }


    private void loadRestaurantMenu(){

        Locale locale;
        locale = getCurrentLanguage();
        String Lang = String.valueOf(locale);


        if(String.valueOf(Lang).equals("en")){
            url_restaurants = vdata.getApiUrl() + "menu.php?restaurant_id=" + restoID + "&hotel_id=" + vdata.getHotelID();
//            Toast.makeText(getApplicationContext(), String.valueOf(url_restaurants), Toast.LENGTH_LONG).show();
        } else {
            url_restaurants = vdata.getApiUrl() + "translations/menu_translation_" + restoID + "_" + vdata.getHotelID() + "_" + Lang + ".json";
//            Toast.makeText(getApplicationContext(),"Item has been added to your cart.", Toast.LENGTH_LONG).show();

        }


        Log.d("URL", url_restaurants);
        nc.getdataArray(url_restaurants, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(JSONArray response) {
                    if(response.equals(null)){
                        MenuList.setAdapter(null);
                    }else {
                        final JSONArray results = response;
                        String serverss_url = vdata.getServerURL().toString();
                        ArrayList<JSONObject> listItems = getArrayListFromJSONArray(results);

                        final ListAdapter adapter = new ListAdapter(getApplicationContext(),vdata, R.layout.list_row_car_spa, servicesType, listItems, serverss_url);
                        MenuList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                        MenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                adapter.setPosition(i);
                                adapter.notifyDataSetChanged();
                                try {

                                    dataRes = adapter.getItemJSONObject();
                                    image = dataRes.getString("imageurl");
                                    name = dataRes.getString("name");
                                    priceString = dataRes.getString("priceString");
                                    price = dataRes.getString("price");
                                    desc = dataRes.getString("desc");
                                    prodID = dataRes.getString("product_id");
                                    focusCounter = 1;
                                    quantity = 1;
                                    Log.d("JSONObject", image + dataRes.toString());
                                    quantityPrice = (int)Double.parseDouble(price);
                                    pprice = (int)Double.parseDouble(price);






                                    final Dialog dialog = new Dialog(MenuLists.this);
                                    dialog.setContentView(R.layout.add_cart_product_dialog);
                                    //start code hide status bar
                                    View decorView = getWindow().getDecorView();
                                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                    decorView.setSystemUiVisibility(uiOptions);
                                    //end code hide status bar

                                    // Loading Font Face
                                    Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
                                    Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

                                    ProductName = dialog.findViewById(R.id.prodName);
                                    ProductDescription = dialog.findViewById(R.id.prodDescription);
                                    ProductQuantity = dialog.findViewById(R.id.prodQuantity);
                                    ProductQuantityPrice = dialog.findViewById(R.id.quantityPrice);
                                    DeductButton = dialog.findViewById(R.id.deductQuantity);
                                    AddButton = dialog.findViewById(R.id.addQuantity);
                                    AddCartButton = dialog.findViewById(R.id.addCartButton);
                                    CancelButton = dialog.findViewById(R.id.cancelCart);
                                    ProductImage = dialog.findViewById(R.id.prodImage);
                                    QuantityLayout = dialog.findViewById(R.id.quanta);

                                    //Applying font
                                    ProductName.setTypeface(fontBold);
                                    ProductDescription.setTypeface(fontReg);
                                    ProductQuantity.setTypeface(fontReg);
                                    ProductQuantityPrice.setTypeface(fontReg);
                                    AddCartButton.setTypeface(fontBold);
                                    QuantityLayout.setVisibility(View.VISIBLE);

                                    if (image.isEmpty()) {
                                        Picasso.with(getApplicationContext()).load(R.drawable.not_available).resize(500, 300).into(ProductImage);
                                    } else {
                                        Log.d("ImageURL", image);
                                        Picasso.with(getApplicationContext()).load(image).resize(500, 300).into(ProductImage);
                                    }
                                    ProductName.setText(name);
                                    ProductDescription.setText(desc);
                                    if (servicesType.equals("spa")) {
                                        SCrate = "/Hour";

                                    } else if (servicesType.equals("car")) {
                                        SCrate = "/Day";
                                    }else if(servicesType.equals("restaurant")){
                                        SCrate = "";
                                    }
                                    ProductQuantityPrice.setText(priceString + SCrate);

                                    AddButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.deductcart));
                                               // AddButton.setAnimation(getResources().getAnimation(R.anim.onfocusanim));



                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    AddButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            addQ();
                                            ProductQuantityPrice.setText("P " + quantityPrice + ".00" + SCrate);
                                            ProductQuantity.setText(Integer.toString(quantity));
                                        }
                                    });
                                    DeductButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    DeductButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            deductQ();
                                            ProductQuantityPrice.setText("P " + quantityPrice + ".00" + SCrate);
                                            ProductQuantity.setText(Integer.toString(quantity));
                                        }
                                    });
                                    AddCartButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.addcart));

                                            }
                                        }
                                    });
                                    CancelButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View view, boolean b) {
                                            if (b) {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.deductcart));

                                            } else {
                                                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.addcart));

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
                                    dialog.show();
                                    AddCartButton.requestFocus();
                                }catch (JSONException e){
                                    Log.d("Dialog", "Error");
                                    e.printStackTrace();
                                }
                            }
                        });
                        MenuList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                adapter.setPosition(i);
                                adapter.notifyDataSetChanged();

                                try {
                                    imagebg_url = listItems.get(i).getString("img_path");
                                    String imageLink = vdata.getServerURL() + imagebg_url;
                                    Picasso.with(getApplicationContext()).load(imageLink).fit().into(menuListBG);

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

    private void loadRestaurantCart(){
        if(servicesType.equals("restaurant")){
            url = vdata.getApiUrl() + "carts.php?guest_id=" + vdata.getGuestID() + "&restaurant_id=" + restoID;
        }
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.equals(null)){
                        CartList.setAdapter(null);
                        Log.d("null", "results");
                    }else {


                        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
                        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);


                        ///// BUANG KA SA INYONG BALAY!!!!
                        final Dialog dialog2= new Dialog(MenuLists.this);
                        dialog2.setContentView(R.layout.cart_confirm_dialog);
                        //start code hide status bar
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                        //end code hide status bar

                        CartLayout = dialog2.findViewById(R.id.confCartLayout);
                        CartList = dialog2.findViewById(R.id.confCarSpaCartLists);
                        ConfirmButton2 = dialog2.findViewById(R.id.confConfirmOrderTxt);
                        CancelOrder2 = dialog2.findViewById(R.id.confCancelOrderTxt);
                        CartCheckOutTitle = dialog2.findViewById(R.id.confCartTxt);
                        GrandTotal = dialog2.findViewById(R.id.confGrandTotal);

                        CancelOrder2.setTypeface(fontBold);
                        CartCheckOutTitle.setTypeface(fontBold);
                        ConfirmButton2.setTypeface(fontBold);
                        GrandTotal.setTypeface(fontBold);



                        JSONObject results = response;
                        final JSONArray products = results.getJSONArray("cart");
                        GrandTotal.setText("P " + results.getString("cart_grand_total") + ".00" + " / TOTAL");
                        ArrayList<JSONObject> listItems = getArrayListFromJSONArray(products);
                        final CarSpaCartListAdapter cartAdapter = new CarSpaCartListAdapter(getApplicationContext(),vdata, R.layout.list_row_cart_car_spa, servicesType, listItems);
                        cartAdapter.setButton(cartSelButton);
                        cartAdapter.setPosition(cartPosition);
                        CartList.setAdapter(cartAdapter);
                        cartAdapter.notifyDataSetChanged();
                        CartList.setSelection(cartPosition);
                        CartList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        CartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                                final int viewID = view.getId();
                                if (viewID == R.id.deductButton) {
                                    JSONObject data = cartAdapter.getItemJSONObject();
                                    try {
                                        data.put("restaurant_id", restoID);
                                        data.put("guest_id", vdata.getGuestID());
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    cartSelButton = 0;
                                    cartPosition = i;
                                    if(servicesType.equals("restaurant")){
                                        url_quantity_deduct = vdata.getApiUrl() + "deductfoodcart.php";
                                    }
                                    nc.postdataObject(url_quantity_deduct, getApplicationContext(), data, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            try {
                                                err1 = response.getString("success");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (err1.equals("true")) {
                                                loadRestaurantCart();
                                            } else {
                                                new AlertDialog.Builder(MenuLists.this)
                                                        .setTitle("Alert")
                                                        .setMessage("Minimum of 1 item.")
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int i) {
                                                                dialog.dismiss();
                                                                loadRestaurantCart();
                                                                //start code hide status bar
                                                                View decorView = getWindow().getDecorView();
                                                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                                                decorView.setSystemUiVisibility(uiOptions);
                                                                //end code hide status bar

                                                            }
                                                        }).show();
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {

                                        }
                                    });
                                } else if (viewID == R.id.addButton) {
                                    JSONObject data = cartAdapter.getItemJSONObject();
                                    try {
                                        data.put("restaurant_id", restoID);
                                        data.put("guest_id", vdata.getGuestID());
                                        data.put("hotel_id", vdata.getHotelID());
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    cartSelButton = 1;
                                    cartPosition = i;
                                    if(servicesType.equals("restaurant")){
                                        url_quantity_add = vdata.getApiUrl() + "addfoodcart.php";
                                    }
                                    nc.postdataObject(url_quantity_add, getApplicationContext(), data, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            loadRestaurantCart();
                                        }

                                        @Override
                                        public void onError(VolleyError error) {

                                        }
                                    });
                                } else if (viewID == R.id.deleteButton) {
                                    JSONObject data = cartAdapter.getItemJSONObject();
                                    try {
                                        data.put("restaurant_id", restoID);
                                        data.put("guest_id", vdata.getGuestID());
                                        data.put("hotel_id", vdata.getHotelID());
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    if (servicesType.equals("restaurant")){
                                        url_delete_product = vdata.getApiUrl() + "deletefoodcart.php";
                                    }
                                    cartSelButton = 0;
                                    cartPosition = 0;
                                    nc.postdataObject(url_delete_product, getApplicationContext(),data, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            loadRestaurantCart();
                                            setCartNumber();
                                            Log.d("Delete Product", "Successful");
                                        }

                                        @Override
                                        public void onError(VolleyError error) {

                                        }
                                    });
                                }

                            }
                        });

                        CancelOrder2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                if (b) {
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.crimsonred));

                                } else {
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.crimsonblack));

                                }
                            }
                        });
                        CancelOrder2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                MenuLists.super.onBackPressed();
                            }
                        });


                        //// BUANG KA SA INYONG BALAY!!!
                        ConfirmButton2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                if(b){
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.crimsonred));

                                    view.setOnKeyListener(new View.OnKeyListener() {
                                        @Override
                                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                            if(i == KeyEvent.KEYCODE_DPAD_UP && KeyEvent.ACTION_DOWN == keyEvent.getAction()){
                                                cartSelButton = 0;
                                                if(CartList.getCount() == 0){
                                                    CartBackButton.requestFocus();

                                                }else{
                                                    cartPosition = CartList.getAdapter().getCount()-1;
                                                }

                                                loadRestaurantCart();
                                            }
                                            return false;
                                        }
                                    });
                                }else{
                                    view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.crimsonblack));
                                }
                            }
                        });
                        ConfirmButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                new AlertDialog.Builder(MenuLists.this)
                                        .setTitle("Cart Order")
                                        .setMessage("Press Ok to confirm order")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                dialog2.dismiss();

                                                JSONObject postData = new JSONObject();
                                                String[] tol = GrandTotal.getText().toString().split(" ");
                                                int total = (int)Double.parseDouble(tol[1]);
                                                try {
                                                    postData.put("guest_id", vdata.getGuestID());
                                                    postData.put("restaurant_id", restoID);
                                                    postData.put("grand_total", total);
                                                    postData.put("hotel_id", vdata.getHotelID());
                                                }catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                                if(servicesType.equals("restaurant")){
                                                    url_confirm = vdata.getApiUrl() + "confirmmenu.php";
                                                }

                                                nc.postdataObject(url_confirm, getApplicationContext(), postData, new VolleyCallback() {
                                                    @Override
                                                    public void onSuccess(JSONObject response) {

                                                        cartSelButton = 0;
                                                        cartPosition = 0;
                                                        cartBTN = 1;

                                                        setCartNumber();
                                                        loadRestaurantMenu();
                                                        MenuLists.super.onBackPressed();

                                                        Log.d("onSuccess", "log");

                                                    }

                                                    @Override
                                                    public void onError(VolleyError error) {
                                                        error.printStackTrace();
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
                    CartList.setAdapter(null);
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

        if(servicesType.equals("restaurant")){
            url_add_to_cart = vdata.getApiUrl() + "addfoodcart.php";
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("menu_id", prodID);
            postData.put("guest_id", vdata.getGuestID());
            postData.put("hotel_id", vdata.getHotelID());
            postData.put("restaurant_id", restoID);
            postData.put("qty", quantity);
            Log.d("JSON OBJECT :", postData.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("JSON DATA",postData.toString());
        nc.postdataObject(url_add_to_cart, getApplicationContext(), postData, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
//                Toast.makeText(getApplicationContext(),"Item has been added to your cart.",Toast.LENGTH_SHORT).show();

//                View view;
//                Toast toast = Toast.makeText(getApplicationContext(), "    Item has been added to your cart.    ", Toast.LENGTH_SHORT);
//                view = toast.getView();
//                view.setBackgroundColor(getResources().getColor(R.color.questpurple));
//                toast.show();
//
//
//


                setCartNumber();
            }

            @Override
            public void onError(VolleyError error) {
//                Toast.makeText(getApplicationContext(),"Item has not been added to your cart.",Toast.LENGTH_SHORT).show();
//                View view;
//                Toast toast = Toast.makeText(getApplicationContext(), "    Item has been added to your cart.    ", Toast.LENGTH_SHORT);
//                view = toast.getView();
//                view.setBackgroundColor(getResources().getColor(R.color.questpurple));
//                toast.show();

            }
        });
    }
    private void setCartNumber(){


        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

        if(servicesType.equals("restaurant")){
            url = vdata.getApiUrl() + "carts.php?guest_id=" + vdata.getGuestID() + "&restaurant_id=" + restoID;
        }
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    JSONObject results = response;
                    JSONArray products = results.getJSONArray("cart");
                    CartNumber.setText(String.valueOf(products.length()) + " item(s) in Cart");
                    CartNumber.setTypeface(fontReg);
                    CartNumber.setVisibility(View.VISIBLE);
                }catch (JSONException e){
                    CartNumber.setTypeface(fontReg);
                    CartNumber.setText("0 item(s) in Cart");
                    CartNumber.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private void setFocusClick(){
        HomeButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.ButtonFocus(view, b, MenuLists.this);
            }
        });
        BackButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.ButtonFocus(view, b, MenuLists.this);
                BackButton.setVisibility(View.GONE);
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < vdata.getHotelAccess().length; i++) {
                    if (vdata.getHotelAccess()[i].equals("chat_acc")) {

                        t.interrupt();
                    }
                }
                Intent i = new Intent(MenuLists.this, LauncherActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                startActivity(i);
                i = new Intent("finish_fooddrinks");
                sendBroadcast(i);
                i = new Intent("finish_hotelservices");
                sendBroadcast(i);
                finish();
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        CartBackButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
//                view.setBackgroundTintList(ContextCompat.getColorStateList(MenuLists.this, R.color.questyellow));
                CartNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        vdata.focusAnim(view,hasFocus,getApplicationContext(),MenuLists.this);


                    }
                });


                vdata.focusAnim(view,hasFocus,getApplicationContext(),MenuLists.this);

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
