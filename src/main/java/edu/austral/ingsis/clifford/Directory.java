package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public final class Directory implements FileSystem {
  private final String name;
  private final Directory parent;
  private final List<FileSystem> items;

  // Constructor for items
  public Directory(String name, Directory parent, List<FileSystem> items) {
    this.name = name;
    this.parent = parent;
    this.items = new ArrayList<>(items);
  }

  // Constructor for an empty directory
  public Directory(String name, Directory parent) {
    this(name, parent, new ArrayList<>());
  }

  public String getName() {
    return name;
  }

  public Directory getParent() {
    return parent;
  }

  public List<FileSystem> getItems() {
    return items;
  }

  @Override
  public Result apply(Operation operation) {
    return operation.applyTo(this);
  }


}
