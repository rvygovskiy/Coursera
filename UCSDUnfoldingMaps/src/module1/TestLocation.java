package module1;

import demos.SimpleLocation;

public class TestLocation
{
	public static void main(String[] args)
	{
		SimpleLocation Kiev = new SimpleLocation(50.47f, 30.33f);
		//double d = Kiev.distance(new SimpleLocation());
		System.out.println("The distance from me to San Diego is: " + Kiev.distance(new SimpleLocation()) + " km");
	}

}