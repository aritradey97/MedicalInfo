package com.btechviral.android.bloodbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    FirebaseUser firebaseUser;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();
    String queryText = "B-", queryText2;

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
            final EditText editText2 = (EditText)findViewById(R.id.queryText2);
            final  ListView listView = (ListView)findViewById(R.id.listView);
            firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");



            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            final ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Item item = snapshot.getValue(Item.class);
                            itemList.add(item);
                            items.add("Name: "+ item.getName() + "\n"+"Place: "+ item.getPlace() + "\n"
                                    + "Medical History: " + item.getMedicHist());
//                            items.add(item.getPlace());
//                            items.add(item.getMedicHist());
//                            items.add(" ");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
//            if(queryText2 == null){
//                firebaseDatabase.child(queryText).child("items").addListenerForSingleValueEvent(valueEventListener);
//            } else {
//                Query query = FirebaseDatabase.getInstance().getReference("users").child(queryText).orderByChild("place").equalTo(queryText2);
//                query.addListenerForSingleValueEvent(valueEventListener);
//            }
            Button button = (Button)findViewById(R.id.searchButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString() != null)
                        queryText = editText.getText().toString();
                    queryText2 = editText2.getText().toString();
                    firebaseDatabase.child(queryText).child("items").orderByChild("place").equalTo(queryText2).addListenerForSingleValueEvent(valueEventListener);
                }
            });
        }
    }
}
