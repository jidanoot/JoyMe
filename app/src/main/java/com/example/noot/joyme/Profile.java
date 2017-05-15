package com.example.noot.joyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    private Button btnEvent;
    private TextView textUsername, textFriend;
    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/events/posts");

    private ListView listView;
    private EventListAdapter eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        initInstances();
    }

    private void initInstances() {
        btnEvent = (Button) findViewById(R.id.btnEvent);
        textUsername = (TextView) findViewById(R.id.textUsername);
        textFriend = (TextView) findViewById(R.id.textFriend);

        listView = (ListView) findViewById(R.id.listView);
        eventListAdapter = new EventListAdapter();
        listView.setAdapter(eventListAdapter);

        updateUser();

//        listView.setOnClickListener(listener);
        btnEvent.setOnClickListener(listener);
        ref.addChildEventListener(childEventListener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnEvent:
                    startActivity(new Intent(Profile.this,EventMap.class));
                    break;
                case R.id.listView:
                    Toast.makeText(getApplicationContext(),listView.getSelectedItemId()+"",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            if (!Data.getInstance().getKeyPost().contains(prevChildKey)) {
                Post newPost = dataSnapshot.getValue(Post.class);
                System.out.println("print key " + prevChildKey);
                Data.getInstance().insertPost(newPost);
                Data.getInstance().insertKeyPost(prevChildKey);
                eventListAdapter.notifyDataSetChanged();
                System.out.println("print new post " + newPost);
                System.out.println("print children " + newPost.getTitle());
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}

    };

    private void updateUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        textUsername.setText(user.getEmail());
        textFriend.setText("1 Friend");
    }

}
