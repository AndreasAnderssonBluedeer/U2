package SplitAndMerge;

/**
 * Created by Andreas on 2016-01-05.
 */
public class Region {

    private int [][] array;
    private int posX,posY;
    public Region(int[][] array, int posX,int posY){
        this.array=array;
        this.posX=posX;
        this.posY=posY;
    }
    public int[][] getArray(){
        return array;
    }
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
}
