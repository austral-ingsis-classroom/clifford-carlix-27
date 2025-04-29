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
    Directory current = directory;
    while (current.getParent() != null) {
      current = current.getParent();
    }
    return current;
  }

  private Result navigatePath(final Directory start, final String rawPath) {
    // todo: el current deberia ser una copia del start, y debe mantenerse inmutabilidad
    Directory current = start;

    Directory foundDirectory = null;

    String[] parts = rawPath.split("/");

    for (String part : parts) {
      if (part.isEmpty() || part.equals(".")) {
        continue;
      }
      if (part.equals("..")) {
        if (current.getParent() != null) {
          current = current.getParent();
          foundDirectory = current;
        } else {
          return new Success<>("moved to directory '/'", current);
        }
        continue;
      }

      Optional<Directory> maybeFs = FileSystemUtils.findDirectoryByName(current, part);
      if (maybeFs.isEmpty()) {
        return new Error("'" + part + "' directory does not exist");
      }

      foundDirectory = maybeFs.get();
    }

    assert foundDirectory != null;
    return new Success<>("moved to directory '" + foundDirectory.getName() + "'", foundDirectory);
  }
}
