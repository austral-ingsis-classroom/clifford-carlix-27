package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.austral.ingsis.clifford.FileSystemUtils.findFileSystem;

public final class Remove implements Operation {

  private final Flag flag;

  public Remove(final Flag flag) {
    this.flag = flag;
  }

  @Override
  public Result applyTo(Directory directory) {

    String targetName = flag.getValue();

    Optional<FileSystem> maybeTarget = findFileSystem(directory, targetName);

    if (maybeTarget.isEmpty()) {
      return new Error("Item '" + targetName + "' not found");
    }

    FileSystem target = maybeTarget.get();

    if (target instanceof Directory && !flag.getKey().equalsIgnoreCase("recursive")) {
      return new Error("Cannot remove directory '" + targetName + "' without recursive flag");
    }

    Directory updatedDirectory = removeItem(directory, target);

    return new Success<>("'" + targetName + "' removed", updatedDirectory);
  }

  @Override
  public Result applyTo(File file) {
    return new Error("Not applicable");
  }

  private Directory removeItem(final Directory directory, FileSystem target) {
    List<FileSystem> newItems = new ArrayList<>(directory.getItems());
    newItems.remove(target);
    return new Directory(directory.getName(), directory.getParent(), newItems);
  }
}
