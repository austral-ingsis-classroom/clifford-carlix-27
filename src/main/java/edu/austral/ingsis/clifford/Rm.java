package edu.austral.ingsis.clifford;


import java.util.Optional;

public final class Rm implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Optional<Flag> flag = CommandUtils.parseFlag(input);
        Operation operation = new Remove(flag);
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("rm");
    }

}
