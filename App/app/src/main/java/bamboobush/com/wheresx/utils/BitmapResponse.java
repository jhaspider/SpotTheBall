/*
 *
 *  * Copyright (c) 2016.
 *  * Amarjit Jha
 *  * Fantain Sports Pvt Ltd
 *  * http://www.fantain.com
 *
 */

package bamboobush.com.wheresx.utils;

import android.graphics.Bitmap;

/**
 * Created by amarjitjha on 24/10/16.
 */
public interface BitmapResponse {

    /**
     * This method return the result from the web service response.
     */
    void bitmapLoadFinish(String type, Bitmap bitmap);

}
