package com.direct2guests.d2g_tv.Activities.ChatActivity;

public class ChatItem {
    public enum ChatType {
        ADMIN, GUEST
    }

    public ChatItem(String text, ChatType ctype){
        super();
        this.text = text;
        this.ctype = ctype;
    }

    public String text;
    public ChatType ctype;
}