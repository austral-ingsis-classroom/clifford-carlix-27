package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class ChangeDirectory implements Operation {

  private final String path;

  public ChangeDirectory(final String path) {
    this.path = path;
  }

  @Override
  public Result applyTo(Directory directory) {

      // arranca con el directory correcto. El que tiene todos los cambios
    Directory startDirectory = getDirectory(directory);

    return navigatePath(startDirectory, path);
  }

  private Directory getDirectory(Directory directory) {
      // el directory que recibo en el parametro con el que arranca es el correcto el que tiene todos los cambios
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
    Directory rootToUpdate = new Directory("/", null);
    String[] parts = rawPath.split("/");

    Optional<Directory> result = Optional.of(start);

    for (String part : parts) {
          if (part.isEmpty() || part.equals(".")) {
              continue;
          }

        result = result.map(current -> {
            if (part.equals("..")) {
                return FileSystemUtils.addDirectoryWithChangesInCdCommandInCdToRootCase(start, rootToUpdate);
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
