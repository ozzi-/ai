package testing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Calculation;
import ai.ActorData;


public class CalculationTest {
	
	public final double epsilon = 0.01;

	@Test
	public void test(){
		ActorData ad_2_1 = new ActorData(2, 1, 5);
		ActorData ad_4_5 = new ActorData(4, 5, 5);

		double dir = Calculation.getDirection(ad_2_1,ad_4_5);
		assertEquals(dir, -2.034,epsilon);
		
		dir = Calculation.getDirection(ad_4_5,ad_2_1);
		assertEquals(dir, 1.107,epsilon);
	}
	
	@Test
	public void testDistance() {
		ActorData ad_0_0 = new ActorData(0, 0, 5);
		ActorData ad_0_5 = new ActorData(0, 5, 5);
		ActorData ad_5_0 = new ActorData(5, 0, 5);
		ActorData ad_5_5 = new ActorData(5, 5, 5);
		ActorData ad_7_3 = new ActorData(7, 3, 5);
		ActorData ad_min2_3 = new ActorData(-2, 3, 5);
		
		double distance_0_0_and_0_5= Calculation.getDistance(ad_0_0, ad_0_5);
		assertEquals(distance_0_0_and_0_5,5,epsilon);
		
		double distance_0_5_and_5_0= Calculation.getDistance(ad_0_5, ad_5_0);
		assertEquals(distance_0_5_and_5_0,7,epsilon);
		
		double distance_5_5_and_7_3= Calculation.getDistance(ad_5_5, ad_7_3);
		assertEquals(distance_5_5_and_7_3,2,epsilon);
		
		double distance_7_3_and_5_5= Calculation.getDistance(ad_7_3, ad_5_5);
		assertEquals(distance_7_3_and_5_5,2,epsilon);
		
		double distance_min2_3_and_7_3= Calculation.getDistance(ad_min2_3, ad_7_3);
		assertEquals(distance_min2_3_and_7_3,9,epsilon);

		
	}
}
