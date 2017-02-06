package bamboobush.com.wheresx.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pratim Bhowmik on 4/12/2016.
 */
public class HttpUrlConnectionTask {
    String result = "";
    final int CONNECTION_TIMEOUT = 40000;
    final int DATARETRIEVAL_TIMEOUT = 40000;
    public String getPostresponse(String targetUrl, String param){
        URL url;
        HttpURLConnection con = null;
        try{
            //create connection
            url = new URL(targetUrl);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // if the parameter length is known then we usse following line otherwisw we use con.setChunkedStreamingMode(0);
            con.setFixedLengthStreamingMode(param.getBytes().length);
            // or the following lne
            /*con.setRequestProperty("Content-Length", "" +
                    Integer.toString(param.getBytes().length));*/
            //con.setRequestProperty("Content-Language", "en-US");

            con.setUseCaches(false);
            con.setDoInput(true);
            // initially it is get method. setDoOutput(true) means wr are using post method now.
            con.setDoOutput(true);

            //send request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(param);
            wr.flush();
            wr.close();


            // handle issues
            int statusCode = con.getResponseCode();
            if (statusCode == 200) {
                //Get response
                InputStream in = con.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                result = response.toString();
            }else {
                result = HttpStatusHandeling.getJsonObject(statusCode);
            }

        }catch (Exception e){

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject("{\"status\":\"4\",\"message\":\"Could not connect to the server.\"}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            result = jsonObject.toString();
        }finally {
            if (con != null){
                con.disconnect();
            }
        }
        return result;
    }

    public static Bitmap downloadBitmap( String http_url ) throws IOException {

        URL url = new URL( http_url );
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
