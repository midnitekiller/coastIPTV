package com.direct2guests.d2g_tv.NonActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.direct2guests.d2g_tv.R;

import java.io.Serializable;

public class Variable implements Serializable {
    public static final String EXTRA = "com.direct2guests.d2g_tv.VARIABLE_EXTRA";

    private String unique_id;
    private String hotel_id;
    private String hotel_name;
    private String guest_first_name;
    private String guest_last_name;
    private String guest_id;
    private String room_id;
    private String qlcheckinpackage_id;
    private String qlcheckinroom_status;


    private String billwithouttax;
    private String billwithtax;

    private String room_number;
    private String check_in;
    private String check_out;
    private String guest_type;
    private String hotel_logo;
    private String hotel_background;
    private String[] access;
    private String chat;
    private String currency;
    private int unreadChat;
    private String weatherid;
    private String airportid;
    private String apiurl;
    private String serverurl;
    private String localipapi;
    private String localipserver;



    private String qlroom_id;
    private String qlroom_no;
    private String qlroom_type;
    private String qlroom_price;
    private String qlroom_status;

    private String chanID, chanBG, chanBGPath, chanName;


    private String[] ads_restaurant_title;
    private String[] ads_restaurant_description;
    private String[] ads_restaurant_address;
    private String[] ads_restaurant_contact;

    private String[] ads_activities_title;
    private String[] ads_activities_description;
    private String[] ads_activities_address;
    private String[] ads_activities_contact;

    private String[] ads_pubs_title;
    private String[] ads_pubs_description;
    private String[] ads_pubs_address;
    private String[] ads_pubs_contact;

    private String[] ads_restaurant_img1;
    private String[] ads_restaurant_img2;
    private String[] ads_restaurant_img3;
    private String[] ads_restaurant_img4;
    private String[] ads_restaurant_img5;

    private String[] ads_activities_img1;
    private String[] ads_activities_img2;
    private String[] ads_activities_img3;
    private String[] ads_activities_img4;
    private String[] ads_activities_img5;

    private String[] ads_pubs_img1;
    private String[] ads_pubs_img2;
    private String[] ads_pubs_img3;
    private String[] ads_pubs_img4;
    private String[] ads_pubs_img5;

    private String[] restaurants_id;
    private String[] restaurant_name;
    private String[] restaurant_open;
    private String[] restaurant_close;
    private String[] restaurant_description;
    private String[] restaurant_image;

    //setters
    public void setUniqueID(String unique_id){
        this.unique_id = unique_id;
    }
    public void setHotelID(String hotel_id){
        this.hotel_id = hotel_id;
    }
    public void setHotelName(String hotel_name){
        this.hotel_name = hotel_name;
    }
    public void setGuestFirstName(String guest_first_name){
        this.guest_first_name = guest_first_name;
    }
    public void setGuestLastName(String guest_last_name){
        this.guest_last_name = guest_last_name;
    }
    public void setGuestID(String guest_id){
        this.guest_id = guest_id;
    }


    public void setRoomID(String room_id){
        this.room_id = room_id;
    }


    public void setBillWithOutTax(String billwithouttax){
        this.billwithouttax = billwithouttax;
    }
    public void setBillWithTax(String billwithtax){
        this.billwithtax = billwithtax;
    }


    public void setRoomNumber(String room_number){
        this.room_number = room_number;
    }
    public void setCheckIn(String check_in){
        this.check_in = check_in;
    }
    public void setCheckOut(String check_out){
        this.check_out = check_out;
    }
    public void setGuestType(String guest_type){
        this.guest_type = guest_type;
    }
    public void setHotelLogo(String hotel_logo){
        this.hotel_logo = hotel_logo;
    }
    public void setHotelBackground(String hotel_background){ this.hotel_background = hotel_background; }
    public void setHotelAccess(String[] access){
        this.access = new String[access.length];
        for(int i = 0; i < access.length; i++){
            this.access[i] = access[i];
        }
    }
    public void setChatMessages(String chatJSON){
        this.chat = chatJSON;
    }
    public void setUnreadChat(int unreadChat){
        this.unreadChat = unreadChat;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }

