package com.example.user.personaldiary;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TRAVEL extends AppCompatActivity {


    private RecyclerView mTravelList;
    private DatabaseReference mTravelDatabase;
    //private EditText mTravelField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        mTravelDatabase = FirebaseDatabase.getInstance().getReference().child("TRAVEL");

        mTravelList =(RecyclerView) findViewById(R.id.travel_list);
        mTravelList.setHasFixedSize(true);
        mTravelList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Travel_des, TravelViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Travel_des,TravelViewHolder>(

                Travel_des.class,
                R.layout.travel_row,
                TravelViewHolder.class,
                mTravelDatabase
        ){

            @Override
            protected void populateViewHolder(TravelViewHolder viewHolder, Travel_des model, int position){
                viewHolder.setTitle(model.getTRAVEL_PLAN());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }

        };
        mTravelList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class TravelViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TravelViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setTitle(String Title) {
            TextView travel_dest = (TextView) mView.findViewById(R.id.travel_dest);
            travel_dest.setText(Title);
        }

        public void setImage(Context ctx, String image){
            ImageView travelImages = (ImageView) mView.findViewById(R.id.travel_image);
            Picasso.with(ctx).load(image).into(travelImages);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
