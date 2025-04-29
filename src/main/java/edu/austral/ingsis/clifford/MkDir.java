package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class MkDir implements Command {

  @Override
  public Result execute(String input, FileSystem fileSystem) {

    Optional<String> maybeDirectoryName = extractDirectoryName(input);

    if (maybeDirectoryName.isEmpty()) {
      return new Error("Missing directory name");
    }

    String directoryName = maybeDirectoryName.get();

    Operation operation = new CreateDirectory(directoryName);
    return fileSystem.apply(operation);
  }

  @Override
  public boolean canExecute(String input) {
    return input.trim().startsWith("mkdir");
  }

  private Optional<String> extractDirectoryName(String input) {
    String[] parts = input.trim().split("\\s+");
    if (parts.length < 2) {
      return Optional.empty();
    }
    return Optional.of(parts[1]);
  }
}
