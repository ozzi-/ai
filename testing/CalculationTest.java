package testing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Calculation;
import ai.ActorData;


public class CalculationTest {
	
	public final double epsilon = 0.01;

	@Test
	public void test(){
		ActorData ad_2_1 = new ActorData(2, 1);
		ActorData ad_4_5 = new ActorData(4, 5);

		double dir = Calculation.getDirection(ad_2_1,ad_4_5);
		assertEquals(dir, -2.034,epsilon);
		
		dir = Calculation.getDirection(ad_4_5,ad_2_1);
		assertEquals(dir, 1.107,epsilon);
	}
	
	
	@Test
	public void angleLine(){
		ActorData line1 = new ActorData(10, 15);
		line1.setX_end(20);
		line1.setY_end(14);
		ActorData line2 = new ActorData(10, 15);
		line2.setX_end(30);
		line2.setY_end(16);
		double angle = Calculation.angleBetween2Lines(line1, line2);
		assertEquals(6.13, angle,epsilon);
	}
	
	@Test
	public void testDistance() {
		ActorData ad_0_0 = new ActorData(0, 0);
		ActorData ad_0_5 = new ActorData(0, 5);
		ActorData ad_5_0 = new ActorData(5, 0);
		ActorData ad_5_5 = new ActorData(5, 5);
		ActorData ad_7_3 = new ActorData(7, 3);
		ActorData ad_min2_3 = new ActorData(-2, 3);
		
		double distance_0_0_and_0_5= Calculation.getDistance(ad_0_0, ad_0_5);
		assertEquals(distance_0_0_and_0_5,5,epsilon);
		
		double distance_0_5_and_5_0= Calculation.getDistance(ad_0_5, ad_5_0);
		assertEquals(distance_0_5_and_5_0,7.07,epsilon);
		
		double distance_5_5_and_7_3= Calculation.getDistance(ad_5_5, ad_7_3);
		assertEquals(distance_5_5_and_7_3,2.82,epsilon);
		
		double distance_7_3_and_5_5= Calculation.getDistance(ad_7_3, ad_5_5);
		assertEquals(distance_7_3_and_5_5,2.82,epsilon);
		
		double distance_min2_3_and_7_3= Calculation.getDistance(ad_min2_3, ad_7_3);
		assertEquals(distance_min2_3_and_7_3,9.0,epsilon);

		
	}
}
