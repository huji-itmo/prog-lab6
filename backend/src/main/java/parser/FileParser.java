package parser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Path;

@Data
@AllArgsConstructor
public abstract class FileParser<T> {
    private Path filePath;
    public abstract T deserializeFromFile();
    public abstract void serializeIntoFile(T type);
}
