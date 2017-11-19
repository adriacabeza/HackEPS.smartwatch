package com.example.alvaro.healthcare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
   static String id;
   Context context;
    String nom;
    int edat;
    Boolean uncop = true;
    String sala1;
    private Bitmap aBigBitmap;
    private String url;
    String prova;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        final Context context = getApplicationContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://healthcare-e8aee.appspot.com");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference pacientRef = database.getReference("consultes");
        final DatabaseReference pendentsRef = database.getReference("pendents");
        final DatabaseReference infermeraRef = database.getReference("infSolic");

        //tinc la base de dades iniciada i l storage

        mTextView = (TextView) findViewById(R.id.text);


        final TextView sala = (TextView) findViewById(R.id.sala);
        final TextView pacients = (TextView) findViewById(R.id.pacient);
        final ImageButton mImage = (ImageButton) findViewById(R.id.podrit);
        final TextView edat = (TextView) findViewById(R.id.edat);
        final Button mButton = (Button) findViewById(R.id.button);
        final ImageView Image = (ImageView) findViewById(R.id.image);
        final ImageView Image1 = (ImageView) findViewById(R.id.image1);
        final ImageView Image2 = (ImageView) findViewById(R.id.image2);
        final ImageView Image3 = (ImageView) findViewById(R.id.image3);
        final ImageView Image4 = (ImageView) findViewById(R.id.image4);
        final ImageView Image5 = (ImageView) findViewById(R.id.image5);

        Image.setVisibility(View.INVISIBLE);
        Image1.setVisibility(View.INVISIBLE);
        Image2.setVisibility(View.INVISIBLE);
        Image3.setVisibility(View.INVISIBLE);
        Image4.setVisibility(View.INVISIBLE);
        Image5.setVisibility(View.INVISIBLE);



        infermeraRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue().toString();
                if(a.equals("{123={idMetge=123, acceptat=false}, 0={idMetge=00, acceptat=false}}")) {
                    sala.setVisibility(View.INVISIBLE);
                    edat.setVisibility(View.INVISIBLE);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    mButton.setBackgroundColor(Color.parseColor("#255d00"));
                    pacients.setText("El metge Nº 123 Solicita ajuda a la sala 23.");

                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pacients.setText("No hi ha cap pacient esperant");
                            mButton.setBackgroundColor(Color.parseColor("#c62828"));
                            pacients.setVisibility(View.VISIBLE);
                            infermeraRef.child("123").child("acceptat").setValue("true");


                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pendentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                {
                    for (DataSnapshot child : dataSnapshot.getChildren())

                        if((dataSnapshot.child("-1").child("id").getValue().toString()).equals("0")){
                            pacients.setText("No hi ha cap pacient esperant");
                            sala.setVisibility(View.INVISIBLE);
                            edat.setVisibility(View.INVISIBLE);
                        }
                        else{
                        String adria=child.getKey();
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            long[] vibrationPattern = {0, 500, 50, 300};
                            //-1 - don't repeat
                            final int indexInPatternToRepeat = -1;
                            vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
                            if(adria.equals("-1")) {}
                            else{
                                sala.setVisibility(View.VISIBLE);
                                edat.setVisibility(View.VISIBLE);
                                pacientRef.child("0").setValue(adria);
                            }


                        }
                     ;}

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //també ficar el sexe i la edat de la persona
        // que es treguin les notificacions quan cliqui o quan baixi el rellotge
        //pensar si el color podria canviar el sexe
        //li ha agradat la idea d podrit
        //puru lio amb lo de cita i lo de prova
        //Albert Polo 619 893 241


        pacientRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                      pacients.setText(id);

                id = (String) dataSnapshot.child("0").getValue();
                      pendentsRef.child(id).setValue(null);

                nom = (String) dataSnapshot.child(id + "/nom").getValue();
                sala.setText("S" + dataSnapshot.child(id + "/sala").getValue().toString());
                edat.setText(dataSnapshot.child(id + "/edat").getValue().toString());
                String sexe = dataSnapshot.child(id + "/sexe").getValue().toString();
                String podrit = dataSnapshot.child(id + "/podrit").getValue().toString();
                if (sexe.equals("0")) {
                    mButton.setBackgroundColor(Color.parseColor("#c62828"));
                } else {
                    mButton.setBackgroundColor(Color.parseColor("#1565c0"));

                }
                if (podrit.equals("0")) {
                    mImage.setBackgroundColor(Color.parseColor("#388e3c"));
                } else if (podrit.equals("1")) {
                    mImage.setBackgroundColor(Color.parseColor("#fdd835"));
                } else if (podrit.equals("2")) {
                    mImage.setBackgroundColor(Color.parseColor("#f57f17"));
                }
                pacients.setText(nom);

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            sala.setVisibility(View.INVISIBLE);
                            pacients.setVisibility(View.INVISIBLE);
                            edat.setVisibility(View.INVISIBLE);
                            String text = id +".jpg";
                            StorageReference imageRef = storageRef.child(text);

                            if(id.equals("1")){
                                Image.setVisibility(View.VISIBLE);
                                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/healthcare-e8aee.appspot.com/o/1.jpg?alt=media&token=1252709e-e57a-43df-8623-a1017e4e8174").asBitmap().skipMemoryCache(true).centerCrop().into(Image);}
                            else if (id.equals("2")){
                                Image1.setVisibility(View.VISIBLE);
                                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/healthcare-e8aee.appspot.com/o/2.jpg?alt=media&token=d897e7cd-e2f6-4e8c-b2cc-544424c13b43").asBitmap().skipMemoryCache(true).centerCrop().into(Image1);}
                            else if (id.equals("3")){
                                Image2.setVisibility(View.VISIBLE);
                                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/healthcare-e8aee.appspot.com/o/3.jpg?alt=media&token=89888a47-6239-440d-84bc-2fd1b67c795c").asBitmap().skipMemoryCache(true).centerCrop().into(Image2);}

                            else if (id.equals("4")){
                                Image3.setVisibility(View.VISIBLE);
                                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/healthcare-e8aee.appspot.com/o/4.jpg?alt=media&token=ee1fe9ec-f04e-41ba-acc9-e2ec5d8f9c1e").asBitmap().centerCrop().into(Image3);}
                            else if (id.equals("5")){
                                Image4.setVisibility(View.VISIBLE);
                                Glide.with(context).load(
                                    "https://firebasestorage.googleapis.com/v0/b/healthcare-e8aee.appspot.com/o/5.jpg?alt=media&token=cc094e00-65bd-47a2-a246-313e05e0e80a").asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).centerCrop().into(Image4);}
                            else if (id.equals("6")){
                                Image5.setVisibility(View.VISIBLE);
                                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/healthcare-e8aee.appspot.com/o/6.jpg?alt=media&token=72ff44af-9d63-4ee2-a293-51d6c7c7fd87").asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).centerCrop().into(Image5);}


                    }
                });

                mButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        if(id.equals("1")){
                            Image.setVisibility(View.INVISIBLE);}
                        else if (id.equals("2")){
                            Image1.setVisibility(View.INVISIBLE);}
                        else if (id.equals("3")){
                            Image2.setVisibility(View.INVISIBLE);}
                        else if (id.equals("4")){
                            Image3.setVisibility(View.INVISIBLE);}
                        else if (id.equals("5")){
                            Image4.setVisibility(View.INVISIBLE);
                           }
                        else if (id.equals("6")){
                            Image5.setVisibility(View.INVISIBLE);
                           }


                        sala.setVisibility(View.VISIBLE);
                        pacients.setVisibility(View.VISIBLE);
                        edat.setVisibility(View.VISIBLE);
                        Image.setVisibility(View.INVISIBLE);
                        pendentsRef.child("-1").child("id").setValue("0");
                        return true;
                    }
                });


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });



     }




}