package SplitAndMerge;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Recieve Raster/input picture in grayscale. OBS FÖRÄNDRAR REGION KLASSERNA, ANPASSA!
 * Created by Andreas on 2015-12-10.
 */
public class SplitAndMergeAlgorithm {

}
    private ArrayList<Region> regionList =new ArrayList<Region>();
    private ArrayList<FinishedRegion> finRegionList=new ArrayList<FinishedRegion>();

    public WritableRaster SplitAndMergeAlgorithm(WritableRaster inputRaster){
        BufferedImage temp=new BufferedImage(inputRaster.getWidth(),inputRaster.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster outputRaster=temp.getRaster();

        //Ange ett tröskelvärde som ska bli samma region
        int threshold=Integer.parseInt(JOptionPane.showInputDialog(null,"Mata in ett tröskelvärde för regionsdelning. 0-255 (Högre=färre regioner)" ));

        //Om ett false ,dela i fyra nya.
        //Lägga alla värden från bilden i en array
        int [][] imagevalues =new int [inputRaster.getHeight()][inputRaster.getWidth()];
        for (int row=0;row<imagevalues.length;row++){
            for (int col=0;col<imagevalues[row].length;col++){
                imagevalues[row][col]=inputRaster.getSample(row,col,0);
            }
        }
        regionList.add(new Region(imagevalues,0,0));    //int[][] och start värden X,Y
        int median=0;//init
        //en arraylist som fylls på så fort arrayen splittas?
        for (int i=0;i< regionList.size();i++) {
            //Ta reda på antalet pixlar i arrayen för att beräkna median
            int totaltNbrofPixels = regionList.get(i).length * regionList.get(i)[0].length;
            //Kontrollera position i i regionList för att se om regionen är ok- ta bort efter kontroll?
            for (int row = 0; row < regionList.get(i).length; row++) {
                for (int col = 0; col < regionList.get(i)[row].length; col++) {
                    median += regionList.get(i)[row][col];
                }
            }
            //Totala pixelvärdet är beräknat, ta ut medianen.
            median = median / totaltNbrofPixels;
            int highThres, lowThres;
            //Bör threshold delas på 2? smak fråga..
            highThres = median + threshold;
            lowThres = median - threshold;
            //ta reda på om hela regionen ligger inom tröskelvärdet.
            for (int row = 0; row < regionList.get(i).length; row++) {
                for (int col = 0; col < regionList.get(i)[row].length; col++) {
                    //Om aktuell pixel är inom tröskelvärdet.-> region = true.. spara region!
                    if (regionList.get(i)[row][col] >= lowThres || regionList.get(i)[row][col] <= highThres) {
                        //spara region till listan ,width,height,colorvalue,startposition.
                        finRegionList.add(new FinishedRegion(regionList.get(i)[row].length, regionList.get(i).length, median, ));
                    } else {
                        //Splitta!
                    }
                }
            }


        }
        return outputRaster;
    }
    public int[][] split(int[][] array){

        return null;
    }

}
