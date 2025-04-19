package edu.austral.ingsis.clifford;

public sealed interface FileSystem permits Directory, File {
  // Success | Error
  Result lsCommand(Flag flag);

  Result cdCommand(String directory_name);

  Result touchCommand(String file_name);

  Result mkDirCommand(String directory_name);

  Result rmCommand(String file_or_dir_name, Flag flag);

  Result pwdCommand();
}
