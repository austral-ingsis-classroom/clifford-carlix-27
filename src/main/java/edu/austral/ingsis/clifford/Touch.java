package edu.austral.ingsis.clifford;

public final class Touch implements Command {

  @Override
  public Result execute(String input, FileSystem fileSystem) {
    String fileName = extractFileName(input);
    if (fileName.isEmpty()) {
      return new Error("file name is empty");
    }
    Operation operation = new CreateFile(fileName);
    return fileSystem.apply(operation);
  }

  @Override
  public boolean canExecute(String input) {
    return input.trim().startsWith("touch");
  }

  private String extractFileName(String input) {
    return input.trim().substring(6).trim();
  }
}
