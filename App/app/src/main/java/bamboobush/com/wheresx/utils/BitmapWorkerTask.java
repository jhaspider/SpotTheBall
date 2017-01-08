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
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by amarjitjha on 24/10/16.
 */
public class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap> {

    public String url;
    private String data = "";
    public BitmapResponse delegate = null;
    public String type = "";
    public static int calling_from_product_details=0;

    public static final String LOAD_AND_SET = "loadAndSet";

    private final WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(ImageView imageView) {

        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    public BitmapWorkerTask() {
        imageViewReference = null;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];


        // Loading data from an http/https source
        try {
            return downloadBitmap(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        // When working with local resource
        //return decodeSampledBitmapFromResource( parentContext.getResources(), data, 100, 100);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (type.equals(LOAD_AND_SET)) {

            if (isCancelled()) {
                bitmap = null;
            }
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapWorkerTask bitmapDownloaderTask = ImageLoader.getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                if (this == bitmapDownloaderTask) {
                    imageView.setImageBitmap(bitmap);
                    delegate.bitmapLoadFinish(type,bitmap);
                }
            }
        } else {
            delegate.bitmapLoadFinish(type,bitmap);
        }

    }

    public static Bitmap downloadBitmap( String http_url ) throws IOException {

        URL url = new URL( http_url );

        // valid whether the url belongs to https or http
        HttpURLConnection urlConnection;
        if( http_url.startsWith("https") ) {
            urlConnection = (HttpsURLConnection) url.openConnection();
        } else {
            urlConnection = (HttpURLConnection) url.openConnection();
        }
        urlConnection.setConnectTimeout(1500);

        try {

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            /**
             * Note: a bug in the previous versions of BitmapFactory.decodeStream may prevent this
             * code from working over a slow connection. Decode a new FlushedInputStream(inputStream)
             * instead to fix the problem. Here is the implementation of this helper class:
             */
            in = new FlushedInputStream(in);
            final Bitmap bitmap = BitmapFactory.decodeStream(in);
            return bitmap;

        } finally {
            urlConnection.disconnect();
        }

    }

    static class FlushedInputStream extends FilterInputStream {

        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int bte = read(); //byte
                    if (bte < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
