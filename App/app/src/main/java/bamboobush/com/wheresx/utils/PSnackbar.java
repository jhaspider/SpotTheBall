/*
 * Copyright (c) 2016.
 * Developer : Amarjit Jha
 * Fantain Sports Pvt Ltd
 * http://www.fantain.com
 */

package bamboobush.com.wheresx.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Amarjit Jha (Fantain) on 16/12/16.
 * <p>
 * Snackbar with some customisations
 */

public class PSnackbar {

    public static Snackbar make(View view, String msg, int duration)
    {
        Snackbar snackbar = Snackbar.make( view, msg, duration);

        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if(textView!=null) {
            textView.setTextColor(Color.WHITE);
        }
        textView.setMaxLines(3); // Change this based on the final message requirement
        snackbar.setActionTextColor(Color.YELLOW);

        // Show the button in snackbar
        return snackbar;

    }

}