    //setters for ads
    public void setAdsRestaurantTitle(String[] ads_restaurant_title){
        this.ads_restaurant_title = new String[ads_restaurant_title.length];
        for(int i = 0; i < ads_restaurant_title.length; i++){
            this.ads_restaurant_title[i] = ads_restaurant_title[i];
        }
    }
    public void setAdsRestaurantDescription(String[] ads_restaurant_description){
        this.ads_restaurant_description = new String[ads_restaurant_description.length];
        for(int i = 0; i < ads_restaurant_description.length; i++){
            this.ads_restaurant_description[i] = ads_restaurant_description[i];
        }
    }
    public void setAdsRestaurantAddress(String[] ads_restaurant_address){
        this.ads_restaurant_address = new String[ads_restaurant_address.length];
        for(int i = 0; i < ads_restaurant_address.length; i++){
            this.ads_restaurant_address[i] = ads_restaurant_address[i];
        }
    }
    public void setAdsRestaurantContact(String[] ads_restaurant_contact){
        this.ads_restaurant_contact = new String[ads_restaurant_contact.length];
        for(int i = 0; i < ads_restaurant_contact.length; i++){
            this.ads_restaurant_contact[i] = ads_restaurant_contact[i];
        }
    }

    public void setAdsActivitiesTitle(String[] ads_activities_title){
        this.ads_activities_title = new String[ads_activities_title.length];
        for(int i = 0; i < ads_activities_title.length; i++){
            this.ads_activities_title[i] = ads_activities_title[i];
        }
    }
    public void setAdsActivitiesDescription(String[] ads_activities_description){
        this.ads_activities_description = new String[ads_activities_description.length];
        for(int i = 0; i < ads_activities_description.length; i++){
            this.ads_activities_description[i] = ads_activities_description[i];
        }
    }
    public void setAdsActivitiesAddress(String[] ads_activities_address){
        this.ads_activities_address = new String[ads_activities_address.length];
        for(int i = 0; i < ads_activities_address.length; i++){
            this.ads_activities_address[i] = ads_activities_address[i];
        }
    }
    public void setAdsActivitiesContact(String[] ads_activities_contact){
        this.ads_activities_contact = new String[ads_activities_contact.length];
        for(int i = 0; i < ads_activities_contact.length; i++){
            this.ads_activities_contact[i] = ads_activities_contact[i];
        }
    }

    public void setAdsPubsTitle(String[] ads_pubs_title){
        this.ads_pubs_title = new String[ads_pubs_title.length];
        for(int i = 0; i < ads_pubs_title.length; i++){
            this.ads_pubs_title[i] = ads_pubs_title[i];
        }
    }
    public void setAdsPubsDescription(String[] ads_pubs_description){
        this.ads_pubs_description = new String[ads_pubs_description.length];
        for(int i = 0; i < ads_pubs_description.length; i++){
            this.ads_pubs_description[i] = ads_pubs_description[i];
        }
    }
    public void setAdsPubsAddress(String[] ads_pubs_address){
        this.ads_pubs_address = new String[ads_pubs_address.length];
        for(int i = 0; i < ads_pubs_address.length; i++){
            this.ads_pubs_address[i] = ads_pubs_address[i];
        }
    }
    public void setAdsPubsContact(String[] ads_pubs_contact){
        this.ads_pubs_contact = new String[ads_pubs_contact.length];
        for(int i = 0; i < ads_pubs_contact.length; i++){
            this.ads_pubs_contact[i] = ads_pubs_contact[i];
        }
    }

    public void setAdsRestaurantImage1(String[] ads_restaurant_img1){
        this.ads_restaurant_img1 = ads_restaurant_img1;
    }
    public void setAdsRestaurantImage2(String[] ads_restaurant_img2){
        this.ads_restaurant_img2 = ads_restaurant_img2;
    }
    public void setAdsRestaurantImage3(String[] ads_restaurant_img3){
        this.ads_restaurant_img3 = ads_restaurant_img3;
    }
    public void setAdsRestaurantImage4(String[] ads_restaurant_img4){
        this.ads_restaurant_img4 = ads_restaurant_img4;
    }
    public void setAdsRestaurantImage5(String[] ads_restaurant_img5){
        this.ads_restaurant_img5 = ads_restaurant_img5;
    }

    public void setAdsActivitiesImage1(String[] ads_activities_img1){
        this.ads_activities_img1 = ads_activities_img1;
    }
    public void setAdsActivitiesImage2(String[] ads_activities_img2){
        this.ads_activities_img2 = ads_activities_img2;
    }
    public void setAdsActivitiesImage3(String[] ads_activities_img3){
        this.ads_activities_img3 = ads_activities_img3;
    }
    public void setAdsActivitiesImage4(String[] ads_activities_img4){
        this.ads_activities_img4 = ads_activities_img4;
    }
    public void setAdsActivitiesImage5(String[] ads_activities_img5){
        this.ads_activities_img5 = ads_activities_img5;
    }

