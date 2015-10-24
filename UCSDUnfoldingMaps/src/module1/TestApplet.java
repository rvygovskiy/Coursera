package module1;

import processing.core.*;

public class TestApplet extends PApplet
{
    private String FileStrFull = "D:\\Work\\Coursera\\WorkSpace\\UCSDUnfoldingMaps\\data\\kolos.jpg";
    private String FileStrShoert = "..\\data\\kolos.jpg";
    //private String FileStr = "kombain-480.jpg";
    //url = "E:\\Work\\Coursera\\WorkSpace\\UCSDUnfoldingMaps\\data\\Penguins.jpg";
    private PImage backgroundImg;
   
    public void setup()
    {
        size(200, 200);
        //backgroundImg = loadImage(url, "jpg");
        backgroundImg = loadImage(FileStrShoert);
        
    }
   
    public void draw()
    {
    	 background(0);
    	 backgroundImg.resize(0, height);
    	 fill(255,209,0); 
    	 image(backgroundImg, 0, 0);
    	  ellipse(width/4, height/5, width/6, height/6);
    }
}