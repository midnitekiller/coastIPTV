package com.direct2guests.d2g_tv.NonActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.direct2guests.d2g_tv.Activities.ChannelListActivity;
import com.direct2guests.d2g_tv.Activities.HotelServicesActivity;
import com.direct2guests.d2g_tv.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Typeface;


import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ImgPath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;

public class ChannelListAdapter extends ArrayAdapter<JSONObject> {

    int row_layout;
    ArrayList<JSONObject> list;
    Context context;
    String image,imgUrl,serveruri;
    int pos;

    Typeface fontPathReg;
    Typeface fontPathBold;



    public ChannelListAdapter(Context context, int row_layout, ArrayList<JSONObject> list, String serveruri) {
        super(context,row_layout, list);

        this.context=context;
        this.row_layout=row_layout;
        this.list=list;
        this.serveruri = serveruri;


        fontPathReg = Typeface.createFromAsset(context.getAssets(), "fonts/brandonreg.ttf");
        fontPathBold = Typeface.createFromAsset(context.getAssets(), "fonts/brandonbold.ttf");



    }

    public void setPosition(int pos){
        this.pos = pos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(row_layout, parent, false);

        ImageView channel_logo = itemView.findViewById(R.id.channelLogo);
        TextView channel_name = itemView.findViewById(R.id.channelTitle);
        TextView channel_num = itemView.findViewById(R.id.channelNum);
        RelativeLayout channel_layout = itemView.findViewById(R.id.channelLayout);
        channel_name.setTypeface(fontPathBold);
        channel_num.setTypeface(fontPathBold);
        channel_name.setAllCaps(true);
        channel_name.setTextColor(Color.parseColor("#000000"));
        channel_num.setTextColor(Color.parseColor("#000000"));






//        if(pos == position) {
//
//
//            channel_name.setTextSize(1, 16 );
//            channel_name.setTextColor(Color.parseColor("#ffffff"));
//
//
//        }else{
//            channel_layout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.orange_transparent));
//            channel_name.setTextColor(Color.parseColor("#878782"));
//
//
//        }


        try{
//            if(list.get(position).has("channel_logo")){
//
//                Picasso.with(context).load(serveruri + list.get(position).getString("img_path")).resize(400,400).into(channel_logo);
//
//            }else{
//                Picasso.with(context).load(R.drawable.not_available).resize(400,400).into(channel_logo);
//            }
            String cname = list.get(position).getString("channel_name");
            int count = position + 1;
            String cnum = Integer.toString(count);
            channel_name.setText(cname);
            channel_num.setText(cnum);

        }catch(JSONException e){
            Log.d("JsonException : ","ChannelListAdapter Error");
            e.printStackTrace();
        }

        return itemView;
    }

}
