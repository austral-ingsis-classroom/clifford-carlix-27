package edu.austral.ingsis.clifford;

public interface Operation {
  Result applyTo(Directory directory);

  Result applyTo(File file);
}
