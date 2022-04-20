package com.myroutine.myroutine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SlideShow extends AppCompatActivity {

    private ViewFlipper vp;
    private int index = 0;
    private List<Step> step;
    TextView countdown, title;
    DatabaseHelper mydb;
    CountDownTimer timer;
    boolean running;
    ImageButton refreh, pnp;
    String holder;
    MediaPlayer mp;
    ImageView image;
    boolean playing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        mydb = new DatabaseHelper(this);
        vp = (ViewFlipper) findViewById(R.id.picflipper);
        countdown = (TextView) findViewById(R.id.timerslide);
        title = (TextView) findViewById(R.id.slidetite);
        title.setText("");
        pnp = (ImageButton) findViewById(R.id.playanp);
        countdown.setText("");
        int x = getIntent().getExtras().getInt("taskid");
        step = mydb.getAllSteps(x);
        image = getNewImageView();
        image.setImageResource(R.drawable.swiping);
        image.setBackgroundColor(ContextCompat.getColor(this, R.color.gainsboro));


        //vp.setDisplayedChild(vp.indexOfChild(image));
        vp.addView(image);
    }

    public void onRefresh(View view) {
        if (running) {
            timer.start();
        }
    }

    protected ImageView getNewImageView() {
        ImageView image = new ImageView(getApplicationContext());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        return image;
    }

    protected Step getNextImage() {
        /*if (index == step.size())
            index = 0;*/
        if(!(index<0 || index>step.size())){
            return step.get(index++);
        }else{
            pnp.setVisibility(View.GONE);
            countdown.setText("");
            return null;
        }

    }

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {   //goes back
                    if (x2 > x1) {

                        cancelTimer();
                        stopRlaying();
                        //to not let the next slide reach size of list
                        index = index - 1;
                        //to get the previous timer
                        int res = index-1;
                        if(!(res<0)) {
                            vp.showPrevious();
                            Step sss = step.get(res);
                            title.setText(sss.getName());
                            runtimer(sss);
                            playRecording(sss);
                        }else{
                            goHome();
                        }

                    } else {
                        //goes next
                        vp.recomputeViewAttributes(image);
                        if (index == step.size()) {
                            goHome();
                        }else{
                            cancelTimer();
                            stopRlaying();
                            Step s = getNextImage();
                            if(s != null) {
                                title.setText(s.getName());
                                playRecording(s);
                                runtimer(s);
                                ImageView imageView = getNewImageView();// Where we will place the image
                                Bitmap x = BitmapFactory.decodeFile(s.getPic());
                                imageView.setImageBitmap(x);
                                vp.addView(imageView);
                                // Adding the image to the flipper
                                vp.showNext();
                            }
                        }


                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }



    public void cancelTimer() {
        if (running) {
            running = false;
            timer.cancel();
            refreh.setVisibility(View.GONE);
            countdown.setText("");

        }
    }

    public void runtimer(Step s) {
        if (!s.getTimer().equals("")) {
            //holder = s.getTimer();
            refreh = (ImageButton) findViewById(R.id.refresh);
            if (refreh.getVisibility() == View.GONE) {
                refreh.setVisibility(View.VISIBLE);
            }
            count(s.getTimer());
        }
    }

    public void pauseOrplay(View view) {
        ImageButton x = (ImageButton) view;
        String imageName = (String) x.getTag();
        if (imageName.equals("R.drawable.stopbutton")) {
            x.setTag("R.drawable.playbutton");
            pnp.setImageResource(R.drawable.playbutton);
            stopRlaying();

        } else {

            pnp.setImageResource(R.drawable.stopbutton);
            playRecording(step.get(index-1));
            //pnp.setVisibility(View.GONE);
            //mp.start();
            //Toast.makeText(this, "play", Toast.LENGTH_SHORT).show();

        }

        //if(pnp.gi)


    }


    public void playRecording(Step s) {

        if (s.getAudio() != null) {

            if (pnp.getVisibility() == View.GONE) {
                pnp.setVisibility(View.VISIBLE);
            }
            pnp.setImageResource(R.drawable.stopbutton);
            pnp.setTag("R.drawable.stopbutton");
            mp = new MediaPlayer();
            try {
                playing = true;
                mp.setDataSource(s.getAudio());
                mp.prepare();
                //Toast.makeText(getApplicationContext(), "recording playing", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }else{
            pnp.setVisibility(View.GONE);
        }
        if(playing) {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pnp.setImageResource(R.drawable.playbutton);
                    pnp.setTag("R.drawable.playbutton");
                }
            });
        }



    }

    public void stopRlaying() {
        if (playing) {
            playing = false;
            if (mp != null) {
                mp.stop();
                mp.release();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (running) {
            timer.cancel();    // timer is a reference to my inner CountDownTimer class

        }
    }


    public void count(String time) {
        long b = (long) Double.parseDouble(time.replace(":", ".").trim());
        timer = new CountDownTimer((b * 60000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                running = true;
                //A TimeUnit represents time durations at a given unit of granularity and provides
                // utility methods to convert across units, and to perform timing and delay
                // operations in these units.
                countdown.setText(String.format(Locale.US, "%d:%2d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes(millisUntilFinished))));

            }

            @Override
            public void onFinish() {
                countdown.setText("done");
            }
        }.start();
    }

    public void goHome(){
        if(getIntent().hasExtra("userpage")){
            Intent x = new Intent(this, UserPage.class);
            startActivity(x);
            stopRlaying();
            cancelTimer();
        }else{
            Intent x = new Intent(this, SuperUserPage.class);
            startActivity(x);
            stopRlaying();
            cancelTimer();
        }

    }


}

