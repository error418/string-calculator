# string-calculator
A simple string calculator supporting variables for Java.

Calculation is done by utilizing the JavaScript capabilities of Java.

## Usage

Start by creating a `Calculator` instance and you are ready to go.
For example:

		HashMap<String, Number> variables = new HashMap<String, Number>();
		
		variables.put("testa", 10D);
		variables.put("testb", 5D);
		
		Number calculationResult = uut.calculate("$testa * $testb", variables);

