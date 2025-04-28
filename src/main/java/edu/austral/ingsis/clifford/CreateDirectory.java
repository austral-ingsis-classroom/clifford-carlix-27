package edu.austral.ingsis.clifford;



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


        Directory dir = new Directory(directory.getName(), directory.getParent());
        Directory newDirectory = directory.addItem(dir);
        Directory updatedHierarchy = newDirectory.propagateChange();
        return new Success<>("'" + directory.getName() + "' directory created", updatedHierarchy);
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Cannot create a directory inside a file");
    }
}
