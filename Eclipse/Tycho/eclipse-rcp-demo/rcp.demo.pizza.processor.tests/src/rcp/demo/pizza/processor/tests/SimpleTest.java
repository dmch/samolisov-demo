package rcp.demo.pizza.processor.tests;

import org.junit.Assert;
import org.junit.Test;

import rcp.demo.pizza.processor.PizzaProcessor;

public class SimpleTest {
	
	@Test
	public void demo() {
		Assert.assertNotNull(PizzaProcessor.getAllPizzas());
		Assert.assertEquals(1.0, 0.0);
	}
}
