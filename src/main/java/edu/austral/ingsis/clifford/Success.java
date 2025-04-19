package edu.austral.ingsis.clifford;

public final class Success<T> implements Result {
  private final String message;
  private final T value;

  public Success(String message, final T value) {
    this.message = message;
    this.value = value;
  }

  public Success(String message) {
    this(message, null);
  }

  public String getMessage() {
    return message;
  }

  public T getValue() {
    return value;
  }
}
