package bamboobush.com.wheresx.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;

import java.util.zip.Inflater;

import bamboobush.com.wheresx.R;
import bamboobush.com.wheresx.databinding.PitthooDialogBinding;

/**
 * Created by Amarjit Jha (Fantain) on 05/02/17.
 * <p>
 * TODO : CLASS DESCRIPTION GOES HERE
 */

public class PitthooDialog extends Dialog {

    public PitthooDialog(Context context) {
        super(context);
        init(context);
    }

    public PitthooDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected PitthooDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context c){

        View view = getLayoutInflater().inflate( R.layout.pitthoo_dialog, null );
        setContentView(view);

        PitthooDialogBinding pitthooDialogBinding = DataBindingUtil.bind( view );
        pitthooDialogBinding.setUser( AppGlobal.getInstance().getUser() );
        pitthooDialogBinding.setHandler( this );

    }




}
