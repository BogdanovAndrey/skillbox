import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * A {@code FileVisitor} that copies a file-tree ("cp -r")
 */
class TreeCopier implements FileVisitor<Path> {
    private final Path source;
    private final Path target;


    TreeCopier(Path source, Path target) {
        this.source = source;
        this.target = target;

    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        // before visiting entries in a directory we copy the directory
        // (okay if directory already exists).

        Path newdir = target.resolve(source.relativize(dir));
        try {
            Files.copy(dir, newdir, COPY_ATTRIBUTES);
        } catch (FileAlreadyExistsException x) {
            // ignore
        } catch (IOException x) {
            System.out.format("Не удалось создать: %s: %s%n", newdir, x);
            return SKIP_SUBTREE;
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        copyFile(file, target.resolve(source.relativize(file)));
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        // fix up modification time of directory when done
        if (exc == null) {
            Path newdir = target.resolve(source.relativize(dir));
            try {
                FileTime time = Files.getLastModifiedTime(dir);
                Files.setLastModifiedTime(newdir, time);
            } catch (IOException x) {
                System.err.format("Невозможно скопировать все атрибуты: %s: %s%n", newdir, x);
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        if (exc instanceof FileSystemLoopException) {
            System.err.println("cycle detected: " + file);
        } else {
            System.out.format("Не удалось скопировать: %s: %s%n", file, exc);
        }
        return CONTINUE;
    }

    static void copyFile(Path source, Path target) {
        if (Files.notExists(target)) {
            try {
                Files.copy(source, target, COPY_ATTRIBUTES);
            } catch (IOException x) {
                System.out.format("Не удалось скопировать: %s: %s%n", source, x);
            }
        } else {
            System.out.format("Не удалось скопировать: %s", source);
        }
    }
}