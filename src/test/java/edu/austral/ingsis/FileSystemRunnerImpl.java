package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import edu.austral.ingsis.clifford.Error;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

  private final CommandRunner commandRunner;

  private Directory current;

  public FileSystemRunnerImpl(List<Command> availableCommands) {
    this.commandRunner = new CommandRunner(availableCommands);
    this.current = new Directory("/", null);
  }

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();

    for (String commandString : commands) {
      Result result = commandRunner.run(commandString, current);

      if (result instanceof Success<?> success && success.getValue() instanceof Directory dir) {
        current = dir;
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
