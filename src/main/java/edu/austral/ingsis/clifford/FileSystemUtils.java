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


    // fixme
    public static Directory findDirectoryByPath(Directory root, List<String> pathParts) {
      Directory current = root;

      for (String part : pathParts) {
            if (part.equals("/")) continue; // Ignoramos la raíz explícita

            boolean found = false;
            for (FileSystem item : current.getItems()) {
                if (item instanceof Directory dir && dir.getName().equals(part)) {
                    current = dir;
                    found = true;
                    break;
                }
            }

            if (!found) throw new RuntimeException("Path not found: " + String.join("/", pathParts));
        }

        return current;
    }


    public static Directory addDirectoryWithChangesInCdCommandInCdToRootCase(Directory dir, Directory root){
        Directory newRootDirWithCorrectChanges = new Directory(dir.getName(), root, dir.getItems());

        List<FileSystem> newItems = List.of(newRootDirWithCorrectChanges);

        Directory updatedRoot = new Directory(root.getName(), root.getParent(), newItems);

        return updatedRoot;
    }

    public static Directory addDirectory(Directory dir, Directory parentDirectory){
        Directory newDirWithCorrectParent = new Directory(dir.getName(), parentDirectory); // aca recibe el directory / sin cambios

        List<FileSystem> newItems = replaceItem(parentDirectory.getItems(), newDirWithCorrectParent);

        // todo: newDirWithCorrectParent. Es horace, tiene un cambio en sus elementos?
        // Directory updateParentDirectory = propagateChange(newDirWithCorrectParent);
        // al hacer esto, estoy actualizando al root con los cambios necesarios.
        // lo que si fijate que no se te produzca un stackoverflow.




        Directory updatedParent = new Directory(parentDirectory.getName(), parentDirectory.getParent(), newItems); // emily actualizado

        Directory updatedRoot = propagateChange(updatedParent);

        return updatedRoot; // root actualizado, emily actualizado
    }

    private static Directory propagateChange(Directory directory) {
        if (directory.getParent() == null) return directory;

        Directory parent = directory.getParent();

        // replaceitem lo que hace es actualizar los hijos del filesystem en relacion al directory que modificamos.
        List<FileSystem> newItems = replaceItem(parent.getItems(), directory);

        Directory updatedParent = new Directory(parent.getName(), parent.getParent(), newItems);

        return propagateChange(updatedParent);
    }

    // ese concat, no me convence nada, considero que es mutable lo que esta haciendo alli. Quiza deberia devolver un nuevo directory con los cambios realizados/
    // todo: analicemos como se comporta en relacion a lo que comente arriba. Si vemos que es posible que lleguemos a un overflow
    // todo: lo que deberia hacerse es ver una forma de crear una instancia nueva de Directory con ReplaceItem, para crear ese directory con los cambios necesarios
    // todo: de esa forma evitamos mutabilidad.
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