    public void setAdsPubsImage1(String[] ads_pubs_img1){
        this.ads_pubs_img1 = ads_pubs_img1;
    }
    public void setAdsPubsImage2(String[] ads_pubs_img2){
        this.ads_pubs_img2 = ads_pubs_img2;
    }
    public void setAdsPubsImage3(String[] ads_pubs_img3){
        this.ads_pubs_img3 = ads_pubs_img3;
    }
    public void setAdsPubsImage4(String[] ads_pubs_img4){
        this.ads_pubs_img4 = ads_pubs_img4;
    }
    public void setAdsPubsImage5(String[] ads_pubs_img5){
        this.ads_pubs_img5 = ads_pubs_img5;
    }

    public void setRestaurantID(String[] restaurant_id){
        this.restaurants_id = restaurant_id;
    }
    public void setRestaurantName(String[] restaurant_name){
        this.restaurant_name = restaurant_name;
    }
    public void setRestaurantOpen(String[] restaurant_open){
        this.restaurant_open = restaurant_open;
    }
    public void setRestaurantClose(String[] restaurant_close){
        this.restaurant_close = restaurant_close;
    }
    public void setRestaurantDesc(String[] restaurant_description){
        this.restaurant_description = restaurant_description;
    }
    public void setRestaurantImage(String[] restaurant_image){
        this.restaurant_image = restaurant_image;
    }
    public void setWeatherid(String weatherid){
        this.weatherid = weatherid;
    }
    public void setAirportid(String airportid){
        this.airportid = airportid;
    }
    public void setServerURL(String serverurl){
        this.serverurl = serverurl;
    }
    public void setApiUrl(String apiurl){
        this.apiurl = apiurl;
    }

    //Begin QueensLand Parsing

    public void setQLroomID(String qlroom_id){this.qlroom_id = qlroom_id;}
    public void setQLroomNo(String qlroom_no){this.qlroom_no = qlroom_no;}
    public void setQLroomType(String qlroom_type){this.qlroom_type = qlroom_type;}
    public void setQLroomPrice(String qlroom_price){this.qlroom_price = qlroom_price;}
    public void setQLroomStatus(String qlroom_status){this.qlroom_status = qlroom_status;}

    public String setChanID(String chanID){this.chanID = chanID;
        return chanID;
    }
    public void setChanBG(String chanBG){this.chanBG = chanBG;}
    public void setChanBGPath(String chanBGPath){this.chanBGPath = chanBGPath;}
    public void setChanName(String chanName){this.chanName = chanName;}

    //End QL



    //getters


    public String getQLroomID() {return qlroom_id;}
    public String getQlroomNo() {return qlroom_no;}
    public String getQLroomType() {return qlroom_type;}
    public String getQLroomStatus() {return qlroom_status;}
    public String getQLroomPrice() {return qlroom_price;}

    public String getChanID() {return chanID;}
    public String getChanBG() {return chanBG;}
    public String getChanBGPath() {return chanBGPath;}
    public String getChanName() {return chanName;}



    public String getUniqueID(){
        return unique_id;
    }
    public String getHotelID(){
        return hotel_id;
    }
    public String getHotelName(){
        return hotel_name;
    }
    public String getGuestFirstName(){
        return guest_first_name;
    }
    public String getGuestLastName() {
        return guest_last_name;
    }
    public String getGuestID(){
        return guest_id;
    }
    public String getRoomID(){
        return room_id;
    }




    public String getBillWithOutTax(){
        return billwithouttax;
    }
    public String getBillWithTax(){
        return billwithtax;
    }

    public String getRoomNumber(){
        return room_number;
    }
    public String getCheckIn(){
        return check_in;
    }
    public String getCheckOut(){
        return check_out;
    }
    public String getGuest_type(){
        return guest_type;
    }
    public String getHotelLogo(){
        return hotel_logo;
    }
    public String getHotelBackground() { return hotel_background; }
    public String[] getHotelAccess(){
        return access;
    }
    public String getChatMessages(){
        return chat;
    }
    public int getUnreadChat() {
        return unreadChat;
    }
    public String getCurrency(){
        return currency;
    }

    //getters ads
    public String[] getAdsRestaurantTitle(){
        return ads_restaurant_title;
    }
    public String[] getAdsRestaurantDescription(){
        return ads_restaurant_description;
    }
    public String[] getAdsRestaurantAddress(){
        return ads_restaurant_address;
    }
    public String[] getAdsRestaurantContact(){
        return ads_restaurant_contact;
    }

    public String[] getAdsActivitiesTitle(){
        return ads_activities_title;
    }
    public String[] getAdsActivitiesDescription(){
        return ads_activities_description;
    }
    public String[] getAdsActivitiesAddress(){
        return ads_activities_address;
    }
    public String[] getAdsActivitiesContact(){
        return ads_activities_contact;
    }

