package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class File implements FileSystem{
    private final String name;
    private final Directory parent;

    public File(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }



    @Override
    public Result lsCommand(Flag flag) {
        return new Error("Operation not supported");
    }

    @Override
    public Result cdCommand(String directory_name) {
        return new Error("Operation not supported");
    }

    @Override
    public Result touchCommand(String file_name) {
        if(file_name.contains("/") || file_name.contains(" ")){
            return new Error("Invalid file name: must not contain '/' or spaces");
        }

        if(getParent().find(file_name).isPresent()){
            return new Error("A file with name '" + file_name + "' already exists");
        }

        File newFile = new File(file_name, parent);
        getParent().addItem(newFile);

        return new Success<>("'" + file_name + "' file created");
    }

    @Override
    public Result mkDirCommand(String directory_name) {
        return new Error("Operation not supported");
    }

    @Override
    public Result rmCommand(String file_name, Flag flag) {
        Optional<FileSystem> match = parent.find(file_name);
        if(match.isEmpty()){
            return new Error("File '" + file_name + "' not found");
        }

        FileSystem target = match.get();

        if(target instanceof File){
            // fixme: fijate de no alterar la lista original.
            parent.getItems().remove(target);
        }

        return new Success<>("'" + file_name + "' removed");
    }

    @Override
    public Result pwdCommand() {
        // todo: Sera lo mismo para file?
        List<String> pathParts = new ArrayList<>();
        Directory current = parent;

        while(current != null){
            pathParts.add(current.getName());
            current = current.getParent();
        }

        return new Success<>(String.join("/", pathParts));
    }
}
