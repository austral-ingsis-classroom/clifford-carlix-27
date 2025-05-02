package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemUtils {
  public static Optional<Directory> findDirectoryByName(Directory dir, String name) {
    return dir.getItems().stream()
        .filter(fs -> fs instanceof Directory)
        .map(fs -> (Directory) fs)
        .filter(d -> d.getName().equals(name))
        .findFirst();
  }

  public static Optional<FileSystem> findFileSystem(Directory dir, String name){
      if(dir.getItems().isEmpty()) return Optional.empty();

      return dir.getItems().stream()
              .filter(
                      fs -> {
                          if (fs instanceof Directory d) return d.getName().equals(name);
                          if (fs instanceof File f) return f.getName().equals(name);
                          return false;
                      })
              .findFirst();
  }




    public static Directory addDirectory(Directory dir, Directory parentDirectory){
        Directory newDirWithCorrectParent = new Directory(dir.getName(), parentDirectory);

        List<FileSystem> newItems = replaceItem(parentDirectory.getItems(), newDirWithCorrectParent);

        Directory updatedParent = new Directory(parentDirectory.getName(), parentDirectory.getParent(), newItems);

        // propagateChange da los cambios para el resto del arbol de fileSystem.
        return propagateChange(updatedParent); // devuelve el root actualizado. Pero queda ahi en root rompiendo la logica que predomina por el cd.
    }

    private static Directory propagateChange(Directory directory) {
        if (directory.getParent() == null) return directory;

        Directory parent = directory.getParent();

        // replaceitem lo que hace es actualizar los hijos del filesystem en relacion al directory que modificamos.
        List<FileSystem> newItems = replaceItem(parent.getItems(), directory);

        Directory updatedParent = new Directory(parent.getName(), parent.getParent(), newItems);

        return propagateChange(updatedParent);
    }

    public static List<FileSystem> replaceItem(List<FileSystem> items, FileSystem updatedItem) {
        return Stream.concat(
                items.stream().filter(item -> !item.getName().equals(updatedItem.getName())),
                Stream.of(updatedItem)
        ).collect(Collectors.toList());
    }




    public static Directory addFile(Directory directory, File file) {
        List<FileSystem> newItems = new ArrayList<>(directory.getItems());
        newItems.add(file);
        return new Directory(directory.getName(), directory.getParent(), newItems);
  }





}
