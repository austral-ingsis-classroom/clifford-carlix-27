package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class ChangeDirectory implements Operation{

    private final Optional<String> path;

    public ChangeDirectory(final Optional<String> path) {
        this.path = path;
    }

    @Override
    public Result applyTo(Directory directory) {
        if(path.isEmpty()){
            return new Error("Path is empty");
        }

        Directory startDirectory = getDirectory(directory);

        return navigatePath(startDirectory, path);

    }

    private Directory getDirectory(Directory directory) {
        return path.get().startsWith("/") ? goToRoot(directory) : directory;
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Cannot change a directory inside a file") ;
    }


    private Directory goToRoot(final Directory directory){
        Directory current = directory;
        while(current.getParent() != null){
            current = current.getParent();
        }
        return current;
    }


    private Result navigatePath(final Directory start, final Optional<String> rawPath) {
        Directory current = start;

        if (rawPath.isPresent()) {
            String[] parts = rawPath.get().split("/");

            for (String part : parts) {
                if (part.isEmpty() || part.equals(".")) {
                    continue;
                }
                if (part.equals("..")) {
                    if (current.getParent() != null) {
                        current = current.getParent();
                    } else{
                        return new Success<>("moved to directory '/'", current);
                    }
                    continue;
                }

                Optional<FileSystem> maybeFs = current.find(part);
                if (maybeFs.isEmpty() || !(maybeFs.get() instanceof Directory)) {
                    return new Error("'" + part + "' directory does not exist");
                }
                return new Error("'" + part + "' is a file, not a directory");
            }
        }
        return new Success<>("moved to directory '" + current.getName() + "'", current);
    }
}
