package SplitAndMerge;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Recieve Raster/input picture in grayscale.
 * Performs image segmentation with split&merge algorithm.
 * Created by Andreas Andersson & David Isberg on 2015-12-10.
 */
public class SplitAndMergeAlgorithm {

    private ArrayList<Region> regionList =new ArrayList<Region>();  //List with regions to test for possible split.
    private ArrayList<FinishedRegion> finRegionList=new ArrayList<FinishedRegion>(); //List with finished regions to merge.

    /**
     * Constructor with Writableraster as input "picture" in gray scale.
     * Returns the merge image as Raster.
     * @param inputRaster- image as raster.
     * @return WritableRaster
     */
    public WritableRaster SplitAndMergeAlgorithm(WritableRaster inputRaster){
        //Let the user pick a Threshold for the region values.
        int threshold=Integer.parseInt(JOptionPane.showInputDialog(null,"Mata in ett tröskelvärde för regionsdelning. 0-255 (Högre=färre regioner)" ));

        //Convert the raster with color values to an array.
        int [][] imagevalues =new int [inputRaster.getHeight()][inputRaster.getWidth()];
        for (int row=0;row<imagevalues.length;row++){
            for (int col=0;col<imagevalues[row].length;col++){
                imagevalues[row][col]=inputRaster.getSample(col,row,0);
            }
        }
        //Add it as the first Region to the regionlist.
        regionList.add(new Region(imagevalues,0,0));

       //While there is Regions to test.
       while(regionList.size()>0){
           int average=0;
            //Get the array,total number of pixels and calculate Average value.
            int [][] workingRegion=regionList.get(0).getArray();
            int totalNbrOfPixels = workingRegion.length * workingRegion[0].length;
            for (int row = 0; row < workingRegion.length; row++) {
                for (int col = 0; col < workingRegion[row].length; col++) {
                    average += workingRegion[row][col];
                }
            }
            average = average / totalNbrOfPixels;
            int highThres, lowThres;
            //Set threshold in comparison to Average value.
            highThres = average + threshold;
            lowThres = average - threshold;

            //Is this Region within the Average value?
            boolean withinThres=true;
            for (int row = 0; row < workingRegion.length; row++) {
                for (int col = 0; col < workingRegion[0].length; col++) {
                    int value=workingRegion[row][col];
                    if (value >= lowThres && value <= highThres) {
                    //The pixel is true compared to the Regions average value. Don't do anything.
                    } else {
                        withinThres=false;
                        //Break loop->
                        row=workingRegion.length;
                        col=workingRegion[0].length;
                    }
                }
            }
           //If the region is okay-> save the region as "ready to merge".
           if(withinThres){
               finRegionList.add(new FinishedRegion(workingRegion[0].length, workingRegion.length, average,
                       regionList.get(0).getPosX(),regionList.get(0).getPosY() ));
           }
           //Else-> Split it.
           else{
               split(regionList.get(0));
           }
           //We're done with this region-> Remove it.
           regionList.remove(0);
        }
        //Finally->Merge and return the new raster
        return merge(inputRaster.getWidth(), inputRaster.getHeight());
    }

    /**
     * Splits a Region and put the new Regions in the regionlist.
     * @param region- the region to split.
     */
    public void split(Region region) {
        int[][] regionArray = region.getArray();
        //[0,0] [0,1] =width values , [1,0] [1,1] height values
        int[][] newRegionSizes = calculateRegionSizes(regionArray);
        int width1, width2, height1, height2;
        //Needs two different values since the split could resolve in "half pixels".
        width1 = newRegionSizes[0][0];
        width2 = newRegionSizes[0][1];
        height1 = newRegionSizes[1][0];
        height2 = newRegionSizes[1][1];

        //Create the 4 new regions and put them in regionlist. If it isn't possible to create 4 new regions-
        //put the remaining ones in the finished list.
        if (width1 > 0 && width2 > 0 && height1 > 0 && height2 > 0) {

            int[][] newRegion1 = new int[height1][width1];
            int[][] newRegion2 = new int[height1][width2];
            int[][] newRegion3 = new int[height2][width1];
            int[][] newRegion4 = new int[height2][width2];

            //Fill the new regions with pixel values.
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
            //Finally add the new regions to the regionlist.
            regionList.add(new Region(newRegion1, region.getPosX() + 0, region.getPosY() + 0));
            regionList.add(new Region(newRegion2, region.getPosX() + width1, region.getPosY() + 0));
            regionList.add(new Region(newRegion3, region.getPosX() + 0, region.getPosY() + height1));
            regionList.add(new Region(newRegion4, region.getPosX() + width1, region.getPosY() + height1));
        }
        //If it isn't possible to split-> add the remaining pixels to the finished list as 1 pixel regions.
        else {
            for(int row=0;row<regionArray.length;row++){
                for (int col=0;col<regionArray[row].length;col++){
                    finRegionList.add(new FinishedRegion(1,1,regionArray[row][col]
                            ,region.getPosX()+col,region.getPosY()+row));
                }
            }
        }
    }

    /**
     * Calculates the size of the new regions widths and heights.
     * @param regionArray- array to calculate with.
     * @return int[][]
     */
    public int[][] calculateRegionSizes(int[][] regionArray){
         int[][] regionSize=new int[2][2];
        //[0,0] [0,1] widths , [1,0] [1,1] heights

        //Split the region with 2 and give width & height it's values. If the sides aren't even-> let on width/height be
        //1 pixel bigger than the other.

        //Width
        if (regionArray[0].length % 2 == 0) {
            regionSize[0][0] = regionArray[0].length / 2;
            regionSize[0][1] = regionArray[0].length / 2;
        } else {
            //If uneven pixel number, let one region have one pixel more than the other.2
            regionSize[0][0] = regionArray[0].length / 2;
            regionSize[0][1]= (regionArray[0].length / 2) + 1;
        }

        //Height
        if (regionArray.length % 2 == 0) {
            regionSize[1][0] = regionArray.length / 2;
            regionSize[1][1] = regionArray.length / 2;
        } else {
            regionSize[1][0]= regionArray.length / 2;
            regionSize[1][1] = (regionArray.length / 2) + 1;
        }
        return regionSize;
    }

    /**
     * Merge the new output image with the Finished Regions with average values.
     * @param width - width of image
     * @param height- height of image
     * @return WritableRaster
     */
    public WritableRaster merge (int width,int height){
        BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster outputRaster=img.getRaster();
        System.out.println("Antal färdiga regioner:"+finRegionList.size());

        //Merge
        for (int i=0;i<finRegionList.size();i++) {
            FinishedRegion temp=finRegionList.get(i);
            int x,y,colorVal;
            x=temp.getPosX();
            y=temp.getPosY();
            colorVal=temp.getColorValue();
            for (int row = 0; row < temp.getHeight(); row++) {
                for (int col = 0; col < temp.getWidth(); col++) {
                    outputRaster.setSample(col+x,row+y,0,colorVal);
                }
            }
        }
        return outputRaster;
    }

}
