package edu.austral.ingsis.clifford;

public sealed interface FileSystem permits Directory, File {
    Result apply(Operation operation);
}
