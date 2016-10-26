package linenux.util;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import linenux.command.result.CommandResult;

/**
 *
 */
//@@author A0135788M
public class FileUtil {

    public static Either<Boolean, CommandResult> doesFileExist(String pathString) {
        Either<Path, CommandResult> path = getPathFromString(pathString);

        if (path.isRight()) {
            return Either.right(path.getRight());
        }
        try {
            return Either.left(Files.exists(path.getLeft()));
        } catch (SecurityException e) {
            return Either.right(makeNoPermissionsResult());
        }
    }

    public static Either<Path, CommandResult> getPathFromString(String pathString) {
        try {
            return Either.left(Paths.get(pathString));
        } catch (InvalidPathException e) {
            return Either.right(makeInvalidPathResult());
        }
    }

    private static CommandResult makeInvalidPathResult() {
        return () -> "String contains invalid characters.";
    }

    private static CommandResult makeNoPermissionsResult() {
        return () -> "Do not have permissions to read the file.";
    }
}
