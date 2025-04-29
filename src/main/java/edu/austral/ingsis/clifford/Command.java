package edu.austral.ingsis.clifford;

public sealed interface Command permits Cd, Ls, MkDir, Pwd, Rm, Touch {
  Result execute(String input, FileSystem fileSystem);

  boolean canExecute(String input);
}
