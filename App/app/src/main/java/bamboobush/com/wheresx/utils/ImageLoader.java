/*
 * Copyright (c) 2016.
 * Developer : Amarjit Jha
 * Fantain Sports Pvt Ltd
 * http://www.fantain.com
 */

package bamboobush.com.wheresx.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.Random;


public class ImageLoader implements BitmapResponse {

    public BitmapResponse delegate = null;

    public void download(String url, ImageView imageView){
        if (cancelPotentialDownload(url, imageView)) {

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            color=0;
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(imageView);
            bitmapWorkerTask.type = BitmapWorkerTask.LOAD_AND_SET;
            bitmapWorkerTask.delegate = this;
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(bitmapWorkerTask,color);
            imageView.setImageDrawable(downloadedDrawable);
            bitmapWorkerTask.execute(url);

        }
    }



    private static boolean cancelPotentialDownload(String url, ImageView imageView) {

        BitmapWorkerTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    public static BitmapWorkerTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    @Override
    public void bitmapLoadFinish(String type, Bitmap bitmap) {
        if(delegate!=null){
            delegate.bitmapLoadFinish(type,bitmap);
        }
    }


    static class DownloadedDrawable extends ColorDrawable {

        private final WeakReference<BitmapWorkerTask> bitmapDownloaderTaskReference;
        public DownloadedDrawable(BitmapWorkerTask bitmapDownloaderTask, int color) {

            super(color);
            bitmapDownloaderTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapDownloaderTask);

        }

        public BitmapWorkerTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }


}
