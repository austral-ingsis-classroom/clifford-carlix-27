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

      if (result instanceof Success<?> success) {
        Object value = success.getValue();

          if (value instanceof Directory updatedDir) {
              // Detectamos si el root cambio (ej: mkdir, rm, etc.)
              if (updatedDir.getParent() == null) {
                  this.root = updatedDir;
                  this.current = new FileSystemUtils().findDirectoryByPath(root, currentPath);
              } else {
                  // Si fue un cambio de directorio, actualizamos current y el path
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
