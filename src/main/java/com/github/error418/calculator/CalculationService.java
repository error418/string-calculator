package com.github.error418.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.error418.calculator.exception.CalculationException;
import com.github.error418.calculator.exception.InvalidCalculationException;

/**
 * CalculationService provides functionality to evaluate calculation strings.
 * 
 * @author mge
 *
 */
public class CalculationService {
	
	private static final char SCRIPT_VARIABLE_PREFIX = '$';
	
	private static final String SCRIPT_ENGINE = "JavaScript";
	private static final String CALC_CHAR_WHITELIST = "^(\\$[a-zA-Z]+|\\d+(\\.\\d+)?)(\\s*[\\+\\-\\/\\*]\\s*(\\$[a-zA-Z]+|\\d+(\\.\\d+)?))*$";
	private static final String VAR_PATTERN = "^[a-zA-Z]+$";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CalculationService.class);
	
	private final ScriptEngine engine;
	private final Pattern whiteListPattern;
	private final Pattern variablePattern;
	
	/**
	 * Standard Constructor
	 */
	public CalculationService() {
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
	 * Calculates a given calculation string and substitutes the passed variables.
	 * 
	 * @param calculation calculation string
	 * @param variables variable map
	 * @return calculation result
	 * @throws CalculationException
	 */
	public final Double calculate(String calculation, Map<String, Double> variables) throws CalculationException {

		if(!isValid(calculation)) {
			throw new InvalidCalculationException("the calculation string is invalid '" + calculation + "'");
		}
		
		StringBuilder engineInput = new StringBuilder();
		
		for(String key : variables.keySet()) {
			if(variablePattern.matcher(key).matches()) {
				engineInput
					.append("var ")
					.append(SCRIPT_VARIABLE_PREFIX)
					.append(key)
					.append("=")
					.append(variables.get(key))
					.append(";");
			} else {
				LOGGER.warn("variable name '{}' is not valid and will not be processed.", key);
			}
		}
		
		engineInput.append(calculation);
		
	    try {
	    	return ((Number)engine.eval(engineInput.toString())).doubleValue();
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
	public final Double calculate(String calculation) throws CalculationException {
		return this.calculate(calculation, new HashMap<String, Double>());
	}
	
}
