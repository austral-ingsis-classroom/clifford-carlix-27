package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class ChangeDirectory implements Operation{

    private final String path;

    public ChangeDirectory(final String path) {
        this.path = path;
    }

    @Override
    public Result applyTo(Directory directory) {

        Directory startDirectory = getDirectory(directory);

        return navigatePath(startDirectory, path);

    }

    private Directory getDirectory(Directory directory) {
        return path.startsWith("/") ? goToRoot(directory) : directory;
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


    private Result navigatePath(final Directory start, final String rawPath) {
        Directory current = start;

            String[] parts = rawPath.split("/");

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

        return new Success<>("moved to directory '" + current.getName() + "'", current);
    }
}
