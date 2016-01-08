package SplitAndMerge;

/**
 * To be used for finished regions in Split&Merge. "Finshed from splitting- ready to merge."
 * Contains variables for the size of a common region with a common colorvalue, start position for x&y
 * and width & height.
 * Created by Andreas on 2016-01-05.
 */
public class FinishedRegion {
    private int width,height,colorValue,posX,posY;

    /**
     * Constructor
     * @param width- the region width
     * @param height- the region height
     * @param colorValue- the regions colorvalue
     * @param posX- start coordinate X
     * @param posY- start coordinate Y
     */
    public FinishedRegion(int width,int height,int colorValue,int posX,int posY){
        this.width=width;
        this.height=height;
        this.colorValue=colorValue;
        this.posX=posX;
        this.posY=posY;
    }

    /**
     * Returns width
     * @return int
     */
    public int getWidth(){
        return width;
    }

    /**
     * Returns height
     * @return int
     */
    public int getHeight(){
        return height;
    }

    /**
     * Returns the color value.
     * @return int
     */
    public int getColorValue(){
        return colorValue;
    }

    /**
     * Returns X coordinate.
     * @return int
     */
    public int getPosX(){
        return posX;
    }

    /**
     * Returns Y coordinate.
     * @return int
     */
    public int getPosY(){
        return posY;
    }
}
