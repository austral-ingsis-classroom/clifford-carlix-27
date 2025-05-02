package edu.austral.ingsis.clifford;


public class PrintWorkingDirectory implements Operation {

  @Override
  public Result applyTo(Directory directory) {
    String path = buildPath(directory);
    return new Success<>(path);
  }

  @Override
  public Result applyTo(File file) {
    return new Error("Can't apply to a file");
  }

  private String buildPath(Directory directory) {
      if(directory.getParent() == null){
          return "/";
      }
      String parentPath = buildPath(directory.getParent());
      return parentPath.equals("/")
              ? parentPath + directory.getName()
              : parentPath + "/" + directory.getName();
  }
}
