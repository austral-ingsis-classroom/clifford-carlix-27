package edu.austral.ingsis.clifford;

public final class Cd implements Command {

  @Override
  public Result execute(String input, FileSystem fileSystem) {
    String path = extractPath(input);
    if (path.isEmpty()) {
      return new Error("Path is empty");
    }
    Operation operation = new ChangeDirectory(path);
    return fileSystem.apply(operation);
  }

  @Override
  public boolean canExecute(String input) {
    return input.trim().startsWith("cd");
  }

  private String extractPath(String input) {
    return input.trim().substring(3).trim();
  }
}
