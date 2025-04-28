package edu.austral.ingsis.clifford;


import java.util.Optional;

public final class Rm implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Optional<Flag> maybeFlag = CommandUtils.parseFlag(input);

        if (maybeFlag.isEmpty()) { // no flag to process
            return new Success<>("");
        }

        Flag flag = maybeFlag.get();

        Operation operation = new Remove(flag);
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("rm");
    }

}
