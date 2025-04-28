package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Directory implements FileSystem {
  private final String name;
  private final Directory parent;
  private final List<FileSystem> items;

  // Constructor for items
  public Directory(String name, Directory parent, List<FileSystem> items) {
    this.name = name;
    this.parent = parent;
    this.items = new ArrayList<>(items);
  }

  // Constructor for an empty directory
  public Directory(String name, Directory parent) {
    this(name, parent, new ArrayList<>());
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

  public Directory addItem(FileSystem item) {
    List<FileSystem> newItems = new ArrayList<>(this.items);
    newItems.add(item);
    return new Directory(this.name, this.parent, newItems);
  }

  public Directory removeItem(FileSystem item) {
    List<FileSystem> newItems = new ArrayList<>(this.items);
    newItems.remove(item);
    return new Directory(this.name, this.parent, newItems);
  }

  public Optional<FileSystem> find(String name) {
    if (items.isEmpty()) return Optional.empty();

    return items.stream()
        .filter(
            fs -> {
              if (fs instanceof Directory d) return d.getName().equals(name);
              if (fs instanceof File f) return f.getName().equals(name);
              return false;
            })
        .findFirst();
  }

    public Directory propagateChange() {
        if (parent == null) {
            return this;
        }
        Directory newParent = parent.replaceChild(this, this);
        return newParent.propagateChange();
    }


  @Override
  public Result apply(Operation operation) {
      return operation.applyTo(this);
  }


  @Override
  public Result touchCommand(String file_name) {
    if (file_name.contains("/") || file_name.contains(" ")) {
      return new Error("Invalid file name: must not contain '/' or spaces");
    }

    if (find(file_name).isPresent()) {
      return new Error("A file with name '" + file_name + "' already exists");
    }

    File newFile = new File(file_name, parent);

    Directory newDirectory = this.addItem(newFile);

    Directory updatedHierarchy = propagateChanges(newDirectory);

    return new Success<>("'" + file_name + "' file created", updatedHierarchy);
  }

  @Override
  public Result mkDirCommand(String directory_name) {
    if (directory_name.contains("/") || directory_name.contains(" ")) {
      return new Error("Invalid directory name: must not contain '/' or spaces");
    }

    if (find(directory_name).isPresent()) {
      return new Error("A directory with name '" + directory_name + "' already exists");
    }

    Directory newDir = new Directory(directory_name, this.parent);

    Directory newDirectory = addItem(newDir);

    Directory updatedHierarchy = propagateChanges(newDirectory);

    return new Success<>("'" + directory_name + "' directory created", updatedHierarchy);
  }

  
  public Directory propagateChanges(Directory changedDirectory) {
    if (changedDirectory.getParent() == null) {
      return changedDirectory;
    }

    Directory parent = changedDirectory.getParent();

    Directory newParent = parent.replaceChild(this, changedDirectory);

    return propagateChanges(newParent);
  }

  public Directory replaceChild(Directory oldChild, Directory newChild) {
    List<FileSystem> newItems = new ArrayList<>();

    for (FileSystem item : this.items) {
      if (item.equals(oldChild)) {
        newItems.add(newChild);
      } else {
        newItems.add(item);
      }
    }
    return new Directory(this.name, this.parent, newItems);
  }

}
