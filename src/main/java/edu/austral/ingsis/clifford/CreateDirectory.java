package edu.austral.ingsis.clifford;



public final class CreateDirectory implements Operation{

    @Override
    public Result applyTo(Directory directory) {
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
