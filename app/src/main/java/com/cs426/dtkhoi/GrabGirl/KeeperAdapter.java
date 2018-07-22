package com.cs426.dtkhoi.GrabGirl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class KeeperAdapter extends ArrayAdapter<Keeper> {
    private final Context mContext;
    private ArrayList<Keeper> mKeepers;

    // Constructor
    KeeperAdapter(Context context, ArrayList<Keeper> keepers) {
        super(context,0,keepers);
        this.mContext = context;
        this.mKeepers = keepers;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    // Change view ... FILL IN DETAIL LATER
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.mykeeper_list_layout, null);

            // Update contact info to a row correspond to the position or index
            Keeper row_keeper = mKeepers.get(position);

            ImageView imageView = convertView.findViewById(R.id.avatar);
            imageView.setImageResource(row_keeper.get_avatar_id());

            TextView name = convertView.findViewById(R.id.name);
            name.setText("Keeper: " + row_keeper.get_name());

            TextView distance = convertView.findViewById(R.id.distance);
            distance.setText("Distance: " + row_keeper.get_distance() + " km");
        }
        return  convertView;
    }
}

