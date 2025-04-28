package edu.austral.ingsis.clifford;

public final class Touch implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Operation operation = new CreateFile();
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("touch");
    }
}
