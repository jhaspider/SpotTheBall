package bamboobush.com.wheresx.utils;

import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

public final class AppUtils {

    private static final String spkey = "BAMBOOBUSH_SHARED_PREF";
    private static final int mp = MODE_PRIVATE;
    private static final String defStr = "";

    // Keys for the shared preferences
    public static final String update_reminder = "update_reminder";
    public static final String IsFirstTimeLaunch = "is_first_time_launch";
    public static final String IsOutOfLife = "is_out_of_life";
    public static final String IsClueLessMsgShow = "is_clue_msg";
    public static final String RenewalTime = "renewal_time";
    public static final String LevelIndex = "level_index";

    public static final String GetScore = "get_score";
    public static final String LifeRemaining = "life_remaining";


    private AppUtils() {
        throw new AssertionError();
    }

    // String settings
    public static void setKey(Context c, String k, String v) {
        c.getSharedPreferences(spkey, mp).edit().putString(k, v).apply();
    }

    public static String getKey(Context c, String k) {
        return c.getSharedPreferences(spkey, mp).getString(k, defStr);
    }

    // Long settings
    public static void setKeyLong(Context c, String k, long v) {
        c.getSharedPreferences(spkey, mp).edit().putLong(k, v).apply();
    }

    public static long getKeyLong(Context c, String k) {
        return c.getSharedPreferences(spkey, mp).getLong(k,0);
    }

    // Int settings
    public static void setKeyInt(Context c, String k, int v) {
        c.getSharedPreferences(spkey, mp).edit().putInt(k, v).apply();
    }

    public static int getKeyInt(Context c, String k) {
        return c.getSharedPreferences(spkey, mp).getInt(k,0);
    }

    // Boolean settings
    public static void setKeyBool(Context c, String k, boolean v) {
        c.getSharedPreferences(spkey, mp).edit().putBoolean(k, v).apply();
    }

    public static boolean getKeyBool(Context c, String k) {
        return c.getSharedPreferences(spkey, mp).getBoolean(k, false);
    }

}
