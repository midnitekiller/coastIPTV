package com.direct2guests.d2g_tv.Activities.ChatActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.direct2guests.d2g_tv.R;
import com.github.library.bubbleview.BubbleTextView;

import java.util.ArrayList;


public class ChatCustomAdapter extends ArrayAdapter<ChatItem> {

    public ChatCustomAdapter(Context context, ArrayList<ChatItem> chats) {
        super(context, 0, chats);
    }

    // Return an integer representing the type by fetching the enum type ordinal
    @Override
    public int getItemViewType(int position) {
        return getItem(position).ctype.ordinal();
    }

    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return ChatItem.ChatType.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatItem chats = (ChatItem) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // Get the data item type for this position
            int type = getItemViewType(position);
            // Inflate XML layout based on the type
            convertView = getInflatedLayoutForType(type);
        }
        // Lookup view for data population
        BubbleTextView tvLabel = convertView.findViewById(R.id.textChat);
        if (tvLabel != null) {
            // Populate the data into the template view using the data object
            tvLabel.setText(chats.text);
        }
        // Return the completed view to render on screen
        return convertView;
    }

        // Given the item type, responsible for returning the correct inflated XML layout file
        private View getInflatedLayoutForType(int type) {
            if (type == ChatItem.ChatType.ADMIN.ordinal()) {
                return LayoutInflater.from(getContext()).inflate(R.layout.from_admin_chat_layout, null);
            } else if (type == ChatItem.ChatType.GUEST.ordinal()) {
                return LayoutInflater.from(getContext()).inflate(R.layout.from_guest_chat_layout, null);
            } else {
                return null;
            }
        }
    }

