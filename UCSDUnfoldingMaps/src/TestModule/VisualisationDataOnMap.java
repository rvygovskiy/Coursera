package TestModule;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class VisualisationDataOnMap extends PApplet
{
	UnfoldingMap map;
	Map<String,Float> lifeExpectancyByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup()
	{
		
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		lifeExpectancyByCountry = loadLifeExpectancyFromCV("..\\data\\LifeExpectancyWorldBank.csv");
		countries = GeoJSONReader.loadData(this, "..\\data\\countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		map.addMarkers(countryMarkers);
		shadeCountries();
		
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
	private Map<String,Float> loadLifeExpectancyFromCV(String fileName)
	{
		Map<String,Float> expLifeMap = new HashMap<String,Float>();
		String[] rows = loadStrings(fileName);
		for (String  row : rows)
		{
			String[] columns = row.split(",");
			if(columns.length == 6 && !columns[5].equals(".."))
			{
				float value = Float.parseFloat(columns[5]);
				expLifeMap.put(columns[4], value);
			}
		}
		return expLifeMap;
	}
	public void draw()
	{
		map.draw();
	}

}
