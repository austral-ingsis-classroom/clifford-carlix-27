package edu.austral.ingsis.clifford;


import java.util.Optional;

public final class Cd implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        String path = extractPath(input);
        Operation operation = new ChangeDirectory(Optional.of(path));
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("cd");
    }

    private String extractPath(String input) {
        return input.trim().substring(3).trim();
    }
}
