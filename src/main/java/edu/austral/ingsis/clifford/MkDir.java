package edu.austral.ingsis.clifford;

public final class MkDir implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Operation operation = new CreateDirectory();
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("mkdir");
    }
}
