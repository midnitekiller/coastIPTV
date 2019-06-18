package com.direct2guests.d2g_tv.NonActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.direct2guests.d2g_tv.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<JSONObject> {
    int row_layout;
    Variable vdata;
    ArrayList<JSONObject> list;
    Context context;
    String image,imgUrl, stype, serveruri;
    int pos;


    Typeface fontPathReg;
    Typeface fontPathBold;



    public ListAdapter(Context context,Variable vdata, int row_layout, String type, ArrayList<JSONObject> list, String serveruri){

        super(context,row_layout, list);


        fontPathReg = Typeface.createFromAsset(context.getAssets(), "fonts/brandonreg.ttf");
        fontPathBold = Typeface.createFromAsset(context.getAssets(), "fonts/brandonbold.ttf");


        this.vdata = vdata;
        this.context=context;
        this.row_layout=row_layout;
        this.list=list;
        this.serveruri=serveruri;
        if(type.equals("restaurant")){
            stype = "menus";
        }else if(type.equals("services")){
            stype = "services";
        }
    }
    public void setPosition(int pos){
        this.pos = pos;
    }

    public JSONObject getItemJSONObject(){
        JSONObject data = new JSONObject();
        try {
            image = list.get(pos).getString("img_path").toString();
            if(image.equals(null) || image.isEmpty()){
                imgUrl = "";
                Log.d("Image", " :: " + image);
            }else{
                imgUrl = serveruri + image;
            }
            data.put("imageurl", imgUrl);
        }catch(JSONException e){
            imgUrl = "";
            try {
                data.put("imageurl", imgUrl);
            }catch(JSONException f){
                Log.d("Data Put", f.getLocalizedMessage());
            }
            Log.d("JSON Image", e.getLocalizedMessage());
        }

        try{
            if(stype.equals("menus")) {
                data.put("name", list.get(pos).getString("menu_name"));
                String php ="P ";
                data.put("priceString", php + list.get(pos).getString("menu_price"));
                data.put("price", list.get(pos).getString("menu_price"));
                data.put("desc", list.get(pos).getString("menu_desc"));
                data.put("product_id", list.get(pos).getString("restomenu_ID"));
            }else if(stype.equals("services")){
                data.put("name", list.get(pos).getString("serviceProdName"));
                String php ="P ";
                data.put("priceString", php + list.get(pos).getString("serviceProdPrice"));
                data.put("price", list.get(pos).getString("serviceProdPrice"));
                data.put("desc", list.get(pos).getString("serviceProdDesc"));
                data.put("product_id", list.get(pos).getString("serviceProd_ID"));
                data.put("duration", list.get(pos).getString("duration"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(row_layout, parent, false);

        ImageView img = itemView.findViewById(R.id.thumbnail);
        TextView title = itemView.findViewById(R.id.title);
        TextView description = itemView.findViewById(R.id.description);
        TextView price = itemView.findViewById(R.id.priceTxt);
        RelativeLayout relLay = itemView.findViewById(R.id.tab);

        title.setAllCaps(true);

        title.setTypeface(fontPathBold);
        description.setTypeface(fontPathBold);
        price.setTypeface(fontPathBold);

        if(pos == position){
            relLay.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.cleartint));
//            title.setTextColor(Color.parseColor("#ffffff"));
//            description.setTextColor(Color.parseColor("#ffffff"));
            //price.setBackgroundColor(Color.parseColor("#000000"));
//            price.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.cleartint));
        }else{
//            relLay.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.adcleartint));
//            title.setTextColor(Color.parseColor("#ffffff"));
//            description.setTextColor(Color.parseColor("#ffffff"));
        }
        try{
            image = list.get(position).getString("img_path").toString();
            if(image.equals("")){
                imgUrl = "";
                Log.d("Image", " :: " + image);
            }else{
                imgUrl = serveruri + image;
            }

        }catch (JSONException e){
            imgUrl = "";
            Log.d("Exception", "image"+imgUrl);
        }

        try {
            if(imgUrl.isEmpty() && stype.equals("services")){
                Picasso.with(context).load(R.drawable.rentals).resize(200,200).into(img);
                Log.d("Car", "empty");
            }else if(imgUrl.isEmpty() && stype.equals("menus")){
                Picasso.with(context).load(R.drawable.not_available).resize(200,200).into(img);
                Log.d("Restaurant", "empty");
            }else{
                Log.d("Image", "not empty");
                Picasso.with(context).load(imgUrl).resize(200,200).into(img);

            }

            String php="P ";
            if(stype.equals("menus")){
                String desc;
                if(list.get(position).getString("menu_shortDesc").length() >= 30){
                    desc = list.get(position).getString("menu_shortDesc").substring(0,27)+"...";
                }else{
                    desc = list.get(position).getString("menu_shortDesc");
                }
                description.setText(desc);
                price.setText(php+list.get(position).getString("menu_price"));
                title.setText(list.get(position).getString("menu_name"));
            }else {
                String desc;
                if(list.get(position).getString("serviceProdDesc").length() >= 30){
                    desc = list.get(position).getString("serviceProdDesc").substring(0,27)+"...";
                }else{
                    desc = list.get(position).getString("serviceProdDesc");
                }
                description.setText(desc);
                price.setText(php+list.get(position).getString("serviceProdPrice"));
                title.setText(list.get(position).getString("serviceProdName"));
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return itemView;
    }
}
