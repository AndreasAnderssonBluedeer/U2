import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Gauss {

	public WritableRaster filter(WritableRaster inPutRaster){
		WritableRaster outPutRaster=inPutRaster;
		int[][] matrix = new int[3][3];
		for(int row=1;row<inPutRaster.getWidth()-1;row++){
			for(int col=1;col<inPutRaster.getHeight()-1;col++){           	
				matrix[0][0]= inPutRaster.getSample(row-1,col-1, 0);
				matrix[0][1]= inPutRaster.getSample(row-1,col,0);
				matrix[0][2]= inPutRaster.getSample(row-1,col+1,0);
				matrix[1][0]= inPutRaster.getSample(row,col-1,0);
				matrix[1][1]= inPutRaster.getSample(row,col,0);
				matrix[1][2]= inPutRaster.getSample(row,col+1,0);
				matrix[2][0]= inPutRaster.getSample(row+1,col-1,0);
				matrix[2][1]= inPutRaster.getSample(row+1,col,0);
            	matrix[2][2]= inPutRaster.getSample(row+1,col+1,0);
            
            	int blur = (int) gaussK(matrix);
            	outPutRaster.setSample(row,col, 0, blur);
			}
		}
		return outPutRaster;
	}

	public double gaussK(int[][]matrix){
		double gy=(matrix[0][0]*1)+(matrix[0][1]*2)+(matrix[0][2]*1)+
				(matrix[1][0]*2)+(matrix[1][1]*4)+(matrix[1][2]*2)+
				(matrix[2][0]*1)+(matrix[2][1]*2)+(matrix[2][2]*1);
		gy = 0.0625*gy;
		return gy;
	}
}


