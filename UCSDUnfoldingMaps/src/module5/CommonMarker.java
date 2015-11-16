package module5;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author modified by Roman V.
 *
 */
public abstract class CommonMarker extends SimplePointMarker {

	// Records whether this marker has been clicked (most recently)
	protected boolean clicked = false;
	// Records count of objects which are within the threat circle
	// There are earthquakes for the city
	// and there are cities for the earthquake
	protected int numberConnetionObjets;
	// Records additional information of objects which are within the threat circle
	// There are earthquakes for the city
	// and there are cities for the earthquake
	protected String addInformation;
	
	public CommonMarker(Location location) {
		super(location);
	}
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	public float getRadius()
	{
		return super.radius;
	}
	
	// Getter method for clicked field
	public boolean getClicked() {
		return clicked;
	}
	
	// Setter method for clicked field
	public void setClicked(boolean state) {
		clicked = state;
	}
	// Getter method for numberConnetionObjets field
		public int getNumberConnetionObjets() {
			return numberConnetionObjets;
		}
		
	// Setter method for numberConnetionObjets field
		public void setNumberConnetionObjets(int count) {
			numberConnetionObjets = count;
		}
	// Getter method for addInformation field
		public String getAddInformation() {
					return addInformation;
				}
	// Setter method for addInformation field
		public void setAddInformation(String info) {
					addInformation = info;
				}
				
	// Common piece of drawing method for markers; 
	// Note that you should implement this by making calls 
	// drawMarker and showTitle, which are abstract methods 
	// implemented in subclasses
	public void draw(PGraphics pg, float x, float y) {
		// For starter code just drawMaker(...)
		if (!hidden) {
			drawMarker(pg, x, y);
		/*	if (selected) {
				showTitle(pg, x, y);  // You will implement this in the subclasses
			}
		*/
		}
	}
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
	public abstract String getTitleInfo();
	//output text information in box
	public void showAddInformation(PGraphics pg, float x, float y,float radius, String textInformation, int textColor,int txtSize, int boxColor, boolean noFrame)
	{
		if(!textInformation.isEmpty())
		{
			pg.pushStyle();
			String[] outputStrings =textInformation.split("\\n");
		
			pg.textSize(txtSize);
			float textWidht =  pg.textWidth(outputStrings[0]);
			int index = 1, countOfStrings = outputStrings.length;
			while(index < countOfStrings)
			{
				textWidht = Math.max(textWidht, pg.textWidth(outputStrings[index]));
				index++;
			}
			if(noFrame)
			{
				pg.noStroke();
			}else
			{
				pg.stroke(0);
			}
			pg.fill(boxColor);
			pg.rect(x+radius, y-radius-(txtSize*(countOfStrings-1)/4+txtSize*countOfStrings+txtSize/2), textWidht+txtSize/2, txtSize*(countOfStrings-1)/4+txtSize*countOfStrings+txtSize/2);
		
			pg.stroke(textColor);
			pg.textAlign(PConstants.LEFT, PConstants.TOP);
			pg.fill(textColor);
			index=0;
			while(index < countOfStrings)
			{
				pg.text(outputStrings[index], x+radius+txtSize/4, y-radius-(txtSize*(countOfStrings-1)/4+txtSize*countOfStrings+txtSize/2)+5*index*txtSize/4);
				index++;
			}
			pg.popStyle();
		}
	
		
	}
}