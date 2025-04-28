package edu.austral.ingsis.clifford;

public final class CreateFile implements Operation {
    @Override
    public Result applyTo(Directory directory) {
        return new Error("Can't apply to a directory");
    }

    @Override
    public Result applyTo(File file) {

        File newFile = new File(file.getName(), file.getParent());

        Directory dir = file.getParent();

        // todo: ver como puede ser aca el codigo de addItem
        Directory newDirectory = dir.addItem(newFile);


        // todo: el codigo de propagateChanges, es el mismo que en otros commands. fijate eso!
        Directory updatedHierarchy = dir.propagateChanges(newDirectory);

        return new Success<>("'" + file.getName() + "' file created", updatedHierarchy);
    }

}
