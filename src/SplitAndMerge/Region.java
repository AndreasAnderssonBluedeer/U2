package SplitAndMerge;

/**
 * A Region with an int array with color values and x&y position for position in the total image.
 * Created by Andreas Andersson & David Isberg on 2016-01-05.
 */
public class Region {

    private int [][] array;
    private int posX,posY;

    /**
     * Constructor with array and position for x & y
     * @param array -array with color values.
     * @param posX -int with x coordinate
     * @param posY-int with y coordinate
     */
    public Region(int[][] array, int posX,int posY){
        this.array=array;
        this.posX=posX;
        this.posY=posY;
    }

    /**
     * returns the array
     * @return int[][]
     */
    public int[][] getArray(){
        return array;
    }

    /**
     * Returns position X
     * @return int
     */
    public int getPosX(){
        return posX;
    }

    /**
     * Returns position Y
     * @return int
     */
    public int getPosY(){
        return posY;
    }
}
