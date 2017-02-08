package bamboobush.com.wheresx;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import bamboobush.com.wheresx.data.HotSpots;
import bamboobush.com.wheresx.data.SourceImage;
import bamboobush.com.wheresx.data.User;
import bamboobush.com.wheresx.databinding.ActivityStartBinding;
import bamboobush.com.wheresx.fragments.ScoreDetailBottomSheet;
import bamboobush.com.wheresx.utils.AppGlobal;
import bamboobush.com.wheresx.utils.AppUtils;
import bamboobush.com.wheresx.utils.AsyncResponse;
import bamboobush.com.wheresx.utils.BitmapResponse;
import bamboobush.com.wheresx.utils.BitmapWorkerTask;
import bamboobush.com.wheresx.utils.PSnackbar;
import bamboobush.com.wheresx.utils.PitchQAsyn;
import bamboobush.com.wheresx.utils.Urls;

/**
 * Created by Amarjit Jha (Fantain) on 03/01/17.
 * <p>
 * TODO : Rel 1.0
 *     - Proportion of image to be set in local database
 *     - ADD CC Image
 *     - UI to be 100% completed
 *     - Create 10 levels of game & upload
 *     - Deep link Pitthoo for download and enter the contest
 * TODO : - Google Push Notification Integration
 * TODO : - API Integration & Image caching
 * TODO : - Offline loading
 * TODO : - Testing & bug fixing
 * TODO : - Level completion chart
 * TODO : - Disclaimer integration
 */


