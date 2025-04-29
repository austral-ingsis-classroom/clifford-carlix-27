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

        Directory newDir = new Directory(directoryName, directory); // el directorio que agrego

        Directory updatedDirectory = addDirectory(newDir, directory); // refleja los cambios

        Directory updatedHierarchy = propagateChanges(updatedDirectory); // contiene el estado de los directorios actuales

        return new Success<>("'" + newDir.getName() + "' directory created", updatedHierarchy);
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Cannot create a directory inside a file");
    }

    private Directory addDirectory(Directory dir, Directory parentDirectory) {
        List<FileSystem> newItems = new ArrayList<>(parentDirectory.getItems());
        newItems.add(dir);
        return new Directory(parentDirectory.getName(), parentDirectory.getParent(), newItems);
    }

    private Directory propagateChanges(Directory updatedChild) {
        Directory parent = updatedChild.getParent();

        if (parent == null) {
            return updatedChild;
        }

        List<FileSystem> newParentItems = new ArrayList<>();

        for (FileSystem item : parent.getItems()) {
            if (item instanceof Directory dir && dir.getName().equals(updatedChild.getName())) {
                newParentItems.add(updatedChild);
            } else {
                newParentItems.add(item);
            }
        }

        Directory updatedParent = new Directory(parent.getName(), parent.getParent(), newParentItems);

        return propagateChanges(updatedParent);
    }

}
