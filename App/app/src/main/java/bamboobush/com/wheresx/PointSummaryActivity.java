package bamboobush.com.wheresx;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import bamboobush.com.wheresx.databinding.ActivityPointSummaryBinding;
import bamboobush.com.wheresx.utils.AppGlobal;
import bamboobush.com.wheresx.utils.PitthooDialog;

public class PointSummaryActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*ImageButton btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);*/
        // Assigning data binding objects
        ActivityPointSummaryBinding pointSummaryBinding = DataBindingUtil.setContentView(this, R.layout.activity_point_summary);
        pointSummaryBinding.setUser(AppGlobal.getInstance().getUser() );
        pointSummaryBinding.setHandler( this );


    }

    public void onCloseClicked(View v) {
        super.onBackPressed();
    }

    public void aboutPitthoo(View v){

        PitthooDialog pitthooDialog = new PitthooDialog(this);
        pitthooDialog.show();

    }

    public void downloadPitthoo(View v){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse( getString(R.string.pitthoo_url) )));
    }

}
