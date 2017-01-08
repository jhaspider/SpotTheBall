package bamboobush.com.wheresx;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import bamboobush.com.wheresx.data.HotSpots;
import bamboobush.com.wheresx.data.SourceImage;
import bamboobush.com.wheresx.utils.AppUtils;
import bamboobush.com.wheresx.utils.BitmapResponse;
import bamboobush.com.wheresx.utils.BitmapWorkerTask;
import bamboobush.com.wheresx.utils.PSnackbar;

public class StartActivity extends AppCompatActivity  implements View.OnTouchListener, BitmapResponse {

    private RelativeLayout hotspot_container, life_out_panel;
    private LinearLayout heading_container;
    private TextView txt_life_remaining, life_out_text;
    private ImageView hotspot, source_image_view;
    private ArrayList<ImageView> hotspotImageViews = new ArrayList<>();

    private SourceImage sourceImage;
    private ArrayList<SourceImage> sourceImageArrayList = new ArrayList<>();
    private int current_index = 0;
    private boolean isTappedOnhotspot = false;
    private long reinstate_time;
    Timer timer;

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
        heading_container = (LinearLayout) findViewById(R.id.heading_container);
        txt_life_remaining = (TextView) findViewById(R.id.txt_life_remaining);
        life_out_text = (TextView) findViewById(R.id.life_out_text);
        source_image_view = (ImageView) findViewById(R.id.source_image_view);

        // Item 1
        HotSpots hotSpots;
        sourceImage = new SourceImage(482,
                512,
                "https://s-media-cache-ak0.pinimg.com/originals/fd/77/a8/fd77a80a0380a879722023ab552c6b6f.jpg");
        sourceImage.setLife(3);
        sourceImage.setIncrement_time_min(2);

        hotSpots = new HotSpots(71,276,true);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        hotSpots = new HotSpots(405,430,false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );


        // Item 2
        sourceImage = new SourceImage(700,
                400,
                "http://ste.india.com/sites/default/files/2016/04/03/475430-virat-kohli-odis-pull-700.jpg");
        sourceImage.setLife(5);
        sourceImage.setIncrement_time_min(3);

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

        sourceImage.setIs_clue_less(true);
        hotSpots = new HotSpots(50,200,true);
        hotSpots.setVisible(false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );

        // Item 5
        sourceImage = new SourceImage(950,
                651,
                "http://4.bp.blogspot.com/-pbNaQrH8HOU/UO0TBjdkqSI/AAAAAAAACNo/6s4Wa2zhnmg/s1600/mahendrasing+dhoni+helicoptar+short+hd+wallaper.jpg");
        sourceImage.setLife(5);
        sourceImage.setIncrement_time_min(10);

        sourceImage.setIs_clue_less(true);
        hotSpots = new HotSpots(107,375,true);
        hotSpots.setVisible(false);
        sourceImage.getHotSpotsArrayList().add( hotSpots );

        sourceImageArrayList.add( sourceImage );


        // Load image
        current_index = AppUtils.getKeyInt(getApplicationContext(),AppUtils.LevelIndex);
        sourceImage = sourceImageArrayList.get(current_index);

        LoadImage();


