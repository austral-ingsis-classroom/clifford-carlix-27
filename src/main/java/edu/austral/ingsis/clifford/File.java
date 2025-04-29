package edu.austral.ingsis.clifford;

public final class File implements FileSystem {
  private final String name;
  private final Directory parent;

  public File(String name, Directory parent) {
    this.name = name;
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public Directory getParent() {
    return parent;
  }

  @Override
  public Result apply(Operation operation) {
    return operation.applyTo(this);
  }
}
