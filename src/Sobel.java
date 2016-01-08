import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.*;      
import javax.imageio.ImageIO;   
import javax.swing.JFileChooser;
/*
 This class performs the convolution using a sobel core.
 Created by Andreas Andersson & David Isberg on 2015-12-10.
*/
public class Sobel {   

	public WritableRaster convolution(WritableRaster inPutRaster){
		BufferedImage temp=new BufferedImage(inPutRaster.getWidth(),inPutRaster.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster outPutRaster=temp.getRaster();
		int[][] matrix=new int[3][3];

		for(int col=1;col<inPutRaster.getWidth()-1;col++){
			for(int row=1;row<inPutRaster.getHeight()-1;row++){
				matrix[0][0]= inPutRaster.getSample(col-1,row-1, 0);
				matrix[0][1]= inPutRaster.getSample(col-1,row,0);
				matrix[0][2]= inPutRaster.getSample(col-1,row+1,0);
				matrix[1][0]= inPutRaster.getSample(col,row-1,0);
				matrix[1][2]= inPutRaster.getSample(col,row+1,0);
				matrix[2][0]= inPutRaster.getSample(col+1,row-1,0);
				matrix[2][1]= inPutRaster.getSample(col+1,row,0);
				matrix[2][2]= inPutRaster.getSample(col+1,row+1,0);

				int edge = (int) convolutionKernels(matrix);
				outPutRaster.setSample(col,row, 0, edge);
			}
        }
		return outPutRaster;
	}
	public double convolutionKernels(int[][]matrix){
		int gy=(matrix[0][0]*-1)+(matrix[0][1]*-2)+(matrix[0][2]*-1)+(matrix[2][0])+(matrix[2][1]*2)+(matrix[2][2]);
		int gx=(matrix[0][0]*-1)+(matrix[0][2])+(matrix[1][0]*-2)+(matrix[1][2]*2)+(matrix[2][0]*-1)+(matrix[2][2]);
		return Math.sqrt(Math.pow(gy,2)+Math.pow(gx,2));
	}
	
	public static void main(String[] args) {
		Sobel sobel = new Sobel(); 
		//sobel.convolution();
	}
}


