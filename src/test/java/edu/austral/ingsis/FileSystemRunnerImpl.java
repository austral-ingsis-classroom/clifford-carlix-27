package edu.austral.ingsis;

import edu.austral.ingsis.clifford.Error;
import edu.austral.ingsis.clifford.*;

import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

  private final CommandRunner commandRunner;

  private Directory root;

  private Directory current;

  private List<String> currentPath;

    public FileSystemRunnerImpl(List<Command> availableCommands) {
    this.commandRunner = new CommandRunner(availableCommands);
    this.root = new Directory("/", null);
    this.current = root;
    this.currentPath = List.of("/");
  }

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();

    for (String commandString : commands) {
      Result result = commandRunner.run(commandString, current);

      // el success tiene el directory correcto con las modificaciones correctas. Digamos, el success.value tiene los cambios correctos.
      if (result instanceof Success<?> success) {
        Object value = success.getValue();

          if (value instanceof Directory updatedDir) {
              String trimmedCommand = commandString.trim();

              if (trimmedCommand.equals("cd ..")) {
                  // actualizo el current al dir con las correcciones correctas
                  this.current = updatedDir;
              } else if (trimmedCommand.equals("cd /")){
                  this.current = root;
              } else if (updatedDir.getParent() == null ) {
                  // Root fue modificado por mkdir, rm, etc.
                  this.root = updatedDir;
                  this.current = FileSystemUtils.findDirectoryByPath(root, currentPath);
              } else {
                  // Cambio de directorio normal (cd <dir>)
                  this.current = updatedDir;
                  this.currentPath = updatedDir.getAbsolutePath();
              }

          }
      }


      results.add(formatResult(result));
    }

    return results;
  }

  private String formatResult(Result result) {
    if (result instanceof Success<?> success) {
      return success.getMessage();
    } else if (result instanceof Error error) {
      return error.getMessage();
    }
    return "";
  }
}
