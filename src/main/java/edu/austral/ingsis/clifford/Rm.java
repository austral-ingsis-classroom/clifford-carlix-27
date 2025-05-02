package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class Rm implements Command {

  @Override
  public Result execute(String input, FileSystem fileSystem) {
    Optional<Flag> maybeFlag = CommandUtils.parseFlag(input);

    // si tiene flag, no hace falta este chequeo, hacer maybeFlag.get().getValue()
    String[] tokens = input.trim().split("\\s+");
    if (tokens.length < 2) {
      return new Error("No item name provided");
    }
    String targetName = tokens[tokens.length - 1];

    if (maybeFlag.isEmpty()) {
      // no hay flag, entonces si el target es un directorio debemos mostrar el mensaje de error
      // adecuado
      if (fileSystem instanceof Directory dir) {
          Optional<Directory> maybeTarget = FileSystemUtils.findDirectoryByName(dir, targetName);
        if (maybeTarget.isPresent()) {
          return new Success<>("cannot remove '" + targetName + "', is a directory");
        }

        // si es un file, se puede remover sin flag, delegamos a Remove. Pasamos un target que
        // representa el file a borrar.
        return new Remove(new Flag("target", targetName)).applyTo(dir);
      } else {
        return new Error("Cannot remove from a file");
      }
    }

    Flag flag = maybeFlag.get();

    Operation operation = new Remove(flag);
    return fileSystem.apply(operation);
  }

  @Override
  public boolean canExecute(String input) {
    return input.trim().startsWith("rm");
  }
}
