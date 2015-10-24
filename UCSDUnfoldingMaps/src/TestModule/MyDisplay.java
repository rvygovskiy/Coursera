package TestModule;

import processing.core.PApplet;
import processing.core.PImage;

public class MyDisplay extends PApplet
{
	private String FileStrShort = "..\\data\\kombain-480.jpg";
	private PImage backgroundImg;
	
	public void setup()
	{
		size(400,400);
		backgroundImg = loadImage(FileStrShort);
		background(backgroundImg);
	}
	public void draw()
	{
		
		
	}
}
