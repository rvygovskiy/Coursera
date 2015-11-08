package TestModule;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;

import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class VisualisationDataOnMap extends PApplet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;
	HashMap<String,Float> lifeExpectancyByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup()
	{
		
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		lifeExpectancyByCountry = loadLifeExpectancyFromCV("LifeExpectancyWorldBank.csv");
		println("Loaded LEbC" + lifeExpectancyByCountry.size() + " data entries");
		
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		println("Loaded Countries" + countries.size() + " data entries");
		
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		map.addMarkers(countryMarkers);
		shadeCountries();
		//Draw 2 squared buttons
	/*	fill(255,255,255);
		rect(50, 50, 25, 25);
		
		fill(100,100,100);
		rect(50, 100, 25, 25);
	*/	
	}
	public void draw()
	{
		map.draw();
		backgroundButtons();
	}
	public void backgroundButtons()
	{
		fill(255,255,255);
		rect(50, 50, 25, 25);
		
		fill(100,100,100);
		rect(50, 100, 25, 25);	
	}
	public void mousePressed()
	{
		
	}
	public void mouseClicked()
	{
		
	}
	public void mouseReleased()
	{
		if(mouseX > 50 && mouseX <75 && 
				mouseY > 50 && mouseY < 75)
		{
			background(255, 255, 255);
		}
		if(mouseX > 50 && mouseX <75 && 
				mouseY > 100 && mouseY < 125)
		{
			background(100, 100, 100);
		}
	}
	
	public void keyPressed()
	{
		if(key == 'b')
		{
			background(255, 255, 255);
		}
	}
	private void shadeCountries()
	{
		for(Marker marker : countryMarkers )
		{
			String countryId = marker.getId();
			if(lifeExpectancyByCountry.containsKey(countryId))
			{
				float lifeExp = lifeExpectancyByCountry.get(countryId);
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else
			{
				marker.setColor(color(150, 150, 150));
			}
				
				
		}
	}
	private HashMap<String,Float> loadLifeExpectancyFromCV(String fileName)
	{
		HashMap<String,Float> expLifeMap = new HashMap<String,Float>();
		String[] rows = loadStrings(fileName);
		for (String  row : rows)
		{
			String[] columns = row.split(",");
			if(columns.length >= 6 && !columns[5].equals(".."))
			{
				try
				{
					expLifeMap.put(columns[4], Float.parseFloat(columns[5]));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Country name: " + columns[2]+ columns[3]+ " country ID: "+ columns[4] + " witj first value: " + columns[5]+e);
				}
				
			}
		}
		return expLifeMap;
	}

}
