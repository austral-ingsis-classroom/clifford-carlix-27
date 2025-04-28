package edu.austral.ingsis.clifford;

import java.util.Optional;

public final class Remove implements Operation{

    private final Optional<Flag> flag;

    public Remove(final Optional<Flag> flag) {
        this.flag = flag;
    }

    // TODO() -> considero que hay que cambiar la logica con la que se maneja
    @Override
    public Result applyTo(Directory directory) {
        return null;
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Remove not implemented yet");
    }
}
