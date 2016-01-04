import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by Andreas on 2015-12-10.
 */
public class CannysMethod {
    //Inkl Förtunning och Hysteresis.

    public WritableRaster edgeThinning(WritableRaster raster){
       // ta emot en bild med gradientmagnituder?
        // för var pixel i bilden( Array) så kontroller riktningen med en array/mask på 3x3 pixlar där center pixeln är
        //den som ska kontrolleras.
        //Bilden är 2D
        //OutputPicture är "utdataBilden", Picture är Arrayen med Gradientvärdena (?).
        //picture[0].length=width(columns), picture.length=height (rows)
        ArrayList<Double> degrees=new ArrayList<Double>();

        WritableRaster outRaster=raster;

        // För varje pixel i bilden (-1 och +1 för att undvika "kantpixlarna"?
                int[][] matrix=new int[3][3];
                for(int row=1;row<raster.getWidth()-1;row++) {
                    for (int col = 1; col < raster.getHeight() - 1; col++) {
                        // 3x3 matris med center punkt så 8 jämförelse positioner
                        //hämta samples från dessa positioner
                        matrix[0][0] = raster.getSample(row - 1, col - 1, 0);
                        matrix[0][1] = raster.getSample(row - 1, col, 0);
                        matrix[0][2] = raster.getSample(row - 1, col + 1, 0);
                        matrix[1][0] = raster.getSample(row, col - 1, 0);
                        matrix[1][2] = raster.getSample(row, col + 1, 0);
                        matrix[2][0] = raster.getSample(row + 1, col - 1, 0);
                        matrix[2][1] = raster.getSample(row + 1, col, 0);
                        matrix[2][2] = raster.getSample(row + 1, col + 1, 0);

                //beräkna gradient riktning? direction = 0,45,90,135?
                //vertikal
                double gy=(matrix[0][0]*-1)+(matrix[0][1]*-2)+(matrix[0][2]*-1)+(matrix[2][0])+(matrix[2][1]*2)+(matrix[2][2]);
                //Horisontell
                double gx=(matrix[0][0]*-1)+(matrix[0][2])+(matrix[1][0]*-2)+(matrix[1][2]*2)+(matrix[2][0]*-1)+(matrix[2][2]);
                //riktningen
                      //  double direction=Math.atan(gy/gx);
                        Double direction=Math.atan2(gy,gx);
                        direction=Math.toDegrees(direction);
                        degrees.add(direction);



                //Beroende på riktningen kontrollera om aktuell pixel ska användas.
                //Värden mellan -180 och +180.
                //Angle=0 -> Nord & Syd
                        //Avgör vilket gradantal som ligger närmst direction.

                //Om vinkeln är negativ gör den till motsvarande "positiv" vinkel. t.ex -45 == 135.
                if(direction<0){
                    //T.ex direction = 180+ (-45 (direction värdet)); =135, samma riktning.
                    // bara att det går "ner-> upp" ist för "upp->ner".
                    direction=180+direction;
                }
                //Avrunda direction till 0,45,90,135 eller 180.
                int closestAngleDifference=200;    //Random nummer. ska vara större än eventuell grad skillnad till närmsta vinkel.
                double closestDirection=0;
                        int[] angles= {0,45,90,135,180};
                        for (int i=0;i<angles.length;i++){
                            //beräknad gradskillnaden mellan vinkel och riktning.
                            int difference=0;
                            difference=angles[i]-direction.intValue();
                            if (difference<0){
                                //Ex: difference= -5. -5--5*2 =5. positiv skillnad som är lättare att jämföra.
                                difference=difference-difference*2;
                            }
                            //Är den nya skillnaden mindre än den sparade? Spara närmsta värdet.
                            closestAngleDifference=Math.min(closestAngleDifference,difference);
                            //Om den senaste beräkningen var närmst en vinkel-> spara det som närmsta riktningen.
                          if (closestAngleDifference==difference){
                              closestDirection=angles[i];
                          }
                        }
                        direction=closestDirection;
                //Avgör vad som ska göras med pixeln.
                if(direction==0 || direction==180){
                    if(raster.getSample(row,col,0) < raster.getSample(row-1,col,0) || raster.getSample(row,col,0) < raster.getSample(row+1,col,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
                //Angle 45-> NordVäst & SydÖst.
                else if (direction==45){
                    if(raster.getSample(row,col,0) < raster.getSample(row-1,col-1,0) || raster.getSample(row,col,0) < raster.getSample(row+1,col+1,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
                //Angle = 90, dvs Väst och Öst.
                else if (direction==90){
                    if(raster.getSample(row,col,0) < raster.getSample(row,col-1,0) || raster.getSample(row,col,0) < raster.getSample(row,col+1,0)){
                        outRaster.setSample(row, col, 0, 0 );
                    }
                    else{
                        outRaster.setSample(row, col, 0, raster.getSample(row,col,0) );
                    }
                }
                //Angle= 135 -> NordÖst och SydVäst.
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

    //KLAR!
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
