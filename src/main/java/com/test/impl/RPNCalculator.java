package com.test.impl;

import com.google.common.collect.ImmutableMap;
import com.test.Calculator;
import com.test.Operator;
import com.test.exception.InsufficientParametersException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RPNCalculator implements Calculator {

  public static final String SPACE = " ";
  public static final int STACK_PRECISION = 15;
  public static final int DISPLAY_PRECISION = 10;

  Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

  Map<String, Integer> operatorIntegerMap =
      new ImmutableMap.Builder<String, Integer>().
          put("+", 2).
          put("-", 2).
          put("/", 2).
          put("sqrt", 1).
          put("*", 2).
          put("undo", 0).
          put("clear", Integer.MAX_VALUE)
          .build();

  public String calculate(String input) {
    int pos = 0;
    Double firstNumber;
    Double secondNumber;
    Stack<Double> numbers = new Stack<>();
    Stack<String> historyNumbers = new Stack<>();
    Stack<String> numberStack = new Stack<>();
    String[] numberArray = input.trim().split(SPACE);
    for (int i = numberArray.length - 1; i >= 0; i--) {
      numberStack.push(numberArray[i]);
    }
    while (!numberStack.empty()) {
      pos++;
      String number = numberStack.pop();
      historyNumbers.push(number);
      if (isNumber(number)) {
        numbers.push(Double.valueOf(number));
      } else if (Operator.contains(number)) {
        switch (Operator.fromString(number)) {
          case ADD:
            firstNumber = getNumberFromStack(numbers, number, pos);
            secondNumber = getNumberFromStack(numbers, number, pos);
            numbers.push(convertToPrecision(firstNumber + secondNumber, STACK_PRECISION));
            break;
          case MINUS:
            firstNumber = getNumberFromStack(numbers, number, pos);
            secondNumber = getNumberFromStack(numbers, number, pos);
            numbers.push(convertToPrecision(secondNumber - firstNumber, STACK_PRECISION));
            break;
          case SQRT:
            firstNumber = getNumberFromStack(numbers, number, pos);
            numbers.push(convertToPrecision(Math.sqrt(firstNumber), STACK_PRECISION));
            break;
          case DIVIDE:
            firstNumber = getNumberFromStack(numbers, number, pos);
            secondNumber = getNumberFromStack(numbers, number, pos);
            numbers.push(convertToPrecision(secondNumber / firstNumber, STACK_PRECISION));
            break;
          case MULTIPLY:
            firstNumber = getNumberFromStack(numbers, number, pos);
            secondNumber = getNumberFromStack(numbers, number, pos);
            numbers.push(convertToPrecision(firstNumber * secondNumber, STACK_PRECISION));
            break;
          case CLEAR:
            historyNumbers.pop();
            while (!numbers.isEmpty()) {
              Double numberInStack = getNumberFromStack(numbers, number, pos);
              historyNumbers.push(String.valueOf(numberInStack));
            }
            historyNumbers.push(number);
            break;
          case UNDO:
            int undoCount = 0;
            getNumberFromStack(numbers, number, pos);
            String previousOp = historyNumbers.pop();
            while (Operator.fromString(previousOp) == Operator.UNDO) {
              undoCount++;
              previousOp = historyNumbers.pop();
            }
            if (!(Operator.fromString(previousOp) == Operator.UNDO) && !isNumber(previousOp)) {
              numberStack.push(previousOp);
            }
            if (isNumber(previousOp)) {
            } else {
              int opCount = operatorIntegerMap.get(previousOp);
              for (int i = 0; i < opCount; i++) {
                String undoNumber = historyNumbers.pop();
                numbers.push(Double.valueOf(undoNumber));
              }
            }
            break;
          default:
        }
      } else {
        throw new UnsupportedOperationException("Invalid input strings");
      }
    }
    logger.info(numbers);
    return numbers.empty() ? ""
        : String.valueOf(convertToPrecision(numbers.pop(), DISPLAY_PRECISION));
  }

  private Double getNumberFromStack(Stack<Double> stack, String number, int pos) {
    Double result;
    try {
      result = stack.pop();
    } catch (EmptyStackException e) {
      String errorMessage = String.format(
          "operator %s (position: %2d): insucient parameters", number, pos);
      throw new InsufficientParametersException(errorMessage);
    }
    return result;
  }

  private Double convertToPrecision(Double source, int precision) {
    Double target = BigDecimal.valueOf(source)
        .setScale(precision, RoundingMode.HALF_UP)
        .doubleValue();
    return target;
  }

  private boolean isNumber(String numberString) {
    boolean result;
    try {
      Double.parseDouble(numberString);
      result = true;
    } catch (NumberFormatException e) {
      result = false;
    }
    return result;
  }

}