        /*hotspot = (ImageView) findViewById(R.id.hotspot);
        hotspot.setOnTouchListener(this);*/
        boolean isOutOfLife = AppUtils.getKeyBool(getApplicationContext(),AppUtils.IsOutOfLife);
        if(isOutOfLife){

            // Show a timer with time remaining for the life to get added
            reinstate_time = AppUtils.getKeyLong(getApplicationContext(),AppUtils.RenewalTime);
            SetOutOfTime();

        } else {
            Snackbar snackbar = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.reveal_the_ball), Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    private void LoadImage() {
        txt_life_remaining.setText(getString(R.string.loading_image));

        AppUtils.setKeyInt(getApplicationContext(),AppUtils.LevelIndex, current_index);

        BitmapWorkerTask workerTask = new BitmapWorkerTask();
        workerTask.delegate = this;
        workerTask.execute(sourceImage.getUrl());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("Position DOWN-- ", x + "::" + y);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("Position MOVE-- ", x + "::" + y);
                break;

            case MotionEvent.ACTION_UP:
                if(isTappedOnhotspot==false) {
                    sourceImage.setLife_remainning( sourceImage.getLife_remainning()-1 );

                    // Check, whether all the life is used
                    if(sourceImage.getLife_remainning()>0) {

                        // Life remaining display
                        LifeRemaining(sourceImage.getLife_remainning());

                    /*snackbar = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.INCORRECT), Snackbar.LENGTH_SHORT);
                    snackbar.show();*/

                    } else {

                        long total_sec = sourceImage.getIncrement_time_min() * 60 * 1000;

                        Calendar cal = Calendar.getInstance();
                        reinstate_time = cal.getTimeInMillis() + total_sec;

                        // Set shared preference to indicate that user is out of life
                        AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsOutOfLife, true);
                        AppUtils.setKeyLong(getApplicationContext(),AppUtils.RenewalTime, reinstate_time);

                        // Show a timer with time remaining for the life to get added
                        SetOutOfTime();

                    }
                }
                isTappedOnhotspot = false;
                break;
        }


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v instanceof ImageView) {
            isTappedOnhotspot = true;

            int ind_position = (int) v.getTag();

            HotSpots hotSpots = sourceImage.getHotSpotsArrayList().get(ind_position);
            Snackbar snackbar;
            if(hotSpots.is_correct()){

                // As soon as correct image is found, make it invisible
                v.setVisibility(View.GONE);

                // Tasks :
                //  - Play some animation to celebrate the success of the user
                //  - Add points to user account
                //  - Ask user to connect ones mobile number (Optional)
                //  - When user reaches some threshold value/points, Prompt them to redeem the points on Pitthoo for a reward
                snackbar = PSnackbar.make(findViewById(android.R.id.content),getString(R.string.CONGRAT),Snackbar.LENGTH_INDEFINITE);
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

                            sourceImage = sourceImageArrayList.get(current_index);
                            LoadImage();

                        } else {
                            hotspot_container.setVisibility(View.GONE);
                            heading_container.setVisibility(View.VISIBLE);
                            /*Snackbar another_warning = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.thatsall), Snackbar.LENGTH_LONG);
                            another_warning.show();*/
                        }

                    }
                });
                snackbar.show();

            } else {

                sourceImage.setLife_remainning( sourceImage.getLife_remainning()-1 );

                // Check, whether all the life is used
                if(sourceImage.getLife_remainning()>0) {

                    // Life remaining display
                    LifeRemaining(sourceImage.getLife_remainning());

                    /*snackbar = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.INCORRECT), Snackbar.LENGTH_SHORT);
                    snackbar.show();*/

                } else {

                    long total_sec = sourceImage.getIncrement_time_min() * 60 * 1000;

                    Calendar cal = Calendar.getInstance();
                    reinstate_time = cal.getTimeInMillis() + total_sec;

                    // Set shared preference to indicate that user is out of life
                    AppUtils.setKeyBool(getApplicationContext(),AppUtils.IsOutOfLife, true);
                    AppUtils.setKeyLong(getApplicationContext(),AppUtils.RenewalTime, reinstate_time);

                    // Show a timer with time remaining for the life to get added
                    SetOutOfTime();

                }

            }

        }

        return false;
    }

    private void SetOutOfTime() {

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
        }, 1000, 1 );

    }


    private void UpdateTimeToLife() {

        Log.e("SpotBall","Inside Update Timer");
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
            LifeRemaining(sourceImage.getLife_remainning());

            heading_container.setVisibility(View.VISIBLE);
            life_out_panel.setVisibility(View.GONE);


        }
    }

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


        Log.e("START", ""+total_sec);
        life_out_text.setText(getString(R.string.life_renewal_time, minutes+":"+seconds+":"+milseconds ));
    }

    @Override
    public void bitmapLoadFinish(String type, Bitmap bitmap) {

        // Determine the scale
        double scale = (double)source_image_view.getWidth() / (double)bitmap.getWidth();
        if( (scale*(double)bitmap.getHeight()) > (double)source_image_view.getHeight() ) {
            scale = (double)source_image_view.getHeight() / (double)bitmap.getHeight();
        }
        sourceImage.setScale( scale );

        // Setting the source image
        source_image_view.setImageBitmap( bitmap );

        // Add HotSpots on the platform
        AddHotSpots(bitmap);

        // Display remaining life
        LifeRemaining(sourceImage.getLife_remainning());

        // If user has reached last item, then execute this.
        // Validated, if this slide is clue less
        if(sourceImage.is_clue_less()){
            //if((sourceImageArrayList.size()-current_index)==1) {
            Snackbar another_warning = PSnackbar.make(findViewById(android.R.id.content), getString(R.string.without_hot_spot), Snackbar.LENGTH_LONG);
            another_warning.show();
        }

    }

    private void LifeRemaining(int life_remainning) {
        txt_life_remaining.setText( getString(R.string.life_remaining, String.valueOf(life_remainning)) );
    }

    private void AddHotSpots(Bitmap bitmap) {

        double screen_centre_v = ( (double)source_image_view.getHeight() - ((double) bitmap.getHeight() * sourceImage.getScale()) )/2;

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
            float hotspot_y = (float) ((sourceImage.getScale() * hotSpots.getY()) + screen_centre_v) - 30;

            hotspot.setX(hotspot_x);
            hotspot.setY(hotspot_y);

        }

    }

}