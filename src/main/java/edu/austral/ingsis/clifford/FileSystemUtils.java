package edu.austral.ingsis.clifford;

import java.util.Optional;

public class FileSystemUtils {
    public static Optional<Directory> findDirectoryByName(Directory dir, String name) {
        return dir.getItems().stream()
                .filter(fs -> fs instanceof Directory)
                .map(fs -> (Directory) fs)
                .filter(d -> d.getName().equals(name))
                .findFirst();
    }

    public static Optional<File> findFileByName(Directory dir, String name) {
        return dir.getItems().stream()
                .filter(fs -> fs instanceof File)
                .map(fs -> (File) fs)
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }

    // todo: cuando traigas los metodos aca, anda generalizando comportamiento.
    //  Por ahora eso es secundario, pero para que lo tengas en cuenta!

}
