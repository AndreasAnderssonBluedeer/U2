package SplitAndMerge;

/**
 * To be used for finished regions in Split&Merge. "Finshed from splitting- ready to merge."
 * Created by Andreas on 2016-01-05.
 */
public class FinishedRegion {
    private int width,height,colorValue,posX,posY;
    public FinishedRegion(int width,int height,int colorValue,int posX,int posY){
        this.width=width;
        this.height=height;
        this.colorValue=colorValue;
        this.posX=posX;
        this.posY=posY;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getColorValue(){
        return colorValue;
    }
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
}
