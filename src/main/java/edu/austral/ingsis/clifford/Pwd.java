package edu.austral.ingsis.clifford;

public final class Pwd implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Operation operation = new PrintWorkingDirectory();
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("pwd");
    }
}
