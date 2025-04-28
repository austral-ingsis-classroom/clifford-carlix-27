package edu.austral.ingsis.clifford;

public final class Cd implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        return null;
    }

    @Override
    public boolean canExecute(String input) {
        return false;
    }
}
