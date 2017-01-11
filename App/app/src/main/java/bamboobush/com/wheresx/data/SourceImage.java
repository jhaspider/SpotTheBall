package bamboobush.com.wheresx.data;

import java.util.ArrayList;

/**
 * Created by Amarjit Jha (Fantain) on 03/01/17.
 * <p>
 * TODO : CLASS DESCRIPTION GOES HERE
 */

public class SourceImage {

    private int width, height;
    private String url;
    private int life;
    private int life_remainning;
    private boolean is_clue_less;
    private int increment_time_min;

    private double scale;
    private int base_score;

    private ArrayList<HotSpots> hotSpotsArrayList = new ArrayList<>();


    public SourceImage( int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public ArrayList<HotSpots> getHotSpotsArrayList() {
        return hotSpotsArrayList;
    }

    public void setHotSpotsArrayList(ArrayList<HotSpots> hotSpotsArrayList) {
        this.hotSpotsArrayList = hotSpotsArrayList;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
        this.life_remainning = life;
    }

    public boolean is_clue_less() {
        return is_clue_less;
    }

    public void setIs_clue_less(boolean is_clue_less) {
        this.is_clue_less = is_clue_less;
    }

    public int getLife_remainning() {
        return life_remainning;
    }

    public void setLife_remainning(int life_remainning) {
        this.life_remainning = life_remainning;
    }

    public int getIncrement_time_min() {
        return increment_time_min;
    }

    public void setIncrement_time_min(int increment_time_min) {
        this.increment_time_min = increment_time_min;
    }

    public int getBase_score() {
        return base_score;
    }

    public void setBase_score(int base_score) {
        this.base_score = base_score;
    }
}
