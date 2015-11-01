package module4;

import processing.core.PApplet;

public class MyApplet extends PApplet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void ellipse(float x, float y, float r)
	{
		ellipse(x,y,2*r,2*r);
	}
	public  void triangle(float x, float y, float r)
	{
		triangle((float)(x-0.5*Math.sqrt(3)*r),(float)(y+0.5*r),x,y-r,(float)(x+0.5*Math.sqrt(3)*r),(float)(y+0.5*r));
	}
	public  void rect(float x, float y, float r)
	{
		rect((float)(x-r/Math.sqrt(2)),(float)(y-r/Math.sqrt(2)),(float)(Math.sqrt(2)*r),(float)(Math.sqrt(2)*r));
	}

}
