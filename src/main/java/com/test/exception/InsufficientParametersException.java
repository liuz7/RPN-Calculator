package com.test.exception;

public class InsufficientParametersException extends RuntimeException {

  public InsufficientParametersException(String message) {
    super(message);
  }
}
