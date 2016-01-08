import SplitAndMerge.SplitAndMergeAlgorithm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Controller class to let the user choose function. Image Segmentation och Edge detection?
 * Created by Andreas Andersson & David Isberg on 2016-01-02.
 */
public class Controller {
    private BufferedImage inputImg; //outputImg
    private WritableRaster inPutRaster,outputRaster;

    /**
     * Let's the user choose what to do and do it.
     */
    public Controller() {

        int choice=Integer.parseInt(JOptionPane.showInputDialog("1- Konturdetektering\n" +
                "2- Bildsegmentering med Split&Merge"+
        "\n3- Bildsegmentering med segmentering2"));

        switch (choice){
            case 1: //Edge Detection with Canny's Method.
                //Read input image.
                try {
                    JFileChooser jc = new JFileChooser();
                    jc.showDialog(null,"Öppna");
                    String file = jc.getSelectedFile().getPath();
                    this.inputImg = ImageIO.read(new File(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Convert to gray, show result in JFrame.
                inPutRaster = convertToGray(inputImg);
                new ImagePanel(inPutRaster,"Step 1-To Gray");
                //Use Gaussian blur, show result in JFrame.
                Gauss g = new Gauss();
                outputRaster = g.filter(inPutRaster);
                new ImagePanel(outputRaster,"Step 2-Gaussian-Filter");
                //Use Sobel-core for convolution, show result in JFrame.
                Sobel s = new Sobel();
                outputRaster = s.convolution(inPutRaster);
                new ImagePanel(outputRaster,"Step 3-Sobel");
                //Thin the Edges with Non-Maximum Suppression, show result in JFrame.
                CannysMethod cm = new CannysMethod();
                outputRaster = cm.edgeThinning(outputRaster);
                new ImagePanel(outputRaster,"Step 4-Edge Thinning");
                //Perform Hysteresis, show result in JFrame.
                outputRaster = cm.hysteresis(outputRaster);
                new ImagePanel(outputRaster,"Step 5-Hysteresis");
            break;

            case 2://Image Segmentation- Split & Merge
                try {   //Read input image.
                    JFileChooser jc = new JFileChooser();
                    jc.showDialog(null,"Öppna");
                    String file = jc.getSelectedFile().getPath();
                    this.inputImg = ImageIO.read(new File(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Convert to gray, show result in JFrame.
                inPutRaster = convertToGray(inputImg);
                new ImagePanel(inPutRaster,"Step 1-To Gray");
                //Perform Image segmentation with Split&Merge, show result in JFrame.
                SplitAndMergeAlgorithm sm=new SplitAndMergeAlgorithm();
                outputRaster=sm.SplitAndMergeAlgorithm(inPutRaster);
                new ImagePanel(outputRaster,"Step 2-Split&Merge");
            break;

            case 3://Image Segmentation- V2
                //Read input image.
                try {
                    JFileChooser jc = new JFileChooser();
                    jc.showDialog(null,"Öppna");
                    String file = jc.getSelectedFile().getPath();
                    this.inputImg = ImageIO.read(new File(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Convert to gray, show result in JFrame.
                inPutRaster = convertToGray(inputImg);
                new ImagePanel(inPutRaster,"Step 1-To Gray");
                break;
            }
        }

    /**
     * Uses a Writableraster to convert an image to gray scale.
     * @param image-input image as raster
     * @return WritableRaster
     */
    public WritableRaster convertToGray(BufferedImage image) {
        BufferedImage grayscale = new BufferedImage(image.getWidth(),image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                grayscale.getColorModel().getColorSpace(),null);
        op.filter(image,grayscale);
        return grayscale.getRaster();
    }

    /**
     * Show's an image on a JPanel.
     * Used to show the different results and progress.
     */
    private class ImagePanel extends JPanel {
        private final BufferedImage image;

        /**
         * Recieves a raster to be used as an image and a title for the JFrame window.
         * @param raster
         * @param title
         */
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

        /**
         * Draw the image to the Panel.
         * @param g
         */
        public void paintComponent(Graphics g) {
            Rectangle rect = this.getBounds();
            if (image != null) {
                g.drawImage(image, 0, 0, rect.width, rect.height, this);
            }
        }
    }

    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        Controller controller= new Controller();

    }
}
