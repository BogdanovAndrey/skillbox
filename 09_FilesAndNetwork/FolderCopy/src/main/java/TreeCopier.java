
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

class TreeCopier implements FileVisitor<Path> {
    private final Path source;
    private final Path target;


    TreeCopier(Path source, Path target) {
        this.source = source;
        this.target = target;

    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        Path newDir = target.resolve(source.relativize(dir));
        try {
            Files.copy(dir, newDir, COPY_ATTRIBUTES);
        } catch (FileAlreadyExistsException ignored) {
            //игнорируем
        } catch (IOException x) {
            System.out.format("Не удалось создать: %s: %s%n", newDir, x);
            return SKIP_SUBTREE;
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        Path newFile = target.resolve(source.relativize(file));
        //Проверим на наличие файла в конечной папке
        if (Files.notExists(newFile)) {
            try {
                Files.copy(file, newFile, COPY_ATTRIBUTES);
            } catch (IOException x) {
                System.out.format("Не удалось скопировать: %s: %s%n", source, x);
            }
        } else {
            System.out.format("Не удалось скопировать: %s%n", source);
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.out.format("Не удалось скопировать: %s: %s%n", file, exc);
        return CONTINUE;
    }

}