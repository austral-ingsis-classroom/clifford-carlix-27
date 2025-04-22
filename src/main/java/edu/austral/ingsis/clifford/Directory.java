package edu.austral.ingsis.clifford;

import java.util.*;
import java.util.stream.Collectors;

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

  private Directory addItem(FileSystem item) {
    List<FileSystem> newItems = new ArrayList<>(this.items);
    newItems.add(item);
    return new Directory(this.name, this.parent, newItems);
  }

  private Directory removeItem(FileSystem item) {
    List<FileSystem> newItems = new ArrayList<>(this.items);
    newItems.remove(item);
    return new Directory(this.name, this.parent, newItems);
  }

  private Optional<FileSystem> find(String name) {
    if(items.isEmpty()) return Optional.empty();

    return items.stream()
        .filter(
            fs -> {
              if (fs instanceof Directory d) return d.getName().equals(name);
              if (fs instanceof File f) return f.getName().equals(name);
              return false;
            })
        .findFirst();
  }

  // return new Success(new Directory(this.name, this.parent, itemNames)(String.join(" "),
  // itemNames))
  @Override
  public Result lsCommand(Flag flag) {
    if (items.isEmpty()) {
      return new Success<String>("");
    }

    List<String> itemNames = getFileAndDirectoryNames();

    Result flagResult = processSortingFlag(flag, itemNames);

    if (flagResult instanceof Error) {
      return flagResult;
    }

    return new Success<String>(String.join(" ", itemNames));
  }

  private static Result processSortingFlag(Flag flag, List<String> itemNames) {
    if (flag == null) {
      return new Success<>(""); // No flag to process
    }

    if (!"--ord".equals(flag.getKey())) {
      return new Error("Unrecognized flag " + flag.getKey());
    }

    String order = flag.getValue();
    switch (order) {
      case "asc":
        itemNames.sort(Comparator.naturalOrder());
        break;
      case "desc":
        itemNames.sort(Comparator.reverseOrder());
        break;
      default:
        return new Error("Invalid value to --ord: " + order);
    }

    return new Success<>("Sorting applied");
  }

  private List<String> getFileAndDirectoryNames() {
    return items.stream()
        .map(
            fs -> {
              if (fs instanceof Directory d) return d.getName();
              else if (fs instanceof File f) return f.getName();
              else return "";
            })
        .collect(Collectors.toList());
  }

  @Override
  public Result cdCommand(String path) {
    if (path == null || path.isEmpty()) return new Error("Path is empty");

    Directory current = this;

    String[] parts = path.split("/");

    if (path.startsWith("/")) {
      while (current.getParent() != null) {
        current = current.getParent();
      }
    }

    for (String part : parts) {
      if (part.equals(".") || part.isEmpty()) {
        continue;
      } else if (part.equals("..")) {
        if (current.getParent() != null) {
          current = current.getParent();
        } else {
            // if parent == null, parent -> null. Root.
            return new Success<>("moved to directory '" + "/" + "'");
        }
      } else {
          // fixme: rompe aca con el path horace/jetta
        Optional<FileSystem> maybe = current.find(part);
        if (maybe.isEmpty()) return new Error("'" + part + "' directory does not exist");

        FileSystem fs = maybe.get();
        if (fs instanceof Directory d) {
          current = d;
        } else {
          return new Error("'" + part + "' is a file, not a directory");
        }
      }
    }

    return new Success<>("moved to directory '" + current.getName() + "'", current);
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

  @Override
  public Result rmCommand(String file_or_dir_name, Flag flag) {
    Optional<FileSystem> match = find(file_or_dir_name);

    if (match.isEmpty()) {
      return new Error("Directory not found: " + file_or_dir_name);
    }

    FileSystem target = match.get();


    if (target instanceof Directory) {
      if (flag == null) {
        return new Error("Cannot remove '" + file_or_dir_name + "', is a directory");
      }
    }

    Directory newDirectory = this.removeItem(target);

    Directory updatedHierarchy = propagateChanges(newDirectory);

    // todo: chequeate esto pero despues por las dudas, fijate el test8.
    if (target instanceof File) {
      Directory newDirectoryWithFile = this.removeItem(target);
      Directory updatedHierarchyWithFile = propagateChanges(newDirectoryWithFile);
      return new Success<>("'" + file_or_dir_name + "' removed", updatedHierarchyWithFile);
    }

    return new Success<>("'" + file_or_dir_name + "' removed", updatedHierarchy);
  }

  private Directory propagateChanges(Directory changedDirectory) {
    if (changedDirectory.getParent() == null) {
      return changedDirectory;
    }

    Directory parent = changedDirectory.getParent();

    Directory newParent = parent.replaceChild(this, changedDirectory);

    return propagateChanges(newParent);
  }

  private Directory replaceChild(Directory oldChild, Directory newChild) {
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

  @Override
  public Result pwdCommand() {
    List<String> pathParts = new ArrayList<>();
    Directory current = this;

    while (current != null) {
      pathParts.add(current.getName());
      current = current.getParent();
    }


    return new Success<>("/" + String.join("/", pathParts));
  }
}
