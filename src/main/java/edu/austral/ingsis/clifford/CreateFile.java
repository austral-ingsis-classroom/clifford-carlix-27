package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class CreateFile implements Operation {

    private final String fileName;

    public CreateFile(final String fileName) {
        this.fileName = fileName;
    }


    @Override
    public Result applyTo(Directory directory) {
        if(fileName.contains("/") || fileName.contains(" ")){
            return new Error("Invalid file name: must not contain '/' or spaces");
        }

        Optional<FileSystem> maybeExistingFile = directory.find(fileName);
        if(maybeExistingFile.isPresent()) {
            return new Error("A file with name '" + fileName + "' already exists");
        }

        File newFile = new File(fileName, directory);

        Directory newDirectory = directory.addItem(newFile);

        Directory updatedHierarchy = directory.propagateChanges(newDirectory);

        return new Success<>("'" + fileName + "' file created", updatedHierarchy);
    }

    @Override
    public Result applyTo(File file) {
        return new Error("");
    }

}
