package edu.austral.ingsis;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.Error;
import edu.austral.ingsis.clifford.Flag;
import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.Success;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();
      Directory current = new Directory("/", null);


    for (String command : commands) {
      String[] parts = command.trim().split("\\s+");
      String baseCmd = parts[0];
      String[] args = new String[parts.length - 1];
      System.arraycopy(parts, 1, args, 0, args.length);

      Result result =
          switch (baseCmd) {
            case "mkdir" -> current.mkDirCommand(args[0]);
            case "touch" -> current.touchCommand(args[0]);
            case "ls" -> {
              Flag flag = null;
              if (args.length == 1 && args[0].startsWith("--")) {
                String[] flagParts = args[0].split("=");
                flag = new Flag(flagParts[0], flagParts[1]);
              }
              yield current.lsCommand(flag);
            }
            case "cd" -> {
              String path = args.length == 0 ? "" : args[0];
              Result res = current.cdCommand(path);
              if (res instanceof Success<?> s && s.getValue() instanceof Directory dir) {
                current = dir;
              }
              yield res;
            }
            case "pwd" -> current.pwdCommand();
            case "rm" -> {
              if (args.length == 2 && args[0].startsWith("--")) {
                String[] flagParts = args[0].split("=");
                Flag flag = new Flag(flagParts[0], flagParts.length > 1 ? flagParts[1] : null);
                yield current.rmCommand(args[1], flag);
              } else if (args.length == 2) {
                Flag flag = new Flag(args[0], args[1]);
                yield current.rmCommand(args[0], flag);
              } else {
                yield current.rmCommand(args[0], null);
              }
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + baseCmd);
          };


      if (result instanceof Success<?> s && s.getValue() instanceof Directory dir) {
            current = dir;
      }

      String output = formatResult(result);
      results.add(output);
    }

    return results;
  }

  private String formatResult(Result result) {
    if (result instanceof Success<?> s) {
      return s.getMessage().toLowerCase(); // Ajustar si el mensaje en test es lowercase
    } else if (result instanceof Error e) {
      return e.getMessage().toLowerCase(); // idem
    }
    return "unknown result";
  }
}
