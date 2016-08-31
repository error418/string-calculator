# string-calculator
[![Build Status](https://travis-ci.org/error418/string-calculator.svg?branch=enhancement%2Ftravis-ci)](https://travis-ci.org/error418/string-calculator)

A simple string calculator supporting variables for Java.

Calculation is done by utilizing the JavaScript capabilities of Java.


## Usage

Start by creating a `Calculator` instance and you are ready to go.
For example:

```java
HashMap<String, Number> variables = new HashMap<String, Number>();

variables.put("testa", 10D);
variables.put("testb", 5D);

Number calculationResult = uut.calculate("$testa * $testb", variables);
```

## JDK Versions

Supported (and tested) JDK versions are

* Oracle JDK 8
* Oracle JDK 7
* OpenJDK 7
