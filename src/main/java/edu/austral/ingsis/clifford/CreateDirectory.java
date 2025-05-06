package edu.austral.ingsis.clifford;

public final class CreateDirectory implements Operation {

  private final String directoryName;

  public CreateDirectory(final String directoryName) {
    this.directoryName = directoryName;
  }

  @Override
  public Result applyTo(Directory directory) {
    if (directoryName.contains("/") || directoryName.contains(" ")) {
      return new Error("Invalid directory name: must not contain '/' or spaces");
    }


     Directory newDir = new Directory(directoryName, directory);

     Directory updatedDirectory = FileSystemUtils.addDirectory(newDir, directory);

     // el updatedDirectory. Tiene tanto a horace como a jetta como subdirectorios.

     return new Success<>("'" + newDir.getName() + "' directory created", updatedDirectory);
  }

  @Override
  public Result applyTo(File file) {
    return new Error("Cannot create a directory inside a file");
  }

}
