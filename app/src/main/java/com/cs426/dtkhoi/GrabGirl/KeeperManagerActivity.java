package com.cs426.dtkhoi.GrabGirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class KeeperManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keeper_list);
        ArrayList<Keeper> keepers = getKeeperList();

        KeeperAdapter keeperAdapter = new KeeperAdapter(this, keepers);
        ListView keeperView = findViewById(R.id.listViewKeeper);
        keeperView.setAdapter(keeperAdapter);
    }

    private ArrayList<Keeper> getKeeperList() {
        ArrayList<Keeper> keepers = new ArrayList<>();

        keepers.add(new Keeper(R.drawable.kyliejenner,"Kylie  Jenner", 1.5));
        keepers.add(new Keeper(R.drawable.nene,"Nene", 2));
        keepers.add(new Keeper(R.drawable.mbappe,"Kylian Mbappe", 4));

        return  keepers;
    }
}

