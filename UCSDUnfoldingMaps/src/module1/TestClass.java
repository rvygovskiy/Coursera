package module1;
public class TestClass
{
	public static void main(String[] arg)
	{
		double Pi = 3.14;
		int koef = 7;
		double d_result = koef*Pi;
		int i_result = (koef*koef);
		System.out.println("double result is: " + d_result + " int result is: " + i_result);
		
		double[] coords = {5.0,3.2};
		ArrayLocation testLoc = new ArrayLocation(coords);
		coords[0] = 7.4;
		System.out.println("coords[0]: " + coords[0]
					+ "\n coords[1]: " + coords[1]+
					"\n testLoc.coords[0]: " + testLoc.getCoord()[0]);
		
	}
}