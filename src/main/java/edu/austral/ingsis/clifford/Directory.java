package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Directory implements FileSystem{
    private final String name;
    private final Directory parent; //
    private final List<FileSystem> items;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }

    public List<FileSystem> getItems() {
        return items;
    }

    public void addItem(FileSystem item) {
        items.add(item);
    }

    public Optional<FileSystem> find(String name) {
        return items.stream()
                .filter(fs -> {
                    if (fs instanceof Directory d) return d.getName().equals(name);
                    if (fs instanceof File f) return f.getName().equals(name);
                    return false;
                }).findFirst();
    }

    @Override
    public Result lsCommand(Flag flag) {
        if(items.isEmpty()) {
            return new Success<String>("");
        }

        // Es una lista de FileSystem (items) porque puede ser tanto directorios como files
        // output solo contiene los nombres de todos estos, como String y luego con la logica posterior se ordenarian
        // todo: Hace que el nombre sea mas explicito de lo que esta haciendo
        List<String> output = getOutput();


        // i dont like this logic. its garbage for this method. todo: (Extract to other private method)
        if(flag != null){
            if("--ord".equals(flag.getKey())){
                String order = flag.getValue();
                if("asc".equals(order)){
                    output.sort(Comparator.naturalOrder());
                } else if("desc".equals(order)){
                    output.sort(Comparator.reverseOrder());
                } else{
                    return new Error("Invalid value to --ord: " + order);
                }
            } else{
                return new Error("Unrecognized flag " + flag.getKey());
            }
        }

        return new Success<String>(String.join(" ", output));
    }

    private List<String> getOutput() {
        return items.stream()
                .map(fs -> {
                    if (fs instanceof Directory d) return d.getName();
                    else if (fs instanceof File f) return f.getName();
                    else return "";
                })
                .collect(Collectors.toList());
    }

    @Override
    public Result cdCommand(String path) {
        if(path == null || path.isEmpty()) return new Error("Path is empty");

        Directory current = this;

        String[] parts = path.split("/");

        if(path.startsWith("/")){
            while(current.getParent() != null){
                current = current.getParent();
            }
        }

        for (String part : parts) {
            if (part.equals(".") || part.isEmpty()) {
                continue;
            } else if (part.equals("..")) {
                if (current.getParent() != null) {
                    current = current.getParent();
                } // si ya estás en root, quedate
            } else {
                // fixme: is absolutely necessary find method?
                Optional<FileSystem> maybe = current.find(part);
                if (maybe.isEmpty()) return new Error("Directory not found: " + part);

                FileSystem fs = maybe.get();
                if (fs instanceof Directory d) {
                    current = d;
                } else {
                    return new Error("'" + part + "' is a file, not a directory");
                }
            }
        }

        return new Success<>("Moved to directory: '" + current.getName() + "'");

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
        if(directory_name.contains("/") || directory_name.contains(" ")){
            return new Error("Invalid directory name: must not contain '/' or spaces");
        }

        if(find(directory_name).isPresent()){
            return new Error("A directory with name '" + directory_name + "' already exists");
        }

        Directory newDir = new Directory(directory_name, this);
        this.items.add(newDir);

        return new Success<>("'" + newDir.getName() + "' directory created");
    }

    @Override
    public Result rmCommand(String file_or_dir_name, Flag flag) {
        Optional<FileSystem> match = find(file_or_dir_name);

        if(match.isEmpty()){
            return new Error("Directory not found: " + file_or_dir_name);
        }

        FileSystem target = match.get();

        if(target instanceof Directory){
            if(flag == null || "--recursive".equals(flag.getKey())){
                return new Error("Cannot remove directory '"+ file_or_dir_name + "' without --recursive" );
            }
        }

        if(target instanceof File){
            // fixme: fijate de no alterar la lista original.
            parent.getItems().remove(target);
        }

        // fixme: estoy alterando los items originales. Quiza conviene crear una nueva instancia de items.
        items.remove(target);
        return new Success<>("'" + file_or_dir_name + "' removed");
    }

    @Override
    public Result pwdCommand() {
        List<String> pathParts = new ArrayList<>();
        Directory current = this;

        while(current != null){
            pathParts.add(current.getName());
            current = current.getParent();
        }

        return new Success<>(String.join("/", pathParts));
    }
}
