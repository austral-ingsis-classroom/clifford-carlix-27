package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class Ls implements Command{

    @Override
    public Result execute(String input, FileSystem fileSystem) {
        Optional<Flag> maybeFlag = CommandUtils.parseFlag(input);

        if(maybeFlag.isEmpty()){
            return new Error("Flag missing");
        }

        Flag flag = maybeFlag.get();

        Operation operation = new ListItems(flag);
        return fileSystem.apply(operation);
    }

    @Override
    public boolean canExecute(String input) {
        return input.trim().startsWith("ls");
    }
}
