# string-calculator
A simple string calculator supporting variables for Java.

```java
HashMap<String, Number> variables = new HashMap<String, Number>();

// notice no prepended "$" character
variables.put("testA", 10D);
variables.put("testB", 5D);


Calculator calculator = new Calculator();
Number calculationResult = calculator.calculate("$testa * $testb", variables);
```

Calculation is done by utilizing the JavaScript capabilities of Java.

## Calculation Strings

Variables in calculation strings need to be prefixed with the `$` character.


### Compiling Calculation Strings

If you need to run calculation strings multiple times you should consider precompiling the calculation string.