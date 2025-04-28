package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class CommandUtils {
    private CommandUtils() {}

    public static Optional<Flag> parseFlag(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2) return Optional.empty();

        String flagPart = parts[1];
        if (!flagPart.contains("=")) return Optional.empty();

        String[] keyValue = flagPart.split("=");
        if (keyValue.length < 2) return Optional.empty();

        String key = keyValue[0];
        String value = keyValue[1];

        return Optional.of(new Flag(key, value));
    }
}
