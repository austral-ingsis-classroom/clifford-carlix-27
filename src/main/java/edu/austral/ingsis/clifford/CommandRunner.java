package edu.austral.ingsis.clifford;

import java.util.List;

public final class CommandRunner {
  private final List<Command> commands;

  public CommandRunner(List<Command> commands) {
    this.commands = List.copyOf(commands);
  }

  public Result run(String input, FileSystem fs) {
    for (Command cmd : commands) {
      if (cmd.canExecute(input)) {
        return cmd.execute(input, fs);
      }
    }
    return new Error("Unknown command: " + input);
  }
}
