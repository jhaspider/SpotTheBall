package bamboobush.com.wheresx.utils;

import android.os.AsyncTask;

/**
 * Created by ms-andro1 on 4/15/2016.
 */
public class PitchQAsyn extends AsyncTask<Object, Void, String> {

    public AsyncResponse delegate = null;
    private String url;
    public String type = "";

    // Constants
    public static final String GET_TASKS = "getTasks";
    private String string_JSON = "";

    @Override
    protected String doInBackground(Object... params) {
        HttpUrlConnectionTask httpUrlconnection = new HttpUrlConnectionTask();
        string_JSON = httpUrlconnection.getPostresponse((String) params[0], (String) params[1]);
        return string_JSON;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(type, string_JSON);
    }
}
