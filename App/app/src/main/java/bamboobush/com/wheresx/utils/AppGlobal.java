package bamboobush.com.wheresx.utils;

import bamboobush.com.wheresx.data.User;

/**
 * Created by Amarjit Jha (Fantain) on 28/01/17.
 * <p>
 * TODO : CLASS DESCRIPTION GOES HERE
 */
public class AppGlobal {

    private static AppGlobal ourInstance = new AppGlobal();
    private User user = new User();

    public static AppGlobal getInstance() {
        return ourInstance;
    }

    private AppGlobal() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
