package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class ChangeDirectory implements Operation {

  private final String path;

  public ChangeDirectory(final String path) {
    this.path = path;
  }

  @Override
  public Result applyTo(Directory directory) {

    Directory startDirectory = getDirectory(directory);

    return navigatePath(startDirectory, path);
  }

  private Directory getDirectory(Directory directory) {
    return path.startsWith("/") ? goToRoot(directory) : directory;
  }

  @Override
  public Result applyTo(File file) {
    return new Error("'" + file.getName() + "' is a file, not a directory");
  }

  private Directory goToRoot(final Directory directory) {
      return directory.getParent() == null
              ? directory
              : goToRoot(directory.getParent());
  }

  private Result navigatePath(final Directory start, final String rawPath) {
    String[] parts = rawPath.split("/");

    Optional<Directory> result = Optional.of(start);

    for (String part : parts) {
          if (part.isEmpty() || part.equals(".")) {
              continue;
          }

        result = result.map(current -> {
            if (part.equals("..")) {
                return current.getParent() != null ? current.getParent() : current;
            } else {
                return FileSystemUtils.findDirectoryByName(current, part).orElse(null);
            }
        });

          if (result.isEmpty()) {
              return new Error("'" + part + "' directory does not exist");
          }
    }

    Directory foundDirectory = result.get();

    return new Success<>("moved to directory '" + foundDirectory.getName() + "'", foundDirectory);
  }
}