public class StartActivity extends AppCompatActivity
        implements View.OnTouchListener, BitmapResponse, View.OnClickListener, ScoreDetailBottomSheet.OnBottomSheetListener, AsyncResponse {

    private RelativeLayout hotspot_container;
    private LinearLayout prize_info;
    private CardView life_out_panel;
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
    ActivityStartBinding startBinding;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = AppGlobal.getInstance().getUser();
        user.initFromSP(getApplicationContext());
        startBinding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        startBinding.setUser(user);

        // Draw some dynamic views over the image to locate the ball
        hotspot_container = (RelativeLayout) findViewById(R.id.hotspot_container);
        life_out_panel = (CardView) findViewById(R.id.life_out_panel);
        life_out_panel.setOnTouchListener(this);

        heading_container = (LinearLayout) findViewById(R.id.heading_container);
        txt_life_remaining = (TextView) findViewById(R.id.txt_life_remaining);
        //txt_life_remaining.startAnimation();
        txt_user_score = (TextView) findViewById(R.id.txt_user_score);

        prize_info = (LinearLayout) findViewById(R.id.prize_info);
        prize_info.setOnClickListener(this);

        life_out_text = (TextView) findViewById(R.id.life_out_text);
        source_image_view = (ImageView) findViewById(R.id.source_image_view);
        /*source_image_view.setTag(-1); // to recognize it as base image in onTouch handler
        source_image_view.setOnTouchListener(this);*/
        circular_progress = (ProgressBar) findViewById(R.id.circular_progress);

        success_container = (LinearLayout) findViewById(R.id.success_container);
        success_tick = (ImageView) findViewById(R.id.success_tick);
        success_tick.setBackgroundResource(R.drawable.picked_correctly);
        tickAnimation = (AnimationDrawable) success_tick.getBackground();


        // Call to load tasks
        loadTasks();

    }

    private void loadTasks() {
        PitchQAsyn pitchQAsyn = new PitchQAsyn();
        pitchQAsyn.type = PitchQAsyn.GET_TASKS;
        pitchQAsyn.delegate = this;
        String urlParams = "";
        try {
            urlParams = "&version_code="+BuildConfig.VERSION_CODE;
        }catch(Exception e){
            Log.e("ParamsException-->", e.getMessage());
        }
        Object[] obj = new Object[]{Urls.playing_tasks,urlParams};
        pitchQAsyn.execute(obj);
    }

    private void continueSetup() {

        // Load First task
        // If user was playing some level, start from that level only
        //current_index = AppUtils.getKeyInt(getApplicationContext(),AppUtils.LevelIndex);
        current_index = user.getLevel();
        sourceImage = sourceImageArrayList.get(current_index);

        // Validate, whether life remaining for the user
        int lifeRemaining = user.getLife_left();
        if(lifeRemaining==0){

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

            LoadImage();

        }
    }

    private void LoadImage() {


                circular_progress.setVisibility(View.VISIBLE);
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

                    user.decrementLife(getApplicationContext());
                    ReduceLife();
                }

                isTappedOnhotspot = false;
                break;
        }

        return super.onTouchEvent(event);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v instanceof CardView){
            return true;
        }

        int img_tag = (int) v.getTag();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(img_tag<0) {

            if( isTappedOnhotspot==false
                    && sourceImage.is_clue_less()
                    && ( y>=taskimage_start_y && y<=taskimage_end_y ) ) {

                user.decrementLife(getApplicationContext());
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
                /*success_container.setVisibility(View.VISIBLE);
                tickAnimation.start();*/

                // As soon as correct image is found, make it invisible
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((ImageView) v).setImageDrawable(getDrawable(R.drawable.ic_check_circle));
                } else {
                    ((ImageView) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle));
                }


                final StartActivity that = this;
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Update Score & Display
                        int wonScore = user.getLife_left() * sourceImage.getBase_score();
                        user.updateScore( getApplicationContext(), wonScore );

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
                            user.incrementLevel(getApplicationContext());


                            LoadImage();

                            // TODO : Animate image loading


                        } else {
                            // TODO : We should never allow to run into this scenario
                            hotspot_container.setVisibility(View.GONE);
                            heading_container.setVisibility(View.VISIBLE);
                        }


                    }
                }, 1000);




            } else {

                user.decrementLife(getApplicationContext());
                ReduceLife();

            }

        }

        return false;
    }

    private void ReduceLife() {

        Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        source_image_view.startAnimation(blink);
        txt_life_remaining.startAnimation(blink);

        // Check, whether all the life is used
        if(user.getLife_left()>0) {

            // TODO : Do some animation to alert the user that life has reduced
            // Life remaining display
            //LifeRemaining(sourceImage.getLife_remainning());

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
            life_out_panel.setVisibility(View.VISIBLE);
            ShowRemainingTime();
        } else {

            if(timer!=null) {
                timer.cancel();
                timer = null;
            }

            AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsOutOfLife, false);
            AppUtils.setKeyLong(getApplicationContext(),AppUtils.RenewalTime, 0);

            heading_container.setVisibility(View.VISIBLE);
            life_out_panel.setVisibility(View.GONE);

            // Load the current level task
            // Reset all the lifes
            user.resetLife(getApplicationContext(), sourceImage.getLife() );
            LoadImage();

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
            float scale = (float) source_image_view.getWidth() / (float) bitmap.getWidth();
            if ((scale * (float) bitmap.getHeight()) > (float) source_image_view.getHeight()) {
                scale = (float) source_image_view.getHeight() / (float) bitmap.getHeight();
            }
            sourceImage.setScale(scale);

            // Setting the source image
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            /*int w = (int) scale * bitmap.getWidth();
            int h = (int) scale * bitmap.getHeight();*/
            //Bitmap resizedBitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            source_image_view.setImageBitmap( bitmap );

            // Add HotSpots on the platform
            AddHotSpots(bitmap);

            // Show Life Available for this level
            user.resetLife( getApplicationContext(), sourceImage.getLife());

            // Hide the progress loader of image
            // Making top panel visible
            circular_progress.setVisibility(View.GONE);
            heading_container.setVisibility(View.VISIBLE);

        }

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

            case R.id.prize_info:
                Intent summary_panel = new Intent( StartActivity.this, PointSummaryActivity.class);
                startActivity( summary_panel );

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

    @Override
    public void processFinish(String type, String output) {

        if(type.equals(PitchQAsyn.GET_TASKS)){

            JSONObject obj = null;
            try {
                obj = new JSONObject(output);
                if (obj.getString("status").equals("1")) {
                    startBinding.setIsDataLoaded(true);
                    JSONArray tasksJA = obj.getJSONArray("tasks");
                    JSONObject taskJO;
                    JSONArray hotSpotJA;
                    JSONObject hotSpotJO;

                    HotSpots hotSpots;
                    SourceImage sourceImage;
                    for( int i=0; i<tasksJA.length(); i++) {

                        taskJO = tasksJA.getJSONObject(i);
                        hotSpotJA = taskJO.getJSONArray("hotspots");


                        // Init Objects
                        sourceImage = new SourceImage(taskJO.getInt("width"),
                                taskJO.getInt("height"),
                                taskJO.getString("image"));
                        sourceImage.setLife( taskJO.getInt("life") );
                        sourceImage.setIncrement_time_min( taskJO.getInt("renew_time") );
                        sourceImage.setBase_score( taskJO.getInt("base_score") );

                        // Parsing hotspots
                        for( int j=0; j<hotSpotJA.length(); j++) {

                            hotSpotJO = hotSpotJA.getJSONObject(j);

                            hotSpots = new HotSpots(hotSpotJO.getDouble("x"), hotSpotJO.getDouble("y"), hotSpotJO.getBoolean("is_correct"));
                            sourceImage.getHotSpotsArrayList().add(hotSpots);
                        }

                        sourceImageArrayList.add(sourceImage);

                    }

                    // Initialize contest
                    continueSetup();

                } else {
                    startBinding.setIsDataLoaded(false);
                    Snackbar another_warning = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.could_not_connect), Snackbar.LENGTH_INDEFINITE);
                    another_warning.setAction(getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadTasks();
                        }
                    }).show();
                    another_warning.show();
                }
            } catch (JSONException e) {
                startBinding.setIsDataLoaded(false);
                e.printStackTrace();
            }

        }

    } // End of processing fee

}