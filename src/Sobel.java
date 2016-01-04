import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.*;      
import javax.imageio.ImageIO;   
import javax.swing.JFileChooser; 
   
public class Sobel {   
   
	private BufferedImage inputImg,outputImg;
	private int[][] matrix=new int[3][3];
	private WritableRaster inPutRaster, outPutRaster;
	private String file;
	
	public Sobel(){    
	    try {
        JFileChooser jc= new JFileChooser();
        jc.showOpenDialog(null);
        file= jc.getSelectedFile().getPath();
		this.inputImg = ImageIO.read(new File(file));
		inputImg = convertToGray(inputImg);
		inPutRaster = inputImg.getRaster();
		
		this.outputImg = new BufferedImage(inputImg.getWidth(),inputImg.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
	    outPutRaster = outputImg.getRaster();
	    } catch (IOException ex){
	    	System.out.println("Image width:height="+inputImg.getWidth()+":"+inputImg.getHeight());}
	}
	
	public BufferedImage convertToGray(BufferedImage image) {
		BufferedImage grayscale = new BufferedImage(image.getWidth(),image.getHeight(),
		BufferedImage.TYPE_BYTE_GRAY);
		ColorConvertOp op = new ColorConvertOp(
		image.getColorModel().getColorSpace(),
		grayscale.getColorModel().getColorSpace(),null);
		op.filter(image,grayscale);
		return grayscale;
	}
	   
	public void convolution(){
        for(int i=1;i<inputImg.getWidth()-1;i++){
            for(int j=1;j<inputImg.getHeight()-1;j++){
				// 3x3 matris med center punkt så 8 jämförelse positioner
				//hämta samples från dessa positioner
                matrix[0][0]= inPutRaster.getSample(i-1,j-1, 0);
                matrix[0][1]= inPutRaster.getSample(i-1,j,0);
                matrix[0][2]= inPutRaster.getSample(i-1,j+1,0);
                matrix[1][0]= inPutRaster.getSample(i,j-1,0);
                matrix[1][2]= inPutRaster.getSample(i,j+1,0);
                matrix[2][0]= inPutRaster.getSample(i+1,j-1,0);
                matrix[2][1]= inPutRaster.getSample(i+1,j,0);
                matrix[2][2]= inPutRaster.getSample(i+1,j+1,0);
                
                int edge= (int) convolutionKernels(matrix);	//för att räkna ut gradientmagnituden
                outPutRaster.setSample(i,j, 0, edge);	//sätt pixeln (center) till resultatet.
            }
        }


        try {
			ImageIO.write(outputImg, "PNG", new File(file+"_sobel.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		CannysMethod cm=new CannysMethod();
		outPutRaster=cm.edgeThinning(outPutRaster);
		try {
			ImageIO.write(outputImg, "PNG", new File(file+"_thinning.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		outPutRaster=cm.hysteresis(outPutRaster);

		try {
			ImageIO.write(outputImg, "PNG", new File(file+"_hysteresis.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public double convolutionKernels(int[][]matrix){
		//sobel kärnor med förbestämt värde
		//Vertikal
		int gy=(matrix[0][0]*-1)+(matrix[0][1]*-2)+(matrix[0][2]*-1)+(matrix[2][0])+(matrix[2][1]*2)+(matrix[2][2]);
		//Horisontell
		int gx=(matrix[0][0]*-1)+(matrix[0][2])+(matrix[1][0]*-2)+(matrix[1][2]*2)+(matrix[2][0]*-1)+(matrix[2][2]);

	    return Math.sqrt(Math.pow(gy,2)+Math.pow(gx,2));	//Gradient magnituden för pixeln.
	}
	
	public static void main(String[] args) {
		Sobel sobel = new Sobel(); 
		sobel.convolution();
	}
}


