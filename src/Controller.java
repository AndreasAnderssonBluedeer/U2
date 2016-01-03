import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andreas on 2016-01-02.
 */
public class Controller {

    public Controller(){
        JFileChooser jc= new JFileChooser();
        jc.showOpenDialog(null);
        String file= jc.getSelectedFile().getPath();
        try {
            //Skapa en bufferedImage som läser in filen
            BufferedImage img  = ImageIO.read(new File(file));
            //Kör klassen med bilden.
            ContourDetector cd=new ContourDetector(img);
            cd.convolution();
            Flipper flipper = new Flipper(img);

            //Skriv ut den "nya bilden/kopian". som PNG.
           ImageIO.write(flipper.getFlippedImage(), "PNG", new File(file+"_flip"+".png"));
            //flipper.getFlippedImage() hämta den förändrade bilden.
            //*** FLIPPA FÄRGER OCH GJORT TILL GRÅSKALA MED BRUS KVAR.***
        } catch (IOException e) {
            System.out.println("Failed processing!\n"+e.toString());
        }

        //Ta bort brus-> Gaussian Filter
    }
    //Läs in bild, skicka ut bild och fixa kommunikation mellan klasserna.

    //Hämta bild
    //Skicka till konturdektektering-> Faltning/Gradient magnitud med Sobelkärnor.
    //Canny's algoritm för förfining av kontur
    //Välj Segmentering 1 eller 2
    //Printa färdigt resultat.
    // OBS+ PRINT I DE OLIKA STEGEN.

    public static void main(String[] args) {
        Controller controller= new Controller();

    }
}
