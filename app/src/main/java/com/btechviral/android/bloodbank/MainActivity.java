package com.btechviral.android.bloodbank;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String UserId, UserEmail, Bloodgrp = "B-";
    ArrayList<Item> itemList = new ArrayList<>();
    Item item;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            UserId = firebaseUser.getUid();
            UserEmail = firebaseUser.getEmail();
            Log.d("Email", UserEmail);
            Log.d("Uid", UserId);
            final ListView listView = (ListView)findViewById(R.id.listView);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
            listView.setAdapter(adapter);

            final EditText nametext = (EditText)findViewById(R.id.nameText);
            final EditText placetext = (EditText)findViewById(R.id.placeText);
            final EditText bloodtext = (EditText)findViewById(R.id.bloodText);
            final EditText mhtext = (EditText)findViewById(R.id.medicText);
            final Button button = (Button)findViewById(R.id.addButton);
            final Button button2 = (Button)findViewById(R.id.queryButton);

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                    startActivity(intent);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bloodgrp = bloodtext.getText().toString();
                    item = new Item(nametext.getText().toString(), placetext.getText().toString(), bloodtext.getText().toString(), mhtext.getText().toString());
                    databaseReference.child("users").child(item.getBloodGroup()).child("items").push().setValue(item);
                    nametext.setText("");
                    placetext.setText("");
                    bloodtext.setText("");
                    mhtext.setText("");
                }
            });
            databaseReference.child("users").child(Bloodgrp).child("items").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    adapter.add((String) dataSnapshot.child("bloodGroup").getValue());
                    adapter.add((String) dataSnapshot.child("name").getValue());
                    adapter.add((String) dataSnapshot.child("medicHist").getValue());
                    adapter.add((String) dataSnapshot.child("place").getValue());

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    adapter.remove((String) dataSnapshot.child("name").getValue());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    databaseReference.child("users").child(UserId).child("items")
                            .orderByChild("name")
                            .equalTo((String) listView.getItemAtPosition(position))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        Log.d("Count", ""+dataSnapshot.getChildrenCount());
                                        //firstChild.getRef().removeValue();
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Item i = snapshot.getValue(Item.class);
                                            itemList.add(i);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            });
            final Button logout = (Button)findViewById(R.id.logoutButton);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });


        }
    }
}
