import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * This class contains and perform Hysteresis and non-maximum suppression as an edge thinning technique.
 * The main part of the Canny's method.
 * Created by Andreas Andersson & David Isberg on 2015-12-10.
 */
public class CannysMethod {

    /**
     * Perform Non-Maximum Supression (Edge Thinning)- uses values from the input image and
     * returns a output raster with the "new" image (raster).
     * @param raster -input raster with the color values from the image.
     * @return WritableRaster
     */
    public WritableRaster edgeThinning(WritableRaster raster){
        ArrayList<Double> degrees=new ArrayList<Double>();

        WritableRaster outRaster=raster;

        // For every pixel in the image- compare it to a 3x3 mask.
                int[][] matrix=new int[3][3];
                for(int row=1;row<raster.getWidth()-1;row++) {
                    for (int col = 1; col < raster.getHeight() - 1; col++) {

                        matrix[0][0] = raster.getSample(row - 1, col - 1, 0);
                        matrix[0][1] = raster.getSample(row - 1, col, 0);
                        matrix[0][2] = raster.getSample(row - 1, col + 1, 0);
                        matrix[1][0] = raster.getSample(row, col - 1, 0);
                        matrix[1][2] = raster.getSample(row, col + 1, 0);
                        matrix[2][0] = raster.getSample(row + 1, col - 1, 0);
                        matrix[2][1] = raster.getSample(row + 1, col, 0);
                        matrix[2][2] = raster.getSample(row + 1, col + 1, 0);


                //Vertical
                double gy=(matrix[0][0]*-1)+(matrix[0][1]*-2)+(matrix[0][2]*-1)+(matrix[2][0])+(matrix[2][1]*2)+(matrix[2][2]);
                //Horizontal
                double gx=(matrix[0][0]*-1)+(matrix[0][2])+(matrix[1][0]*-2)+(matrix[1][2]*2)+(matrix[2][0]*-1)+(matrix[2][2]);
                //Calculate Gradient Direction.
                        Double direction=Math.atan2(gy,gx);
                        direction=Math.toDegrees(direction);
                        degrees.add(direction);

                //If the angle is negative convert it to positive.
                if(direction<0){
                    direction=180+direction;
                }
                //Round to closest direction-> 0,45,90,135 or 180.
                int closestAngleDifference=200;    //Random number- just needs to be bigger then the closest angle.
                double closestDirection=0;
                        int[] angles= {0,45,90,135,180};
                        for (int i=0;i<angles.length;i++){
                           //Compare the difference to find the closest angle.
                            int difference=0;
                            difference=angles[i]-direction.intValue();
                            if (difference<0){
                                //Ex: difference= -5. -5--5*2 =5. Convert to positive difference.
                                difference=difference-difference*2;
                            }
                            //Save the closest value.
                            closestAngleDifference=Math.min(closestAngleDifference,difference);
                          if (closestAngleDifference==difference){
                              closestDirection=angles[i];
                          }
                        }
                        direction=closestDirection;
                //Depending on direction set the pixel to a part of the Edge or not.
                //Angle 0 or 180-> North/South
                if(direction==0 || direction==180){
                    if(raster.getSample(row,col,0) < raster.getSample(row-1,col,0) || raster.getSample(row,col,0) < raster.getSample(row+1,col,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
                //Angle 45-> NorthWest/SouthEast.
                else if (direction==45){
                    if(raster.getSample(row,col,0) < raster.getSample(row-1,col-1,0) || raster.getSample(row,col,0) < raster.getSample(row+1,col+1,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
                //Angle = 90,West/East.
                else if (direction==90){
                    if(raster.getSample(row,col,0) < raster.getSample(row,col-1,0) || raster.getSample(row,col,0) < raster.getSample(row,col+1,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
                //Angle= 135 -> NorthEast/SouthWest.
                else if (direction==135){

                    if(raster.getSample(row,col,0) < raster.getSample(row-1,col+1,0)|| raster.getSample(row,col,0) < raster.getSample(row+1,col-1,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
            }
        }
        return outRaster;
    }

    /**
     * This method performs hysteresis. Receives an WritableRaster to fetch input values from and returns an
     * output WritableRaster. Set's a low and high threshold.
     * @param raster- raster with input values to work with.
     * @return WritableRaster
     */
    public WritableRaster hysteresis(WritableRaster raster){
        int lowThreshold,highTreshold;
        //Ask the user for low and high thresholds.
        lowThreshold=Integer.parseInt(JOptionPane.showInputDialog("Mata in ett tal mellan 0-255 för Low Threshold."));
        highTreshold=Integer.parseInt(JOptionPane.showInputDialog("Mata in ett tal mellan 0-255 för High Threshold som" +
                " är större än Low Threshold:"+lowThreshold));

        for(int row=0; row<raster.getHeight(); row++){
            for (int col=0; col<raster.getWidth(); col++){
                //If pixel is higher than the high threshold
                if( raster.getSample(col,row,0)>=highTreshold){
                    //Do nothing, we're gonna keep it.
                }
                //If pixel is lower than the low threshold, set pixel to 0. (this pixel will not be used)
                else if (raster.getSample(col,row,0)<=lowThreshold){
                    raster.setSample(col,row,0,0);
                }
                //If the pixel is in between the two thresholds-> check if it should be used
                //Is it a neighbour to a high threshold pixel?
                else {
                        int i=row-1,k=col-1;
                        boolean neighbour=false;
                        while(i<=row+1){
                            while (k<=col+1){
                                //if its not a pixel at the end/edge of the image. To prevent out of bounds.
                                if (i>0 && i < raster.getHeight() &&
                                        k>0 && k < raster.getWidth()) {
                                    if (raster.getSample(k, i, 0) >= highTreshold) {
                                        neighbour = true;
                                    }
                                }
                                k++;
                            }
                            i++;
                        }
                        //If there wasn't any high threshold neighbour-> delete it.
                        if (!neighbour){
                            raster.setSample(col,row,0,0);
                        }

                }
            }
        }
        return raster;
    }
}