    public String[] getAdsPubsTitle(){
        return ads_pubs_title;
    }
    public String[] getAdsPubsDescription(){
        return ads_pubs_description;
    }
    public String[] getAdsPubsAddress(){
        return ads_pubs_address;
    }
    public String[] getAdsPubsContact(){
        return ads_pubs_contact;
    }

    public String[] getAdsRestaurantImage1(){
        return ads_restaurant_img1;
    }
    public String[] getAdsRestaurantImage2(){
        return ads_restaurant_img2;
    }
    public String[] getAdsRestaurantImage3(){
        return ads_restaurant_img3;
    }
    public String[] getAdsRestaurantImage4(){
        return ads_restaurant_img4;
    }
    public String[] getAdsRestaurantImage5(){
        return ads_restaurant_img5;
    }

    public String[] getAdsActivitiesImage1(){
        return ads_activities_img1;
    }
    public String[] getAdsActivitiesImage2(){
        return ads_activities_img2;
    }
    public String[] getAdsActivitiesImage3(){
        return ads_activities_img3;
    }
    public String[] getAdsActivitiesImage4(){
        return ads_activities_img4;
    }
    public String[] getAdsActivitiesImage5(){
        return ads_activities_img5;
    }

    public String[] getAdsPubsImage1(){
        return ads_pubs_img1;
    }
    public String[] getAdsPubsImage2(){
        return ads_pubs_img2;
    }
    public String[] getAdsPubsImage3(){
        return ads_pubs_img3;
    }
    public String[] getAdsPubsImage4(){
        return ads_pubs_img4;
    }
    public String[] getAdsPubsImage5(){
        return ads_pubs_img5;
    }

    public String[] getRestaurantID(){
        return restaurants_id;
    }
    public String[] getRestaurantName(){
        return restaurant_name;
    }
    public String[] getRestaurantOpen(){
        return restaurant_open;
    }
    public String[] getRestaurantClose(){
        return restaurant_close;
    }
    public String[] getRestaurantDesc(){
        return restaurant_description;
    }
    public String[] getRestaurantImage(){
        return restaurant_image;
    }

    public String getWeatherid(){
        return weatherid;
    }

    public String getAirportid(){
        return airportid;
    }


    public String getServerURL(){
        return serverurl;
    }

    public String getApiUrl(){
        return apiurl;
    }


    //methods
    public void focusAnim(View view, boolean hasFocus, Context mContext, Activity activity){
        final Animation onFocusAnim = AnimationUtils.loadAnimation(mContext, R.anim.onfocusanim);
        final Animation onBlurAnim = AnimationUtils.loadAnimation(mContext, R.anim.onbluranim);
        onFocusAnim.setDuration(50);
        onBlurAnim.setDuration(50);
        if(hasFocus){
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.hometint));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            view.startAnimation(onFocusAnim);
            view.setTranslationZ(5);
            view.setElevation(80);

        } else{
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.cleartint));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            view.startAnimation(onBlurAnim);
            view.setTranslationZ(1);
        }
    }
    public void focusNoAnim(View view, boolean hasFocus, Context mContext, Activity activity){
        if(hasFocus){
            view.setForegroundTintList(ContextCompat.getColorStateList(activity, R.color.hometint));
            view.setForegroundTintMode(PorterDuff.Mode.MULTIPLY);
        } else{
            view.setForegroundTintList(ContextCompat.getColorStateList(activity, R.color.cleartint));
            view.setForegroundTintMode(PorterDuff.Mode.MULTIPLY);
        }
    }
    public void focusPlaces(View view, boolean hasFocus, Context mContext, Activity activity){
        final Animation onFocusAnim = AnimationUtils.loadAnimation(mContext, R.anim.onfocusanim);
        final Animation onBlurAnim = AnimationUtils.loadAnimation(mContext, R.anim.onbluranim);
        onFocusAnim.setDuration(50);
        onBlurAnim.setDuration(50);
        if(hasFocus){
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.placesfocus));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            view.startAnimation(onFocusAnim);
            view.setTranslationZ(5);
        } else{
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.placesblur));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            view.startAnimation(onBlurAnim);
            view.setTranslationZ(1);
        }
    }
    public void focusIcons(View view, boolean hasFocus, Context mContext, Activity activity){
        if(hasFocus){
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.iconfocus));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        } else{
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.iconblur));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        }
    }
    public void ButtonFocus(View view, boolean hasFocus, Activity activity){
        if(hasFocus){
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.iconfocus));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);

        } else{
            view.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.iconblur));
            view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        }
    }
    public void focusText(TextView textView, boolean hasFocus, Context mContext, Activity activity){
        if(hasFocus){
            textView.setTextColor(Color.parseColor("#000000"));
        } else {
            textView.setTextColor(Color.parseColor("#ffffff"));
        }
    }
}
