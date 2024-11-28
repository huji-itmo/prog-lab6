package parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import parser.adapters.DateDeserializer;
import parser.adapters.DateSerializer;
import parser.adapters.LocalDateDeserializer;
import parser.adapters.LocalDateSerializer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Logger;

public class JsonFileParser<T> extends FileParser<T> {

    private final Gson gson;
    private final Class<T> classToSerialize;
    public JsonFileParser(Path filePath,Class<T> classToSerialize) {
        super(filePath);
        this.classToSerialize = classToSerialize;

        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .setPrettyPrinting()
                .create();
    }

    @Override
    public T deserializeFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(getFilePath(), StandardCharsets.UTF_8)){
            System.out.println("Opening file at " + getFilePath().toString());

//            logger.getName();
            return gson.fromJson(reader,classToSerialize);
        }
        catch (FileNotFoundException e) {
            System.err.println("File doesn't exists at " + getFilePath().toString());
            try {
                Files.createFile(getFilePath());
                System.out.println("Created new file at " + getFilePath().toString());
                return null;

            } catch (IOException ex) {
                System.err.println("Cannot create new file!" + ex.getMessage());
            }
        }
        catch (IOException e) {
            System.err.println("Error opening file. " + e.getMessage());
        }
        catch (JsonSyntaxException | JsonIOException e) {
            System.err.println(e.getMessage());
            System.err.println("Json parse error!");
        }

        System.exit(-1);
        return null;
    }

    @Override
    public void serializeIntoFile(T type)  {
        try (FileWriter writer = new FileWriter(getFilePath().toString())) {
            writer.write(gson.toJson(type, classToSerialize));
        } catch (IOException e) {
            System.err.println("Error saving file! " + e.getMessage());
        }
    }
}
