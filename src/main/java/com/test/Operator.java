package com.test;

public enum Operator {

  ADD("+"),
  MINUS("-"),
  DIVIDE("/"),
  SQRT("sqrt"),
  UNDO("undo"),
  CLEAR("clear"),
  MULTIPLY("*");

  private String operator;

  Operator(String operator) {
    this.operator = operator;
  }

  @Override
  public String toString() {
    return this.operator;
  }

  public static boolean contains(String s) {
    for (Operator op : Operator.values()) {
      if ((op.toString()).equalsIgnoreCase(s)) {
        return true;
      }
    }
    return false;
  }

  public static Operator fromString(String text) {
    for (Operator op : Operator.values()) {
      if (op.toString().equalsIgnoreCase(text)) {
        return op;
      }
    }
    return null;
  }

}
