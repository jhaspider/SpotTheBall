package bamboobush.com.wheresx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import bamboobush.com.wheresx.data.HotSpots;
import bamboobush.com.wheresx.data.SourceImage;
import bamboobush.com.wheresx.fragments.ScoreDetailBottomSheet;
import bamboobush.com.wheresx.utils.AppUtils;
import bamboobush.com.wheresx.utils.BitmapResponse;
import bamboobush.com.wheresx.utils.BitmapWorkerTask;
import bamboobush.com.wheresx.utils.PSnackbar;

/**
 * Created by Amarjit Jha (Fantain) on 03/01/17.
 * <p>
 * TODO : - Google Push Notification Integration
 * TODO : - API Integration & Image caching
 * TODO : - Offline loading
 * TODO : - Testing & bug fixing
 * TODO : - Level completion chart
 * TODO : - Disclaimer integration
 */


public class StartActivity extends AppCompatActivity
        implements View.OnTouchListener, BitmapResponse, View.OnClickListener, ScoreDetailBottomSheet.OnBottomSheetListener {

    private RelativeLayout hotspot_container, life_out_panel;
    private LinearLayout heading_container, success_container;
    private TextView txt_life_remaining, life_out_text, txt_user_score;
    private ImageView hotspot, source_image_view, success_tick;
    private ArrayList<ImageView> hotspotImageViews = new ArrayList<>();

    private SourceImage sourceImage;
    private ArrayList<SourceImage> sourceImageArrayList = new ArrayList<>();
    private int current_index = 0;
    private boolean isTappedOnhotspot = false;
    private long reinstate_time;
    private Timer timer;
    private ProgressBar circular_progress;
    private AnimationDrawable tickAnimation;

    double taskimage_start_y, taskimage_end_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);

        // Draw some dynamic views over the image to locate the ball
        hotspot_container = (RelativeLayout) findViewById(R.id.hotspot_container);
        life_out_panel = (RelativeLayout) findViewById(R.id.life_out_panel);
        life_out_panel.setOnTouchListener(this);

        heading_container = (LinearLayout) findViewById(R.id.heading_container);
        txt_life_remaining = (TextView) findViewById(R.id.txt_life_remaining);
        //txt_life_remaining.startAnimation();
        txt_user_score = (TextView) findViewById(R.id.txt_user_score);
        txt_user_score.setOnClickListener(this);

        life_out_text = (TextView) findViewById(R.id.life_out_text);
        source_image_view = (ImageView) findViewById(R.id.source_image_view);
        /*source_image_view.setTag(-1); // to recognize it as base image in onTouch handler
        source_image_view.setOnTouchListener(this);*/
        circular_progress = (ProgressBar) findViewById(R.id.circular_progress);

        success_container = (LinearLayout) findViewById(R.id.success_container);
        success_tick = (ImageView) findViewById(R.id.success_tick);
        success_tick.setBackgroundResource(R.drawable.picked_correctly);
        tickAnimation = (AnimationDrawable) success_tick.getBackground();

        // Item 1
        HotSpots hotSpots;
        sourceImage = new SourceImage(501,
                699,
                "http://worldcricketevents.com/wp-content/uploads/2015/08/images-for-dhoni-helicopter-shot-against-malinga.img_.jpg");
        sourceImage.setLife(2);
        sourceImage.setIncrement_time_min(2);
        sourceImage.setBase_score(10);
        hotSpots = new HotSpots(385,135,true);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(160,200,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );


        sourceImage = new SourceImage(482,
                512,
                "https://s-media-cache-ak0.pinimg.com/originals/fd/77/a8/fd77a80a0380a879722023ab552c6b6f.jpg");
        sourceImage.setLife(3);
        sourceImage.setIncrement_time_min(3);
        sourceImage.setBase_score(20);

        hotSpots = new HotSpots(71,276,true);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(405,430,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(100,316,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );


        // Item 2
        sourceImage = new SourceImage(700,
                400,
                "http://ste.india.com/sites/default/files/2016/04/03/475430-virat-kohli-odis-pull-700.jpg");
        sourceImage.setLife(4);
        sourceImage.setIncrement_time_min(3);
        sourceImage.setBase_score(20);

        hotSpots = new HotSpots(577,229,true);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(463,296,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );

        // Item 3
        sourceImage = new SourceImage(800,
                500,
                "http://cdn.parhlo.com/wp-content/uploads/2015/02/277632621.jpg");
        sourceImage.setLife(5);
        sourceImage.setIncrement_time_min(4);
        sourceImage.setBase_score(30);

        hotSpots = new HotSpots(335,59,true);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(236,355,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(544,400,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );

        // Item 4
        sourceImage = new SourceImage(628,
                673,
                "http://st2.cricketcountry.com/wp-content/uploads/cricket/image_20131015131036.jpg");
        sourceImage.setLife(5);
        sourceImage.setIncrement_time_min(5);
        sourceImage.setBase_score(40);

        sourceImage.setIs_clue_less(true);
        hotSpots = new HotSpots(50,200,true);
        hotSpots.setVisible(false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );

        // Item 5

        sourceImage = new SourceImage(500,
                668,
                "http://pitthoo.co.in/mahi.jpeg");
        sourceImage.setLife(5);
        sourceImage.setIncrement_time_min(10);
        sourceImage.setBase_score(50);

        sourceImage.setIs_clue_less(true);
        hotSpots = new HotSpots(53,509,true);
        hotSpots.setVisible(false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );





        // Load First task
        // If user was playing some level, start from that level only
        current_index = AppUtils.getKeyInt(getApplicationContext(),AppUtils.LevelIndex);
        sourceImage = sourceImageArrayList.get(current_index);
        LoadImage();


        boolean isOutOfLife = AppUtils.getKeyBool(getApplicationContext(),AppUtils.IsOutOfLife);

        // Show users accumulative score
        int userScore = AppUtils.getKeyInt(getApplicationContext(),AppUtils.GetScore);
        ShowScore(userScore);

        // Display remaining life
        int lifeRemaining = AppUtils.getKeyInt(getApplicationContext(),AppUtils.LifeRemaining);
        if(lifeRemaining>0 || isOutOfLife ) {
            sourceImage.setLife_remainning(lifeRemaining);
            LifeRemaining(sourceImage.getLife_remainning());
        }

        // Outoflife scenario handling
        if(isOutOfLife){

            // Show a timer with time remaining for the life to get added
            reinstate_time = AppUtils.getKeyLong(getApplicationContext(),AppUtils.RenewalTime);
            SetOutOfLife();

        } else {

            // Welcome message to news user, who come to this game for the first time
            boolean IsFirstTimeLaunch = AppUtils.getKeyBool(getApplicationContext(),AppUtils.IsFirstTimeLaunch);
            if(!IsFirstTimeLaunch) {
                AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsFirstTimeLaunch, true);
                Snackbar snackbar = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.reveal_the_ball), Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }

    }

    private void LoadImage() {
        circular_progress.setVisibility(View.VISIBLE);
        AppUtils.setKeyInt(getApplicationContext(),AppUtils.LevelIndex, current_index);

        BitmapWorkerTask workerTask = new BitmapWorkerTask();
        workerTask.delegate = this;
        workerTask.execute(sourceImage.getUrl());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                if( isTappedOnhotspot==false
                        && sourceImage.is_clue_less()
                        && ( y>=taskimage_start_y && y<=taskimage_end_y ) ) {

                    ReduceLife();

                }

                isTappedOnhotspot = false;
                break;
        }


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v instanceof RelativeLayout){
            return true;
        }

        int img_tag = (int) v.getTag();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(img_tag<0) {

            if( isTappedOnhotspot==false
                    && sourceImage.is_clue_less()
                    && ( y>=taskimage_start_y && y<=taskimage_end_y ) ) {

                ReduceLife();
            }
            isTappedOnhotspot = false;

        } else
        if(img_tag>=0) {
            isTappedOnhotspot = true;

            int ind_position = (int) v.getTag();

            HotSpots hotSpots = sourceImage.getHotSpotsArrayList().get(ind_position);
            Snackbar snackbar;
            if(hotSpots.is_correct()){

                // Play correct action animation
                success_container.setVisibility(View.VISIBLE);
                tickAnimation.start();

                // As soon as correct image is found, make it invisible
                v.setVisibility(View.GONE);

                // Update Score & Display
                int wonScore = sourceImage.getLife_remainning() * sourceImage.getBase_score();
                int userScore = AppUtils.getKeyInt(getApplicationContext(),AppUtils.GetScore);
                final int total_score = userScore + wonScore;
                AppUtils.setKeyInt(getApplicationContext(),AppUtils.GetScore, total_score);
                ShowScore( total_score );

                // Load Next Level
                current_index++;
                if(current_index<sourceImageArrayList.size()) {

                    // Delete, if there was any previous hot spot on the stage
                    if(hotspotImageViews.size()>0){
                        for(int i=0; i<hotspotImageViews.size(); i++) {
                            hotspot = hotspotImageViews.get(i);
                            hotspot_container.removeView( hotspot );
                        }
                    }
                    // Remove image of the current level
                    source_image_view.setImageBitmap(null);
                    heading_container.setVisibility(View.GONE);
                    life_out_panel.setVisibility(View.GONE);

                    // Load the next level
                    sourceImage = sourceImageArrayList.get(current_index);
                    LoadImage();

                } else {
                    // TODO : We should never allow to run into this scenario
                    hotspot_container.setVisibility(View.GONE);
                    heading_container.setVisibility(View.VISIBLE);
                }


                // Show users score and message
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        success_container.setVisibility(View.GONE);

                        ScoreDetailBottomSheet promotionPanel = new ScoreDetailBottomSheet();
                        promotionPanel.setMessage( getString(R.string.user_score, String.valueOf(total_score)) );
                        promotionPanel.show(getSupportFragmentManager(), "hellow");
                    }
                }, 700);


                // Tasks :
                //  - Play some animation to celebrate the success of the user
                //  - Add points to user account
                //  - Ask user to connect ones mobile number (Optional)
                //  - When user reaches some threshold value/points, Prompt them to redeem the points on Pitthoo for a reward
                /*snackbar = PSnackbar.make(findViewById(android.R.id.content),getString(R.string.CONGRAT),Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(getString(R.string.action_next), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        current_index++;
                        if(current_index<sourceImageArrayList.size()) {

                            // Delete, if there was any previous hot spot on the stage
                            if(hotspotImageViews.size()>0){
                                for(int i=0; i<hotspotImageViews.size(); i++) {
                                    hotspot = hotspotImageViews.get(i);
                                    hotspot_container.removeView( hotspot );
                                }
                            }
                            // Remove image of the current level
                            source_image_view.setImageBitmap(null);
                            heading_container.setVisibility(View.GONE);
                            life_out_panel.setVisibility(View.GONE);

                            // Load the next level
                            sourceImage = sourceImageArrayList.get(current_index);
                            LoadImage();

                        } else {
                            hotspot_container.setVisibility(View.GONE);
                            heading_container.setVisibility(View.VISIBLE);
                        }

                    }
                });
                snackbar.show();*/

            } else {

                ReduceLife();

            }

        }

        return false;
    }

    private void ReduceLife() {

        Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        source_image_view.startAnimation(blink);

        txt_life_remaining.startAnimation(blink);

        sourceImage.setLife_remainning( sourceImage.getLife_remainning()-1 );
        AppUtils.setKeyInt(getApplicationContext(),AppUtils.LifeRemaining, sourceImage.getLife_remainning());

        // Check, whether all the life is used
        if(sourceImage.getLife_remainning()>0) {

            // TODO : Do some animation to alert the user that life has reduced

            // Life remaining display
            LifeRemaining(sourceImage.getLife_remainning());

        } else {

            long total_sec = sourceImage.getIncrement_time_min() * 60 * 1000;

            Calendar cal = Calendar.getInstance();
            reinstate_time = cal.getTimeInMillis() + total_sec;

            // Set shared preference to indicate that user is out of life
            AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsOutOfLife, true);
            AppUtils.setKeyLong(getApplicationContext(),AppUtils.RenewalTime, reinstate_time);

            // Show a timer with time remaining for the life to get added
            SetOutOfLife();

        }
    }

    private void SetOutOfLife() {

        heading_container.setVisibility(View.GONE);
        life_out_panel.setVisibility(View.VISIBLE);

        timer = new Timer("QuestionTimer");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        UpdateTimeToLife();
                    }
                });
            }
        }, 1000, 1000 );

    }


    /**
     * Callback method to the timer task
     *
     */
    private void UpdateTimeToLife() {

        Calendar cal = Calendar.getInstance();
        if(cal.getTimeInMillis()<reinstate_time) {
            ShowRemainingTime();
        } else {

            if(timer!=null) {
                timer.cancel();
                timer = null;
            }

            AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsOutOfLife, false);
            AppUtils.setKeyLong(getApplicationContext(),AppUtils.RenewalTime, 0);

            sourceImage.setLife_remainning(sourceImage.getLife());
            AppUtils.setKeyInt(getApplicationContext(),AppUtils.LifeRemaining, sourceImage.getLife_remainning());
            LifeRemaining(sourceImage.getLife_remainning());

            heading_container.setVisibility(View.VISIBLE);
            life_out_panel.setVisibility(View.GONE);


        }
    }


    /**
     * Show the remaining time in the textview
     *
     */
    private void ShowRemainingTime() {

        Calendar cal = Calendar.getInstance();
        long total_sec = reinstate_time - cal.getTimeInMillis();

        long secs = total_sec / 1000; // Gets a temprorary seconds out of milliseconds, for ease of calculation
        long min_m = 60;
        long m = secs/min_m; // Get minutes remaining E.G. 14/60 = 0
        long s = (secs%min_m); // Gets seconds remaining E.G. 14%60 = 14, 9%2 = 1
        long ms = (total_sec%1000)/10; // Getting the reamining milliseconds value

        final String minutes = (m < 10) ? "0" + m : (String.valueOf(m));
        final String seconds = (s < 10) ? "0" + s : (String.valueOf(s));
        final String milseconds = (ms < 10) ? "0" + ms : (String.valueOf(ms));

        life_out_text.setText(getString(R.string.life_renewal_time, minutes+":"+seconds ));

    }

    /**
     * The callback method to bitmap data loading completion
     *
     * @param type
     * @param bitmap
     */
    @Override
    public void bitmapLoadFinish(String type, Bitmap bitmap) {

        if(bitmap==null) {

            Snackbar another_warning = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.could_not_connect), Snackbar.LENGTH_INDEFINITE);
            another_warning.setAction(getString(R.string.try_again), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadImage();
                }
            }).show();
            another_warning.show();

        } else {
            // Determine the scale
            double scale = (double) source_image_view.getWidth() / (double) bitmap.getWidth();
            if ((scale * (double) bitmap.getHeight()) > (double) source_image_view.getHeight()) {
                scale = (double) source_image_view.getHeight() / (double) bitmap.getHeight();
            }
            sourceImage.setScale(scale);

            // Setting the source image
            source_image_view.setImageBitmap(bitmap);

            // Add HotSpots on the platform
            AddHotSpots(bitmap);

            // Show Life Available for this level
            LifeRemaining(sourceImage.getLife_remainning());
            AppUtils.setKeyInt(getApplicationContext(), AppUtils.LifeRemaining, sourceImage.getLife_remainning());

            // Hide the progress loader of image
            // Making top panel visible
            circular_progress.setVisibility(View.GONE);
            heading_container.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Shows the life remaining counter to the user
     *
     * @param life_remainning
     */
    private void LifeRemaining(int life_remainning) {
        txt_life_remaining.setText( getString(R.string.life_remaining, String.valueOf(life_remainning)) );
    }

    private void ShowScore(int score ){
        txt_user_score.setText( getString(R.string.user_score, String.valueOf(score)) );
    }

    /**
     * Add the touchable hot spots on the stage in all scenario right after image loading is completed
     *
     * @param bitmap
     */
    private void AddHotSpots(Bitmap bitmap) {

        taskimage_start_y = ( (double)source_image_view.getHeight() - ((double) bitmap.getHeight() * sourceImage.getScale()) )/2;
        taskimage_end_y = taskimage_start_y + ((double) bitmap.getHeight() * sourceImage.getScale());

        // Position the hotspot at the ball
        for(int i=0; i<sourceImage.getHotSpotsArrayList().size(); i++) {

            HotSpots hotSpots = sourceImage.getHotSpotsArrayList().get(i);

            RelativeLayout.LayoutParams lprams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            hotspot = new ImageView(hotspot_container.getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                hotspot.setImageDrawable(getDrawable(R.drawable.ic_brightness_1_black_24dp));
            } else {
                hotspot.setImageDrawable( getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp));
            }
            hotspot.setLayoutParams(lprams);

            // Validate, whether hot spot should be visible or not
            if(!hotSpots.isVisible()){
                hotspot.setAlpha(0f);
            }

            // Assigning click listener and tag, to check the reference
            hotspot.setTag(i);
            hotspot.setOnTouchListener(this);

            // Adding the image view object on stage
            // Keep a refrence of hotspots to clean in next steps
            hotspot_container.addView( hotspot );
            hotspotImageViews.add( hotspot );

            // Positioning the object on stage
            float hotspot_x = (float) (sourceImage.getScale() * hotSpots.getX()) - 30;
            float hotspot_y = (float) ((sourceImage.getScale() * hotSpots.getY()) + taskimage_start_y) - 30;

            hotspot.setX(hotspot_x);
            hotspot.setY(hotspot_y);

        }

    }

    /**
     *
     * Handled click event of following items
     *  -
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.txt_user_score:

                int userScore = AppUtils.getKeyInt(getApplicationContext(),AppUtils.GetScore);

                ScoreDetailBottomSheet promotionPanel = new ScoreDetailBottomSheet();
                promotionPanel.setMessage( getString(R.string.user_score, String.valueOf(userScore)) );
                promotionPanel.show(getSupportFragmentManager(), "hellow");
                break;

        }

    }

    /**
     * Stop the timer, as the application enter the pause state
     *
     */
    @Override
    protected void onPause() {
        super.onPause();

        Log.e("PAUSED"," - Inside paused");

    }

    /**
     * Resinstates the timer, if time still left to reinstate the life
     *
     */
    @Override
    protected void onResume() {
        super.onResume();

        Log.e("RESUMED"," - Inside resumed");
    }

    @Override
    public void onBottomSheetReturn(String action) {

        Log.e("CALLBACK"," - " + action);
        // Validated, if this slide is clue less
        boolean isMessageShown = AppUtils.getKeyBool(getApplicationContext(),AppUtils.IsClueLessMsgShow);
        if(sourceImage.is_clue_less() && !isMessageShown ){
            AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsClueLessMsgShow, true);
            Snackbar another_warning = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.without_hot_spot), Snackbar.LENGTH_LONG);
            another_warning.show();
        }

    }

}