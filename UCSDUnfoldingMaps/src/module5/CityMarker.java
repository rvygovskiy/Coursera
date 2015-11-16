package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 7;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
		super.setRadius(TRI_SIZE);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	/*public void draw(PGraphics pg, float x, float y) {
		// Save previous drawing style
			
		// IMPLEMENT: drawing triangle for each city
		//pg.fill(150, 30, 30);
		//pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		drawMarker(pg, x, y);
		// Restore previous drawing style

	}
	*/
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		pg.pushStyle();
		String displayedText = getCity()+" " +getCountry()/*+" population: " + getPopulation()+"m"*/;
		pg.textSize(10);
		float textWidht = pg.textWidth(displayedText);
		pg.fill(200,200,200);
		pg.rect(x+radius, y-radius-14, textWidht+4, 14);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(displayedText, x+radius+2, y-radius-12);
		pg.popStyle();
		
	}
	
	public String getTitleInfo()
	{
		String titleInfo = getCity()+" " +getCountry() 
			+ "\nPop: "+ getPopulation() +"M";
		return titleInfo;
	}
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}


	@Override
	public void drawMarker(PGraphics pg, float x, float y) 
	{
		int cMarker = pg.color(150,0,255);
		pg.pushStyle();
		pg.fill(cMarker);
		DrawCentratedFigure.triangle(pg, x, y, TRI_SIZE);
		pg.popStyle();
	}
}
