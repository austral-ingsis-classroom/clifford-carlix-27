package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class PrintWorkingDirectory implements Operation{

    @Override
    public Result applyTo(Directory directory) {
        List<String> pathParts = new ArrayList<>();
        Directory current = directory;
        while (current != null) {
            pathParts.add(current.getName());
            current = current.getParent();
        }
        return new Success<>("/" + String.join("/", pathParts));
    }

    @Override
    public Result applyTo(File file) {
        return new Error("Can't apply to a file");
    }
}
