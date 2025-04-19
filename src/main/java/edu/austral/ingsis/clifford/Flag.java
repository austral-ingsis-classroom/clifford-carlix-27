package edu.austral.ingsis.clifford;

public class Flag {
  private final String key;
  private final String value;

  public Flag(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
