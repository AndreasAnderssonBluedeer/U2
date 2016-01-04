import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Created by Andreas on 2015-12-10.
 */
public class CannysMethod {
    //Inkl Förtunning och Hysteresis.

    public void gaussianFilter(BufferedImage img){
        int[][] mask=new int [5][5];    //Gaussian filter (kernel size)mask, 5x5 is normally good.

    }
    public void edgeThinning(int[][] picture){
       // ta emot en bild med gradientmagnituder?
        // för var pixel i bilden( Array) så kontroller riktningen med en array/mask på 3x3 pixlar där center pixeln är
        //den som ska kontrolleras.
        //Bilden är 2D
        //OutputPicture är "utdataBilden", Picture är Arrayen med Gradientvärdena (?).
        //picture[0].length=width(columns), picture.length=height (rows)

        BufferedImage outputPicture= new BufferedImage(picture[0].length, picture.length, BufferedImage.TYPE_BYTE_GRAY);

        // För varje pixel i bilden (-1 och +1 för att undvika "kantpixlarna"?)
        for(int row=1; row < picture.length-1; row++){
            for (int col=1; col < picture[row].length-1; col++){
                float direction=0;
                //beräkna gradient riktning? direction = 0,45,90,135?

                //Beroende på riktningen kontrollera om aktuell pixel ska användas.

                //Angle=0 -> Nord & Syd
                if(direction==0){
                    if(picture[row][col] < picture[row-1][col] || picture[row][col] < picture[row+1][col]){
                      //  picture[row][col]=0; eller en specific utdata bild? outputPicture[row][col]=0;
                        outputPicture.getRaster().setSample(row, col, 0, 0 );
                    }
                    else{
                        //outputPicture[row][col]=picture[row][col];
                        outputPicture.getRaster().setSample(row, col, 0, picture[row][col] );
                    }
                }
                //Angle 45-> NordVäst & SydÖst.
                else if (direction==45){
                    if(picture[row][col] < picture[row-1][col-1] || picture[row][col] < picture[row+1][col+1]){
                        outputPicture.getRaster().setSample(row, col, 0, 0 );
                    }
                    else{
                        outputPicture.getRaster().setSample(row, col, 0, picture[row][col] );
                    }
                }
                //Angle = 90, dvs Väst och Öst.
                else if (direction==90){
                    if(picture[row][col] < picture[row][col-1] || picture[row][col] < picture[row][col+1]){
                        outputPicture.getRaster().setSample(row, col, 0, 0 );
                    }
                    else{
                        outputPicture.getRaster().setSample(row, col, 0, picture[row][col] );
                    }
                }
                //Angle= 135 -> NordÖst och SydVäst.
                else if (direction==135){
                    if(picture[row][col] < picture[row-1][col+1] || picture[row][col] < picture[row+1][col-1]){
                        outputPicture.getRaster().setSample(row, col, 0, 0 );
                    }
                    else{
                        outputPicture.getRaster().setSample(row, col, 0, picture[row][col] );
                    }
                }

            }
        }
    }
    //Tar emot högt och lågt tröskelvärde.
    public WritableRaster hysteresis(WritableRaster raster){
        int lowThreshold,highTreshold;

        lowThreshold=Integer.parseInt(JOptionPane.showInputDialog("Mata in ett tal mellan 0-255 för Low Threshold."));
        highTreshold=Integer.parseInt(JOptionPane.showInputDialog("Mata in ett tal mellan 0-255 för High Threshold som" +
                " är större än Low Threshold:"+lowThreshold));
        //För varje pixel i rasteret.
        for(int row=0; row<raster.getHeight(); row++){
            for (int col=0; col<raster.getWidth(); col++){
                //If pixel is higher than the high threshold
                if( raster.getSample(col,row,0)>=highTreshold){
                    //Do nothing, we're gonna keep it.
                    //Maybe maximize the value?
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
