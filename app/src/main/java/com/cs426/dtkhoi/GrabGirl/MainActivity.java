package com.cs426.dtkhoi.GrabGirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Contact> contacts = getContactList();

        ContactAdapter contactAdapter = new ContactAdapter(this, contacts);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(contactAdapter);
    }

    private ArrayList<Contact> getContactList() {
        ArrayList<Contact> contacts = new ArrayList<>();

        contacts.add(new Contact(R.drawable.mbappe,"Kylian Mbappe",19, "123", 14.7,"Airpod", "Quận 12"));
        contacts.add(new Contact(R.drawable.neymar, "Neymar", 26,"456",8.4,"Wheelchair", "Quận 2"));
        contacts.add(new Contact(R.drawable.hashimotokanna, "Hashimoto Kanna", 16, "123", 7.1, "Airpod", "Quận 7"));
        contacts.add(new Contact(R.drawable.nene, "Pornpan Pornpenpipat", 18, "456", 5.1, "Watch", "Quận 8"));

//        contacts.add(new Contact(R.drawable.kyliejenner, "Kylie Jenner", 20, "789", 5, "Iphone X", "CGV Su Van Hanh"));
//        contacts.add(new Contact(R.drawable.lukamodric,"Luka Modric", 32, "789", 4,"Iphone X", "Toong Co-working Space"));
        return contacts;
    }
}