package module5;

import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	private boolean isFiltered = false;
	private NumberFormat formatter = NumberFormat.getNumberInstance();
	public void setup() {		
		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// Uncomment this line to take the quiz
		//earthquakesURL = "quiz2.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	    sortAndPrint(20);
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(200);
		map.draw();
		addKey();
		if(lastSelected != null && !lastSelected.isHidden())
		{
		//	lastSelected.showTitle(this.g,mouseX, mouseY);
			lastSelected.showAddInformation(this.g, mouseX, mouseY, lastSelected.getRadius(), lastSelected.getTitleInfo(), 0, 10, (lastSelected instanceof CityMarker)? color(203,230,231) : color(235,238,196), false);
		}
		if(lastClicked != null && !lastClicked.isHidden())
		{
		//	lastSelected.showTitle(this.g,mouseX, mouseY);
			lastClicked.showAddInformation(this.g, 25, 650, 0, lastClicked.getAddInformation(), 0, 10, 255, false);
		}
	}

	private void sortAndPrint(int numToPrint)
	{
		List<EarthquakeMarker> earthquakeList = new ArrayList<EarthquakeMarker>((Collection<? extends EarthquakeMarker>)quakeMarkers) ;
		
		Collections.sort(earthquakeList);
		int index = 0, sizeOfList = earthquakeList.size();
		String textToPrint;
		for (Marker eqm : earthquakeList)
		{	
			textToPrint = "";
			if(index <sizeOfList  && index < numToPrint)
			{
			textToPrint = index+1 + ". " +((EarthquakeMarker) eqm).getTitle(); 
			System.out.println(textToPrint);
			index++;
			}else
			{
				return;
			}
			
		}
	}
	

	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void keyPressed(java.awt.event.KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			if(!isFiltered)
			{
				hideMarkers(quakeMarkers);
				hideMarkers(cityMarkers);
				filterConnetedMarkers();
				isFiltered = true;
				
			}else
			{
				unhideMarkers();
				isFiltered = false;
			}
		}
	}
	public void filterConnetedMarkers()
	{
		for(Marker cityMarker : cityMarkers)
		{
			Location cityMarkerLocation = cityMarker.getLocation();
			for(Marker quakeMarker : quakeMarkers)
			{
				double	distanceToCity = quakeMarker.getDistanceTo(cityMarkerLocation)
						,threatCircle = ((EarthquakeMarker)quakeMarker).threatCircle();
				if(distanceToCity <= threatCircle)
				{
					quakeMarker.setHidden(false);
					cityMarker.setHidden(false);
				}
			}
			
		}
	}
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers)
	{Iterator<Marker>  markerIterator  = markers.iterator(); 
		while(lastSelected == null && markerIterator.hasNext())
		{
			CommonMarker nextMarker = (CommonMarker) markerIterator.next();
			if(nextMarker.isInside(map, mouseX, mouseY))
			{
				lastSelected = nextMarker;
				lastSelected.setSelected(true);
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	/*mouse clicke user action I start  processing at onCityMarkerClicked helper method which goes through city marker's list and makes:
	1. find first not hidden city marker if mouse clicked on it - in this case method returns true, other wise - false
	2. check status "clicked" of detected marker:
	2.1 If true - uncover all markers by set up false for status "Hidden" at helper method unhideMarkers. And setup false for status "clicked" of detected marker.
	2.2.If false - uncheck all markers by set up false for status "clicked" at   helper method unClickeMarkers. Setup true for status "clicked" of detected marker. Hide all city markers by setup true for  status "Hidden" at helper method hideMarkers except for detected marker.Go through earth quake marker list with check of threat distance to the detected city marker and hide all earth quake markers than lies not in threat circle.
	In case onCityMarkerClicked method return false I start the same analysis at onQuakeMarkerClicked helper method which goes through quake marker's list.
	Why is another helper method? - Because a different code: another point of view to consider the same condition.
	In case onQuakeMarkerClicked method return false uncover all markers  by set up false for status "Hidden" at helper method unhideMarkers and unchecked all markers by set up false for status "clicked" at   helper method unClickeMarkers.
	That's it.

	Well done! 
	Here's how our code handled mouse clicks:
	When the user clicks the mouse, the mouseClicked code in EarthquakeCityMap is called by Java. This method first checks the lastClicked variable. If it is null, meaning a city is already shown as "clicked", it sets lastClicked to null and unhides all the cities and earthquakes.
	Otherwise, it relies on two helper methods: checkEarthquakesForClick and checkCitiesForClick.
	checkEarthquakesForClick first checks lastClicked, and aborts if it is not null (just in case). Then it loops through all the earthquakes to see if one has been clicked on. If it finds one, it loops through all of the earthquake markers and sets all but the clicked earthquake to hidden. Then it loops through the city markers and sets all of the city markers outside of the clicked earthquake's threat circle to be hidden. It then returns so that it does not check anymore earthquakes.
	checkCitiesForClick first checks lastClicked, and aborts if it is not null (which could mean an earthquake has already been found as clicked). Then it loops through all the cities to see if one has been clicked on. If it finds one, it loops through all of the city markers and sets all but the clicked city to hidden. Then it loops through the earthquake markers and sets all of the earthquake markers for which the city is outside of the threat circle to be hidden. It then returns so that it does not check anymore cities.
	 * (non-Javadoc)
	 * @see processing.core.PApplet#mouseClicked()
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			lastClicked = null;
		
		}
		if(!onCityMarkerClicked(cityMarkers))
		{
			if(!onQuakeMarkerClicked(quakeMarkers))
				{
				unClickeMarkers();
				unhideMarkers();
				};
		}
	}
	
	
	private boolean onQuakeMarkerClicked(List<Marker> markers) 
	{	boolean clickedMarker = false;
		Iterator<Marker>  markerIterator  = markers.iterator();
		int numberConnetionObjets = 0;
		String addInformation = "", cityInformation="";
		if(markerIterator.hasNext())
		{
			CommonMarker nextMarker;
			do
			{
				nextMarker = (CommonMarker) markerIterator.next();
				clickedMarker = (nextMarker.isInside(map, mouseX, mouseY) && !(nextMarker.isHidden()));
			}while (!(clickedMarker) && markerIterator.hasNext());
			if(clickedMarker)
			{
				if(nextMarker.getClicked())
				{
					unhideMarkers();
					nextMarker.setClicked(false);
				}else
				{
					unClickeMarkers();
					hideMarkers(quakeMarkers);
					nextMarker.setClicked(true);
					nextMarker.setHidden(false);
					double threatCircle = ((EarthquakeMarker)nextMarker).threatCircle();
					String strThreatCircle = formatter.format(threatCircle);
					Location clickedMarkerLocation = nextMarker.getLocation();
					addInformation = nextMarker.getTitleInfo()+
							"\nLocation: "+clickedMarkerLocation.toString()+
							"\nThreat circle: " + strThreatCircle+ "km";
					for(Marker qMarker: cityMarkers)
					{
						double distanceToCity;
						distanceToCity = qMarker.getDistanceTo(clickedMarkerLocation);
						if(distanceToCity  <= threatCircle)
						{
							String strDistanceToCity = formatter.format (distanceToCity);
							qMarker.setHidden(false);
							cityInformation+="\n" + ((CommonMarker)qMarker).getTitleInfo() +
									"\nDistance: " + strDistanceToCity + "km";
							numberConnetionObjets++;
						}else
						{
							qMarker.setHidden(true);
						}
					}
					addInformation+= "\n"+ ((numberConnetionObjets == 0) ? "There is no any city in danger area" :(((numberConnetionObjets == 1)? "There is 1 city in danger area": ("There are " +numberConnetionObjets +" cities in danger area"))))+
								cityInformation;
					((CommonMarker)nextMarker).setNumberConnetionObjets(numberConnetionObjets);
					((CommonMarker)nextMarker).setAddInformation(addInformation);
					
				}
				lastClicked = nextMarker;
			}
		}
		return clickedMarker;	
	}


	private boolean onCityMarkerClicked(List<Marker> markers) 
	{	
		boolean clickedMarker = false;
		int numberConnetionObjets = 0;
		String addInformation = "", quakeInformation="";
		Iterator<Marker>  markerIterator  = markers.iterator(); 
		if(markerIterator.hasNext())
		{
			CommonMarker nextMarker;
			do
			{
				nextMarker = (CommonMarker) markerIterator.next();
				clickedMarker = (nextMarker.isInside(map, mouseX, mouseY) && !(nextMarker.isHidden()));
			}while (!(clickedMarker) && markerIterator.hasNext());
			if(clickedMarker)
			{
				if(nextMarker.getClicked())
				{
					unhideMarkers();
					nextMarker.setClicked(false);
					
				}else
				{
					unClickeMarkers();
					hideMarkers(cityMarkers);
					nextMarker.setClicked(true);
					nextMarker.setHidden(false);
					Location clickedMarkerLocation = nextMarker.getLocation();
					addInformation = nextMarker.getTitleInfo()+
							"\nLocation: "+clickedMarkerLocation.toString();
					
					for(Marker qMarker: quakeMarkers)
					{
						double	distanceToCity = qMarker.getDistanceTo(clickedMarkerLocation)
								,threatCircle = ((EarthquakeMarker)qMarker).threatCircle();
						String	strDistanceToCity = formatter.format(distanceToCity)
								,strThreatCircle = formatter.format(threatCircle);
						
						if(distanceToCity <= threatCircle)
						{
							qMarker.setHidden(false);
							quakeInformation+="\n" + ((CommonMarker)qMarker).getTitleInfo() +
									"\nLocation: "+(qMarker.getLocation()).toString()+
									"\nThreat circle: " + strThreatCircle+ "km"+
									"\nDistance: " + strDistanceToCity + "km";
							numberConnetionObjets++;
					
						}else
						{
							qMarker.setHidden(true);
						}
					}
				addInformation+= "\n"+ ((numberConnetionObjets == 0) ? "The city is in a safe area" :(((numberConnetionObjets == 1)? "There is 1 eartquake in danger area": ("There are " +numberConnetionObjets +" eartquakes in danger area"))))+
							quakeInformation;
				((CommonMarker)nextMarker).setNumberConnetionObjets(numberConnetionObjets);
				((CommonMarker)nextMarker).setAddInformation(addInformation);
				
				}
				lastClicked = nextMarker;
				
			}
		}
		return clickedMarker;	
	}
	
	private void hideMarkers(List<Marker> markers)
	{
		for(Marker marker : markers)
		{
			marker.setHidden(true);
		}
	}

	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	private void unClickeMarkers() {
		for(Marker marker : quakeMarkers) {
			((CommonMarker) marker).setClicked(false);
		}
			
		for(Marker marker : cityMarkers) {
			((CommonMarker) marker).setClicked(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		fill(150, 0, 250);
		DrawCentratedFigure.triangle(this.g, tri_xbase, tri_ybase, CityMarker.TRI_SIZE);
		/*fill(150, 30, 30);
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);
		*/
		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
			
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.	
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	/*private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	*/
	private void printQuakes() 
	{
		HashMap<String, Float> countryEarthquake = new HashMap<String, Float>();
		float count;
		float OseanCounts = 0;
		String str;
		for(Marker quake : quakeMarkers)
		{
			str = (String) quake.getProperty("country");
			if (str != null)
			{
				if (countryEarthquake.get(str) != null)
				{
					count = (float)countryEarthquake.get(str) +1;
				} else
				{
					count = 1;
				}	
				countryEarthquake.put(str, count);
			} else
			{
				OseanCounts++;
			}
		}
		for(HashMap.Entry<String, Float> e : countryEarthquake.entrySet())
		{
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		System.out.println("Osean's earthquakes : " + OseanCounts);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
