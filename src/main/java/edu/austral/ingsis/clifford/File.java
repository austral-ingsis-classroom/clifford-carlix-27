package edu.austral.ingsis.clifford;

public final class File implements FileSystem{
    private final String name;

    public File(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Result lsCommand(Flag flag) {
        return new Error("Operation not supported");
    }

    @Override
    public Result cdCommand(String directory_name) {
        return null;
    }

    @Override
    public Result touchCommand(String file_name) {
        return null;
    }

    @Override
    public Result mkDirCommand(String directory_name) {
        return null;
    }

    @Override
    public Result rmCommand(String file_or_dir_name, Flag flag) {
        return null;
    }

    @Override
    public Result pwdCommand() {
        return null;
    }
}
