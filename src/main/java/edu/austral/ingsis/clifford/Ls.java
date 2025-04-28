package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class Ls implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Optional<Flag> flag = parseFlag(input);
        Operation operation = new ListItems(flag);
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("ls");
    }

    private static Optional<Flag> parseFlag(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2) return Optional.empty();

        String flagPart = parts[1];
        if (!flagPart.contains("=")) return Optional.empty();

        String[] keyValue = flagPart.split("=");
        String key = keyValue[0];
        String value = keyValue[1];

        return Optional.of(new Flag(key, value));
    }
}
