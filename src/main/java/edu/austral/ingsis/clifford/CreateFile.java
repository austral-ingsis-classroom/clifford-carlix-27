package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CreateFile implements Operation {

  private final String fileName;

  public CreateFile(final String fileName) {
    this.fileName = fileName;
  }

  @Override
  public Result applyTo(Directory directory) {
    if (fileName.contains("/") || fileName.contains(" ")) {
      return new Error("Invalid file name: must not contain '/' or spaces");
    }

    // fixme: findFileByName funciona mal!
    Optional<File> maybeExistingFile = FileSystemUtils.findFileByName(directory, fileName);

    if (maybeExistingFile.isPresent()) {
      return new Error("A file with name '" + fileName + "' already exists");
    }

    File newFile = new File(fileName, directory);

    Directory newDirectory = addFile(directory, newFile);

    return new Success<>("'" + newFile.getName() + "' file created", newDirectory);
  }

  @Override
  public Result applyTo(File file) {
    return new Error("");
  }

  // todo: tiralo todo en FileSystemUtils

  private Directory addFile(Directory directory, File file) {
    List<FileSystem> newItems = new ArrayList<>(directory.getItems());
    newItems.add(file);
    return new Directory(directory.getName(), directory.getParent(), newItems);
  }
}
