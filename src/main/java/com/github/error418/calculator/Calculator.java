package com.github.error418.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.error418.calculator.exception.CalculationException;
import com.github.error418.calculator.exception.InvalidCalculationException;

/**
 * Evaluates string calculations.
 * 
 * A calculation string can also contain variables, which need to be prefixed with a {@value #SCRIPT_VARIABLE_PREFIX} character.
 * <br />
 * 
 * Calculation String examples:
 * <ul>
 *  <li>1 + 2 + 3</li>
 * 	<li>10 + 20 + $variable</li>
 *  <li>$variableA + $variableB</li>
 * </ul>
 * 
 * @author Michael Gerbig
 *
 */
public class Calculator {
	
	private static final char SCRIPT_VARIABLE_PREFIX = '$';
	
	private static final String SCRIPT_ENGINE = "JavaScript";
	private static final String CALC_CHAR_WHITELIST = "^(\\$[a-zA-Z]+|\\d+(\\.\\d+)?)(\\s*[\\+\\-\\/\\*]\\s*(\\$[a-zA-Z]+|\\d+(\\.\\d+)?))*$";
	private static final String VAR_PATTERN = "^[a-zA-Z]+$";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Calculator.class);
	
	private final ScriptEngine engine;
	private final Pattern whiteListPattern;
	private final Pattern variablePattern;
	
	/**
	 * Constructs a Calculator and creates a JavaScript Engine for calculation operations. Consider reusing a constructed Calculator. 
	 */
	public Calculator() {
		ScriptEngineManager mgr = new ScriptEngineManager();
	    engine = mgr.getEngineByName(SCRIPT_ENGINE);
	    
	    whiteListPattern = Pattern.compile(CALC_CHAR_WHITELIST, Pattern.MULTILINE);
	    variablePattern = Pattern.compile(VAR_PATTERN);
	}
	
	/**
	 * Checks, if a calculation string is valid
	 * 
	 * @param calculation calculation string to check
	 * @return true, if string is valid; otherwise false
	 */
	public final boolean isValid(String calculation) {
		return calculation != null && calculation.trim().length() > 0 && whiteListPattern.matcher(calculation).matches();
	}

	/**
	 * Compile a script for reuse.
	 * 
	 * @param calculation calculation string
	 * @return compiled script
	 * @throws CalculationException if calculation script failed to compile
	 */
	public final CompiledCalculation compile(String calculation) throws CalculationException {
		if (!isValid(calculation)) {
			throw new InvalidCalculationException("the calculation string is invalid '" + calculation + "'");
		}
		
		if(engine instanceof Compilable) {
			Compilable compiler = (Compilable)engine;
			
			try {
				return new CompiledCalculation(compiler.compile(calculation));
			} catch (ScriptException e) {
				throw new InvalidCalculationException("the calculation string is invalid '" + calculation + "'", e);
			}
		} else {
			throw new InvalidCalculationException("engine does not support compiling code.");
		}
	}
	
	/**
	 * Execute a previously compiled script with the given variables.
	 * 
	 * @param compiled compiled calculation script
	 * @param variables variables passed to the script
	 * @return calculation result
	 * @throws CalculationException if script execution failed
	 */
	public final Number calculate(CompiledCalculation compiled, HashMap<String, Number> variables) throws CalculationException {
		Bindings bindings = toBindings(variables);

		try {
	    	return ((Number)compiled.getCompiledScript().eval(bindings));
	    }
	    catch (ScriptException e) {
	    	throw new CalculationException("an exception was thrown while processing calculation", e);
	    }
	}
	
	/**
	 * Calculates a given calculation string and substitutes the passed variables.
	 * 
	 * @param calculation calculation string
	 * @param variables variable map
	 * @return calculation result
	 * @throws CalculationException
	 */
	public final Number calculate(String calculation, Map<String, Number> variables) throws CalculationException {

		if(!isValid(calculation)) {
			throw new InvalidCalculationException("the calculation string is invalid '" + calculation + "'");
		}
		
		Bindings bindings = toBindings(variables);
		
	    try {
	    	return ((Number)engine.eval(calculation, bindings));
	    }
	    catch (ScriptException e) {
	    	throw new CalculationException("an exception was thrown while processing calculation", e);
	    }
	}
	
	/**
	 * Performs a simple calculation.
	 * 
	 * @param calculation calculation string
	 * @return calculation result
	 * @throws CalculationException
	 */
	public final Number calculate(String calculation) throws CalculationException {
		return this.calculate(calculation, new HashMap<String, Number>());
	}
	
	/**
	 * Builds a {@link Bindings} instance from a passed {@link Map}.
	 * 
	 * This method checks the {@link Map}s key values to comply to the RegEx Pattern {@value #VAR_PATTERN}. If the key does not comply it will be ignored.
	 * 
	 * @param map variables
	 * @return Bindings instance containing the variables
	 */
	private Bindings toBindings(Map<String, Number> map) {
		Bindings bindings = new SimpleBindings();
		
		for(String key : map.keySet()) {
			if(variablePattern.matcher(key).matches()) {
				bindings.put(SCRIPT_VARIABLE_PREFIX + key, map.get(key));
			} else {
				LOGGER.warn("variable name '{}' is not valid and will not be processed. Please comply to RegEx Pattern {}", key, VAR_PATTERN);
			}
		}
		
		return bindings;
	}
	
}
