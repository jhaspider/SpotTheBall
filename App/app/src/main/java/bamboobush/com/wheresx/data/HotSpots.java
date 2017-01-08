package bamboobush.com.wheresx.data;

/**
 * Created by Amarjit Jha (Fantain) on 03/01/17.
 * <p>
 * TODO : CLASS DESCRIPTION GOES HERE
 */

public class HotSpots {

    private double x, y;
    private boolean is_correct;
    private boolean isVisible = true;


    public HotSpots(double x, double y, boolean is_correct) {
        this.x = x;
        this.y = y;
        this.is_correct = is_correct;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean is_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
