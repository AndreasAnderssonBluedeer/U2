import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * given klass för bild-inläsning och bildbehandlingar.
 */
public class Flip {

   private BufferedImage flip;

   public BufferedImage getFlippedImage(){
      return this.flip;
   }
    //Constructor recieves a bufferedImage.
   public Flip(BufferedImage img){
       //Store height and width
      int width  = img.getWidth();
      int height = img.getHeight();
       //Create a new Image with same size.TYPE_BYTE GRAY??? Färg/Gråskala?
      this.flip = new BufferedImage(width, height, /*BufferedImage.TYPE_INT_RGB);*/ BufferedImage.TYPE_BYTE_GRAY);

       //Skapa raster?
      WritableRaster imgraster  = img.getRaster();
      WritableRaster flipraster = flip.getRaster();

       //För varje position i bilden, ge ett annat värde i kopian?
      for (int i=0; i<width; i++)
          for (int j=0; j<height; j++) {
              int value = imgraster.getSample(i,j,0);
              flipraster.setSample(i,j,0, 255-value );
          }
   }
   
   public static void main(String[] args) {
      // String file  = args[0];//? huh?
       JFileChooser jc= new JFileChooser();
       jc.showOpenDialog(null);
       String file= jc.getSelectedFile().getPath();
       try {
           //Skapa en bufferedImage som läser in filen
           BufferedImage img  = ImageIO.read(new File(file));
           //Kör klassen med bilden.
          // Flipper flipper = new Flipper(img);
           //Skriv ut den "nya bilden/kopian". som PNG.
       //    ImageIO.write(flipper.getFlippedImage(), "PNG", new File("flip_"+file+".png"));
       } catch (IOException e) {
           System.out.println("Failed processing!\n"+e.toString());
       }

   }
}


