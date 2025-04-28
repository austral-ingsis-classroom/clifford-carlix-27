package edu.austral.ingsis.clifford;

import java.util.List;

public sealed interface FileSystem permits Directory, File {
    // todo: problema en relacion a OCP.
//    List<String> listItems(); // para ls
//    FileSystem changeDirectory(String name); // para cd
//    Result createFile(String name); // para touch
//    Result createDirectory(String name); // para mkdir
//    Result remove(String name, Flag flag); // para rm
//    String printWorkingDirectory(); // para pwd
    Result apply(Operation operation);
}
