package edu.austral.ingsis.clifford;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ListItems implements Operation{

    private final Optional<Flag> flag;

    public ListItems(Optional<Flag> flag) {
        this.flag = flag;
    }

    @Override
    public Result applyTo(Directory directory) {
        List<FileSystem> items = directory.getItems();

        List<String> itemNames = getFileAndDirectoryNames(items);

        Result flagResult = processSortingFlag(flag, itemNames);

        if(flagResult instanceof Error){ return flagResult; }

        return new Success<>(String.join(" ", itemNames));
    }

    private List<String> getFileAndDirectoryNames(List<FileSystem> items) {
        return items.stream()
                .map(
                     fs -> {
                            if (fs instanceof Directory d) return d.getName();
                            else if (fs instanceof File f) return f.getName();
                            else return "";
                     }
                )
                .collect(Collectors.toList());
    }

    private static Result processSortingFlag(Optional<Flag> flag, List<String> itemNames) {
        if (flag.isEmpty()) {
            return new Success<>(""); // No flag to process
        }

        if (!"--ord".equals(flag.get().getKey())) {
            return new Error("Unrecognized flag " + flag.get().getKey());
        }

        String order = flag.get().getValue();
        switch (order) {
            case "asc":
                itemNames.sort(Comparator.naturalOrder());
                break;
            case "desc":
                itemNames.sort(Comparator.reverseOrder());
                break;
            default:
                return new Error("Invalid value to --ord: " + order);
        }

        return new Success<>("Sorting applied");
    }


    @Override
    public Result applyTo(File file) {
        return new Error("Can't apply to a file");
    }
}
