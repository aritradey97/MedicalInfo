package com.btechviral.android.bloodbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    FirebaseUser firebaseUser;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            final EditText editText = (EditText)findViewById(R.id.queryText);
            final  ListView listView = (ListView)findViewById(R.id.listView);
            firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");

            query = "B-";

            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Item item = snapshot.getValue(Item.class);
                            itemList.add(item);
                            items.add(item.getName());
                            items.add(item.getPlace());
                            items.add(item.getMedicHist());
                            items.add(" ");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            firebaseDatabase.child(query).child("items").addListenerForSingleValueEvent(valueEventListener);
        }
    }
}
