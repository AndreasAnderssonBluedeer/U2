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

    private ArrayList<Region> regionList =new ArrayList<Region>();
    private ArrayList<FinishedRegion> finRegionList=new ArrayList<FinishedRegion>();

    public WritableRaster SplitAndMergeAlgorithm(WritableRaster inputRaster){
        //Ange ett tröskelvärde som ska bli samma region
        int threshold=Integer.parseInt(JOptionPane.showInputDialog(null,"Mata in ett tröskelvärde för regionsdelning. 0-255 (Högre=färre regioner)" ));

        //Om ett false ,dela i fyra nya.
        //Lägga alla värden från bilden i en array
        int [][] imagevalues =new int [inputRaster.getHeight()][inputRaster.getWidth()];
        for (int row=0;row<imagevalues.length;row++){
            for (int col=0;col<imagevalues[row].length;col++){
                imagevalues[row][col]=inputRaster.getSample(col,row,0);
            }
        }
        regionList.add(new Region(imagevalues,0,0));    //int[][] och start värden X,Y
        int median=0;//init
        //en arraylist som fylls på så fort arrayen splittas?
       while(regionList.size()>0){
            //Hämta ut arrayen att arbeta på:
            int [][] workingRegion=regionList.get(0).getArray();
            //Ta reda på antalet pixlar i arrayen för att beräkna median
            int totaltNbrofPixels = workingRegion.length * workingRegion[0].length;
            //Kontrollera position i i regionList för att se om regionen är ok- ta bort efter kontroll?
            for (int row = 0; row < workingRegion.length; row++) {
                for (int col = 0; col < workingRegion[row].length; col++) {
                    median += workingRegion[row][col];
                }
            }
            //Totala pixelvärdet är beräknat, ta ut medianen.
            median = median / totaltNbrofPixels;
            int highThres, lowThres;
            //Bör threshold delas på 2? smak fråga..
            highThres = median + threshold;
            lowThres = median - threshold;
            //ta reda på om hela regionen ligger inom tröskelvärdet.
            boolean withinThres=true;
            for (int row = 0; row < workingRegion.length; row++) {
                for (int col = 0; col < workingRegion[row].length; col++) {
                    //Om aktuell pixel är inom tröskelvärdet.-> region = true.. spara region!
                    if (workingRegion[row][col] >= lowThres && workingRegion[row][col] <= highThres) {
                    //gör inget
                    } else {
                        withinThres=false;
                    }
                }
            }
           if(withinThres){
               finRegionList.add(new FinishedRegion(workingRegion[0].length, workingRegion.length, median,
                       regionList.get(0).getPosX(),regionList.get(0).getPosY() ));
           }
           else{
               split(regionList.get(0));   //Splittar regionen och lägger till de 4a nya regionerna i listan.
           }
           regionList.remove(0);   //färdig med denna Region, ta bort den.
        }
        //Merge and return the new raster
        return merge(inputRaster.getWidth(), inputRaster.getHeight());
    }
    //Param Region för att kontroller positionsvärden.
    public void split(Region region) {
        //Splitta regionen till fyra nya "lika" delar.
        //Lagra arrayen då den kommer hanteras mycket.
        int[][] regionArray = region.getArray();
        //[0,0] [0,1] är width , [1,0] [1,1] är height
        int[][] newRegionSizes = calculateRegionSizes(regionArray);
        int width1, width2, height1, height2;

        width1 = newRegionSizes[0][0];
        width2 = newRegionSizes[0][1];
        height1 = newRegionSizes[1][0];
        height2 = newRegionSizes[1][1];

        //Skapa de fyra nya Regionerna:
        //Regionerna kanske inte är kvadratiska utan rektangulära med olika höjder och bredder så välj en gemensam höjd
        //med varsin bredd.
        //Om det endast finns två pixlar att arbeta med låter vi dem vara.
        //behöver lägga till dem i finRegionListan.-
        if (width1 > 0 && width2 > 0 && height1 > 0 && height2 > 0) {

            int[][] newRegion1 = new int[height1][width1];
            int[][] newRegion2 = new int[height1][width2];
            int[][] newRegion3 = new int[height2][width1];
            int[][] newRegion4 = new int[height2][width2];

            //Fyll arrayerna med dess värden och ange startpositioner i totala bilden för x&y
            //Hämta värden från regionArray
            //  |R1|R2|
            //  |R3|R4| } tillsammans regionArray
            for (int row = 0; row < regionArray.length; row++) {
                for (int col = 0; col < regionArray[row].length; col++) {
                    //Region 2
                    if (row < height1 && col >= width1) {
                        newRegion2[row][col - width1] = regionArray[row][col];
                    }
                    //Region 3
                    else if (row >= height1 && col < width1) {
                        newRegion3[row - height1][col] = regionArray[row][col];
                    }
                    //Region 4
                    else if (row >= height1 && col >= width1) {
                        newRegion4[row - height1][col - width1] = regionArray[row][col];
                    }
                    //Region 1
                    else {
                        newRegion1[row][col] = regionArray[row][col];
                    }
                }
            }
            //Lägg till de fyra nya regionerna i regionslistan.
            regionList.add(new Region(newRegion1, region.getPosX() + 0, region.getPosY() + 0));
            regionList.add(new Region(newRegion2, region.getPosX() + width1, region.getPosY() + 0));
            regionList.add(new Region(newRegion3, region.getPosX() + 0, region.getPosY() + height1));
            regionList.add(new Region(newRegion4, region.getPosX() + width1, region.getPosY() + height1));
        }
        //Om det endast finns två pixlar i arrayen-> dvs den går inte att splitta.
        else {
            for(int row=0;row<regionArray.length;row++){
                for (int col=0;col<regionArray[row].length;col++){
                    finRegionList.add(new FinishedRegion(1,1,regionArray[row][col]
                            ,region.getPosX()+col,region.getPosY()+row));
                }
            }
        }
    }

    public int[][] calculateRegionSizes(int[][] regionArray){
         int[][] regionSize=new int[2][2];
        //[0,0] [0,1] är width , [1,0] [1,1] är height
        //Räkna ut regionsstorlekarna, dela på totalt antal pixlar med modolus.
        //Börja med bredden: (width) modolus 2 (bredden ska splittas på två) dvs bredden får exakt pixel antal/2
        if (regionArray[0].length % 2 == 0) {
            regionSize[0][0] = regionArray[0].length / 2;
            regionSize[0][1] = regionArray[0].length / 2;
        } else {
            //blir det ojämnt så gör en heltals division och låt region två få en pixel mer
            // än den andra då det inte går att splitta en pixel.
            regionSize[0][0] = regionArray[0].length / 2;
            regionSize[0][1]= (regionArray[0].length / 2) + 1;
        }

        //Återupprepa för height
        if (regionArray.length % 2 == 0) {
            regionSize[1][0] = regionArray.length / 2;
            regionSize[1][1] = regionArray.length / 2;
        } else {
            regionSize[1][0]= regionArray.length / 2;
            regionSize[1][1] = (regionArray.length / 2) + 1;
        }
        return regionSize;
    }

    public WritableRaster merge (int width,int height){
       // int[][] finalImgArray=new int[height][width];
        BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster outputRaster=img.getRaster();
        //Merga ihop regionerna från finRegionList.
        System.out.println("Antal färdiga regioner:"+finRegionList.size());
        for (int i=0;i<finRegionList.size();i++) {
            FinishedRegion temp=finRegionList.get(i);
            for (int row = 0; row < temp.getHeight(); row++) {
                for (int col = 0; col < temp.getWidth(); col++) {
                    outputRaster.setSample(col+temp.getPosX(),row+temp.getPosY(),0,temp.getColorValue());
                   // finalImgArray[row+temp.getPosY()][col+temp.getPosX()]=temp.getColorValue();
                }
            }
        }

        return outputRaster;
    }

}
