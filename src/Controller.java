import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andreas on 2016-01-02.
 */
public class Controller {
    private BufferedImage inputImg; //outputImg
    private WritableRaster inPutRaster,outputRaster;

    public Controller() {

        int choice=Integer.parseInt(JOptionPane.showInputDialog("1- Konturdetektering\n2- Bildsegmentering"));

        switch (choice){
            case 1:
                try {   //inläsning bild
                    JFileChooser jc = new JFileChooser();
                    jc.showDialog(null,"Öppna");
                    String file = jc.getSelectedFile().getPath();
                    this.inputImg = ImageIO.read(new File(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inPutRaster = convertToGray(inputImg);
                new ImagePanel(inPutRaster,"Step 1-To Gray");

                Gauss g = new Gauss();
                outputRaster = g.filter(inPutRaster);
                new ImagePanel(outputRaster,"Step 2-Gaussian-Filter");

                Sobel s = new Sobel();
                outputRaster = s.convolution(inPutRaster);
                new ImagePanel(outputRaster,"Step 3-Sobel");

                CannysMethod cm = new CannysMethod();
                outputRaster = cm.edgeThinning(outputRaster);
                new ImagePanel(outputRaster,"Step 4-Edge Thinning");

                outputRaster = cm.hysteresis(outputRaster);
                new ImagePanel(outputRaster,"Step 5-Hysteresis");

            break;
            case 2:
            //Segmentering....
            break;
            }
        }


    public WritableRaster convertToGray(BufferedImage image) {
        BufferedImage grayscale = new BufferedImage(image.getWidth(),image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                grayscale.getColorModel().getColorSpace(),null);
        op.filter(image,grayscale);
        return grayscale.getRaster();
    }
    private class ImagePanel extends JPanel {
        private final BufferedImage image;

        public ImagePanel(WritableRaster raster,String title) {
            image=new BufferedImage(raster.getWidth(),raster.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
            image.setData(raster);
            JFrame frame = new JFrame(title);
            frame.add(this);
            frame.pack();
            frame.setSize(512, 512);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(512, 512);
        }

        public void paintComponent(Graphics g) {
            Rectangle rect = this.getBounds();
            if (image != null) {
                g.drawImage(image, 0, 0, rect.width, rect.height, this);
            }
        }
    }

    public static void main(String[] args) {
        Controller controller= new Controller();

    }
}
