package module4;

import processing.core.PGraphics;

public class DrawCentratedFigure
{ 
	
	
	public static void ellipse(PGraphics pg, float x, float y, float r)
	{
		pg.ellipse(x,y,2*r,2*r);
	}
	public static void triangle(PGraphics pg, float x, float y, float r)
	{
		pg.triangle((float)(x-0.5*Math.sqrt(3)*r),(float)(y+0.5*r),x,y-r,(float)(x+0.5*Math.sqrt(3)*r),(float)(y+0.5*r));
	}
	public static  void rect(PGraphics pg, float x, float y, float r)
	{
		pg.rect((float)(x-r/Math.sqrt(2)),(float)(y-r/Math.sqrt(2)),(float)(Math.sqrt(2)*r),(float)(Math.sqrt(2)*r));
	}
	public static  void xCross(PGraphics pg, float x, float y, float r)
	{
		pg.line((float)(x-r/Math.sqrt(2)),(float)(y-r/Math.sqrt(2)),(float)(x+r/Math.sqrt(2)),(float)(y+r/Math.sqrt(2)));
		pg.line((float)(x-r/Math.sqrt(2)),(float)(y+r/Math.sqrt(2)),(float)(x+r/Math.sqrt(2)),(float)(y-r/Math.sqrt(2)));	
	}

}
