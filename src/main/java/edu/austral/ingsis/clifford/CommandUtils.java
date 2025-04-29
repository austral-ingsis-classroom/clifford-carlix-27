package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class CommandUtils {
  private CommandUtils() {}

  public static Optional<Flag> parseFlag(String input) {
    String[] parts = input.trim().split("\\s+");
    if (parts.length < 2) return Optional.empty();

    String command = parts[0];
    String flagPart = parts[1];

    if (flagPart.contains("=")) {
      String[] keyValue = flagPart.split("=");
      if (keyValue.length < 2) return Optional.empty();

      String key = keyValue[0];
      String value = keyValue[1];

      return Optional.of(new Flag(key, value));
    }

    if (command.equals("rm") && flagPart.startsWith("--")) {
      String flagKey = flagPart.substring(2); // remove leading --
      if (parts.length >= 3) {
        String targetName = parts[2];
        return Optional.of(new Flag(flagKey, targetName));
      } else {
        return Optional.empty(); // no target name provided
      }
    }

    return Optional.empty();
  }
}
