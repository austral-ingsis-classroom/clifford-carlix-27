package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

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
  public Result lsCommand(Flag flag) {
    return new Error("Operation not supported");
  }

  @Override
  public Result cdCommand(String directory_name) {
    return new Error("Operation not supported");
  }

  @Override
  public Result touchCommand(String file_name) {
    return new Error("Operation not supported");
  }

  @Override
  public Result mkDirCommand(String directory_name) {
    return new Error("Operation not supported");
  }

  @Override
  public Result rmCommand(String file_name, Flag flag) {
    return new Error("Operation not supported");
  }

  @Override
  public Result pwdCommand() {
    List<String> pathParts = new ArrayList<>();
    Directory current = parent;

    while (current != null) {
      pathParts.add(current.getName());
      current = current.getParent();
    }

    // Invierte -> posible caso a tener en cuenta

    pathParts.add(name);

    return new Success<>(String.join("/", pathParts));
  }
}
