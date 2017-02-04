package bamboobush.com.wheresx.data;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import bamboobush.com.wheresx.BR;
import bamboobush.com.wheresx.utils.AppUtils;

/**
 * Created by Amarjit Jha (Fantain) on 28/01/17.
 * <p>
 * TODO : Retaining the stats for display purposes
 */

public class User extends BaseObservable {

    private int score, level = 0, life_left;
    private String name, mobile;
    private int contest_score = 2000;


    public User() {

    }


    @Bindable
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    @Bindable
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        notifyPropertyChanged(BR.level);
    }

    @Bindable
    public int getLife_left() {
        return life_left;
    }

    public void setLife_left(int life_left) {
        this.life_left = life_left;
        notifyPropertyChanged(BR.life_left);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getContest_score() {
        return contest_score;
    }

    public void setContest_score(int contest_score) {
        this.contest_score = contest_score;
    }


    // Database / Shared preference methods
    public void initFromSP( Context c ) {
        this.score = AppUtils.getKeyInt(c,AppUtils.GetScore);
        this.level = AppUtils.getKeyInt(c,AppUtils.LevelIndex);
        this.life_left = AppUtils.getKeyInt(c,AppUtils.LifeRemaining);
    }

    public void updateScore(Context c, int total_score ) {
        score += total_score;
        notifyPropertyChanged(BR.score);
        AppUtils.setKeyInt(c,AppUtils.GetScore, score);
    }

    public void incrementLevel( Context c ){
        level++;
        notifyPropertyChanged(BR.level);
        AppUtils.setKeyInt(c,AppUtils.LevelIndex, level);
    }

    public void decrementLife( Context c) {
        life_left--;
        notifyPropertyChanged(BR.life_left);
        AppUtils.setKeyInt(c,AppUtils.LifeRemaining, life_left);
    }
    public void resetLife( Context c, int life) {
        life_left = life;
        notifyPropertyChanged(BR.life_left);
        AppUtils.setKeyInt(c,AppUtils.LifeRemaining, life_left);
    }




}
