package com.cs426.dtkhoi.GrabGirl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class ContactAdapter extends ArrayAdapter<Contact> {
    private final Context mContext;
    private ArrayList<Contact> mContacts;

    // Constructor
    ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.mContext = context;
        this.mContacts = contacts;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    // Change view ... FILL IN DETAIL LATER
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.mylistlayout, null);

            // Update contact info to a row correspond to the position or index
            Contact row_contact = mContacts.get(position);

            ImageView imageView = convertView.findViewById(R.id.avatar);
            imageView.setImageResource(row_contact.get_avatar_id());

            TextView name = convertView.findViewById(R.id.name);
            name.setText("Customer: " + row_contact.get_name());

            TextView distance = convertView.findViewById(R.id.distance);
            distance.setText("Distance: " + row_contact.get_distance() + " km");

            final Button call = convertView.findViewById(R.id.callBut);
            final Button sms = convertView.findViewById(R.id.smsBut);
//            final Button email = convertView.findViewById(R.id.emailBut);
            final Button map = convertView.findViewById(R.id.mapBut);
            final Button detail = convertView.findViewById(R.id.detailBut);
            final Button keeper = convertView.findViewById(R.id.keeperBut);

            final Button delivered = convertView.findViewById(R.id.deliveredBut);
            final Button notAtHome = convertView.findViewById(R.id.notAtHomeBut);

            // Handle OnClick events
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCall(position);
                }
            });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSMS(position);
                }
            });

//            email.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    sendEmail(position);
//                }
//            });

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMap(position);
                }
            });

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(position);
                }
            });

            keeper.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    showKeeper();
                }
            });
        }

        return  convertView;
    }

    private void makeCall(int position)
    {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mContacts.get(position).get_phoneNumber()));
        mContext.startActivity(callIntent);
    }

    private void sendSMS(int position)
    {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
        smsIntent.putExtra("address", getItem(position).get_phoneNumber());
        smsIntent.putExtra("sms_body", "Hi " + getItem(position).get_name() + ". I 'll deliver new stuffs to you soon, babe <3");
        mContext.startActivity(smsIntent);
    }

    //    private void sendEmail(int position)
//    {
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, getItem(position)._email);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey Deadpool here");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "X-Force is recruiting on Linkedin");
//        context.startActivity(Intent.createChooser(emailIntent, "Send email to " + getItem(position)._name));
//    }

    private void openMap(int position) {
        Intent mapIntent = new Intent(mContext, MapsActivity.class);
        mapIntent.putExtra("ContactInfo", getItem(position));
        mContext.startActivity(mapIntent);
    }

    private void showDetail(int position) {
        Intent detailIntent = new Intent(mContext, DetailActivity.class);
        detailIntent.putExtra("ContactInfo", getItem(position));
        mContext.startActivity(detailIntent);
    }

    private void showKeeper() {
        Intent keeperIntent = new Intent(mContext, KeeperManagerActivity.class);
        mContext.startActivity(keeperIntent);
    }
}