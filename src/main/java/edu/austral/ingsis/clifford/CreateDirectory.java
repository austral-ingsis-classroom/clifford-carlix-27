package edu.austral.ingsis.clifford;


import java.util.ArrayList;
import java.util.List;

public final class CreateDirectory implements Operation{

    private final String directoryName;

    public CreateDirectory(final String directoryName) {
        this.directoryName = directoryName;
    }

    @Override
    public Result applyTo(Directory directory) {
        if(directoryName.contains("/") || directoryName.contains(" ")) {
            return new Error("Invalid directory name: must not contain '/' or spaces");
        }

        Directory newDir = new Directory(directoryName, directory);

        Directory updatedDirectory = addDirectory(newDir, directory);

        return new Success<>("'" + newDir.getName() + "' directory created", updatedDirectory);
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Cannot create a directory inside a file");
    }

    // todo: tiralo todo en FileSystemUtils

    private Directory addDirectory(Directory dir, Directory parentDirectory) {
        List<FileSystem> newItems = new ArrayList<>(parentDirectory.getItems());
        newItems.add(dir);
        return new Directory(parentDirectory.getName(), parentDirectory.getParent(), newItems);
    }

}
