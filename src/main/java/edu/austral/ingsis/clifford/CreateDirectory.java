package edu.austral.ingsis.clifford;



public final class CreateDirectory implements Operation{

    @Override
    public Result applyTo(Directory directory) {
        Directory dir = new Directory(directory.getName(), directory.getParent());
        // fixme: addItem, quiza conviene directamente hacerlo aca en la clase CreateDirecotry.
        Directory newDirectory = directory.addItem(dir);
        Directory updatedHierarchy = propagateChanges(newDirectory);
        return new Success<>("'" + directory.getName() + "' directory created", updatedHierarchy);
    }

    private Directory propagateChanges(Directory changedDirectory){
        if(changedDirectory.getParent() == null){
            return changedDirectory;
        }

        // fixme: es algo rara esta logica aca. No necesariamente va a ser la misma
        Directory parent = changedDirectory.getParent();

        Directory newParent = replaceChild(parent, changedDirectory);

        return propagateChanges(newParent);
    }


    // todo: quiza habria que reevaluar la logica para este metodo
    private Directory replaceChild(Directory oldChild, Directory newChild) {
        return null;
    }



    @Override
    public Result applyTo(File file) {
        return new Error("Can't apply to a file");
    }
}
