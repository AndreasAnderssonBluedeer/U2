import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * given klass för bild-inläsning och bildbehandlingar.
 */
public class Flipper {

   private BufferedImage flip;

   public BufferedImage getFlippedImage(){
      return this.flip;
   }
    //Constructor recieves a bufferedImage.
   public Flipper(BufferedImage img){
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
   
   public static void main(String[] args) {
      // String file  = args[0];//? huh?
       JFileChooser jc= new JFileChooser();
       jc.showOpenDialog(null);
       String file= jc.getSelectedFile().getPath();
       try {
           //Skapa en bufferedImage som läser in filen
           BufferedImage img  = ImageIO.read(new File(file));
           //Kör klassen med bilden.
           Flipper flipper = new Flipper(img);
           //Skriv ut den "nya bilden/kopian". som PNG.
           ImageIO.write(flipper.getFlippedImage(), "PNG", new File(file+"_flip6"+".png"));
       } catch (IOException e) {
           System.out.println("Failed processing!\n"+e.toString());
       }

   }
}


