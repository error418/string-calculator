package com.github.error418.calculator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.error418.calculator.exception.CalculationException;
import com.github.error418.calculator.exception.InvalidCalculationException;

public class CalculationTest {

	private Calculator uut;
	
	private String[] invalidCalculations;
	private String[] validCalculations;
	
	@Before
	public void init() {
		uut = new Calculator();
		
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
		HashMap<String, Number> variables = new HashMap<String, Number>();
		
		variables.put("testa", 10D);
		variables.put("testb", 5D);
		
		Number calculationResult = uut.calculate("$testa * $testb", variables);
	
		Assert.assertEquals(50D, calculationResult.doubleValue(), 0.01);
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
	
	@Test
	public void testSimpleCompiledScript() throws Exception {
		CompiledCalculation script = uut.compile("10 + 20");
		Number result = uut.calculate(script, new HashMap<String, Number>());
		
		Assert.assertEquals(30D, result.doubleValue(), 0.01);
	}
	
	@Test
	public void testVariableCompiledScript() throws Exception {
		CompiledCalculation calculation = uut.compile("$longVarName + 20 + $a");
		
		HashMap<String, Number> vars = new HashMap<String, Number>();
		vars.put("a", 20);
		vars.put("longVarName", 10);
		
		Number result = uut.calculate(calculation, vars);
		
		Assert.assertEquals(50D, result.doubleValue(), 0.01);
	}
}
