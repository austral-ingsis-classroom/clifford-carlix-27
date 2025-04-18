package edu.austral.ingsis.clifford;

public final class Success<T> implements Result{

    private final T value;

    public Success(final T value) {
        this.value = value;
    }


}
