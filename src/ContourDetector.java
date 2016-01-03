import java.awt.image.BufferedImage;

/**
 * Created by Andreas on 2015-12-10.
 */
public class ContourDetector {
    private BufferedImage bImg;
    public ContourDetector(BufferedImage bImg){
        this.bImg=bImg;
    }

    public void convolution(){
        //bImg....
        /* public Flipper(BufferedImage img){
       //Store height and width
      int width  = img.getWidth();
      int height = img.getHeight();
       //Create a new Image with same size.TYPE_BYTE GRAY??? Färg/Gråskala?
      this.flip = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);//BufferedImage.TYPE_INT_RGB);

       //Skapa raster?
      WritableRaster imgraster  = img.getRaster();
      WritableRaster flipraster = flip.getRaster();

       //För varje position i bilden, ge ett annat värde i kopian?
       //0=Red,1=Green,2=Blue
      for (int row=0; row<width; row++)
          for (int col=0; col<height; col++) {
              int value = imgraster.getSample(row,col,0);
              flipraster.setSample(row,col,0, 255-value );
          }
   }
   */
    }

}
