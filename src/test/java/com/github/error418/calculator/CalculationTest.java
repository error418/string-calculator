package com.github.error418.calculator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.github.error418.calculator.CalculationService;
import com.github.error418.calculator.exception.CalculationException;
import com.github.error418.calculator.exception.InvalidCalculationException;

public class CalculationTest {

	private CalculationService uut;
	
	private String[] invalidCalculations;
	private String[] validCalculations;
	
	@Before
	public void init() {
		uut = new CalculationService();
		
		invalidCalculations = new String[] {
			"3 + 2 * 9 // 2",
			"+ 1 + 2 + 3",
			" 2 2 2 + 2",
			" asdasdad + 2",
			null,
			"",
			"      ",
			"\t"
		};
		
		validCalculations = new String[] {
			"3 + 3",
			"1 + 2 + 3 * 4 / 5 - 9",
			"2*2",
			"1233331.0 * 3",
			"$abc + $def"
		};
	}
	
	@Test
	public void testVariables() throws CalculationException {
		HashMap<String, Double> variables = new HashMap<String, Double>();
		
		variables.put("testa", 10D);
		variables.put("testb", 5D);
		
		Double calculationResult = uut.calculate("$testa * $testb", variables);
	
		assertTrue("needs to calculate properly", calculationResult.equals(50D));
	}
	
	@Test
	public void testCalculationValidation() {
		
		
		for(String c : invalidCalculations) {
			assertFalse("'" + c + "' should be invalid", uut.isValid(c));
		}
		
		for(String c : validCalculations) {
			assertTrue("'" + c + "' should be valid", uut.isValid(c));
		}
	}
	
	@Test(expected = InvalidCalculationException.class)
	public void checkCalculationException() throws CalculationException {
		uut.calculate(invalidCalculations[0]);
	}
}
