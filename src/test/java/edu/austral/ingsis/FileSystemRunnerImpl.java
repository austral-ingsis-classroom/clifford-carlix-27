//package edu.austral.ingsis;
//
//import edu.austral.ingsis.clifford.*;
//import edu.austral.ingsis.clifford.Error;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FileSystemRunnerImpl implements FileSystemRunner {
//
//  @Override
//  public List<String> executeCommands(List<String> commands) {
//    List<String> results = new ArrayList<>();
//    Directory current = new Directory("/", null);
//
//    for (String command : commands) {
//      String[] parts = command.trim().split("\\s+");
//      String baseCmd = parts[0];
//      String[] args = new String[parts.length - 1];
//      System.arraycopy(parts, 1, args, 0, args.length);
//
//
//      List<Command> commands1 = new ArrayList<Command>();
//
//      for(Command command1: commands1){
//
//          // todo: quiza por ahora que el command tenga la responsabilidad de ejecutarse no tiene sentido. Pero por ahora centremonos en esto.
////          if(command1.canExecute()){
////
////              command1.execute();
////          }
//
//
//      }
//
////      if (result instanceof Success<?> s && s.getValue() instanceof Directory dir) {
////        current = dir;
////      }
////
////      String output = formatResult(result);
////      results.add(output);
////    }
//
//    return results;
//  }
//
//  private String formatResult(Result result) {
//    if (result instanceof Success<?> s) {
//      return s.getMessage().toLowerCase(); // Ajustar si el mensaje en test es lowercase
//    } else if (result instanceof Error e) {
//      return e.getMessage().toLowerCase(); // idem
//    }
//    return "unknown result";
//  }
//}
