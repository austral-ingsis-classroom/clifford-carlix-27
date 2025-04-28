package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class Remove implements Operation{

    private final Flag flag;

    public Remove(final Flag flag) {
        this.flag = flag;
    }


    @Override
    public Result applyTo(Directory directory) {

        String targetName = flag.getValue();
        Optional<FileSystem> maybeTarget = directory.find(targetName);

        if (maybeTarget.isEmpty()) {
            return new Error("Item '" + targetName + "' not found");
        }

        FileSystem target = maybeTarget.get();

        if (target instanceof Directory && !flag.getKey().equalsIgnoreCase("recursive")) {
            return new Error("Cannot remove directory '" + targetName + "' without recursive flag");
        }

        Directory updatedDirectory = directory.removeItem(target);
        Directory updatedHierarchy = updatedDirectory.propagateChange();

        return new Success<>("'" + targetName + "' removed", updatedHierarchy);
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Remove not implemented yet");
    }
}
