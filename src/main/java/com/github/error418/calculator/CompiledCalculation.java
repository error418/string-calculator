package com.github.error418.calculator;

import javax.script.CompiledScript;

/**
 * Holds a CompiledScript.
 * 
 * Purpose of this class is to prevent direct access to the CompiledScript instance from outside of the package as well as adding a abstraction layer.
 *
 */
public class CompiledCalculation {
	private CompiledScript compiled;
	
	CompiledCalculation(CompiledScript compiled) {
		this.compiled = compiled;
	}
	
	CompiledScript getCompiledScript() {
		return compiled;
	}

	void setCompiledScript(CompiledScript compiledScript) {
		this.compiled = compiledScript;
	}
	
	
}
