package com.example.brianscott.caresit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UserHome extends AppCompatActivity
{
    EditText startTime;
    EditText endTime;
    EditText startDate;
    EditText endDate;
    EditText description;
    ListView theListView;
    Button purchaseButton;
    Firebase serviceRequestsRef;
    ArrayList<HashMap> theRequests;
    ListAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Firebase.setAndroidContext(this);

        theRequests = new ArrayList<HashMap>();

        Core.myFirebaseRef.child("service requests").addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                HashMap newRequest = new HashMap();
                newRequest = (HashMap) snapshot.getValue();
                theRequests.add(newRequest);
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError error)
            {

            }

        });
        //need to change from hashmap into something presentable when we show it in the listview
        //doens't like it at all when the data changes, says something about notifyDataSetChanged() needs to be added to the adapter.
        //seems like some bushi becuase when I change
        startTime = (EditText) this.findViewById(R.id.startTimeEditText);
        endTime = (EditText) this.findViewById(R.id.endTimeEditText);
        startDate = (EditText) this.findViewById(R.id.startDateEditText);
        endDate = (EditText) this.findViewById(R.id.endDateEditText);
        description = (EditText) this.findViewById(R.id.descriptionEditText);

        theListView = (ListView) this.findViewById(R.id.userListView);
        theAdapter = new ArrayAdapter<HashMap>(this, android.R.layout.simple_list_item_1, theRequests);
        theListView.setAdapter(theAdapter);


        purchaseButton = (Button)this.findViewById(R.id.purchaseButton);

        serviceRequestsRef = Core.myFirebaseRef.child("service requests");

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request newRequest = new Request();
                newRequest.setEndDate(endDate.getText().toString());
                newRequest.setDescription(description.getText().toString());
                newRequest.setEndTime(endTime.getText().toString());
                newRequest.setStartDate(startDate.getText().toString());
                newRequest.setStartTime(startTime.getText().toString());

                serviceRequestsRef.push().setValue(newRequest);

                startDate.setText("");
                startTime.setText("");
                endDate.setText("");
                endTime.setText("");
                description.setText("");
            }
        });
    }
}
