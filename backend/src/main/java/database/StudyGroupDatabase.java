package database;

import dataStructs.*;
import database.exceptions.PrimaryKeyIsNotUnique;
import lombok.Getter;
import lombok.Setter;
import parser.FileParser;
import parser.StudyGroupList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
public class StudyGroupDatabase extends CollectionDatabase<StudyGroup, Long> {

    private long lastId = 0;

    private final FileParser<parser.StudyGroupList> parser;

    Map<Integer, Stack<UndoLog<StudyGroup>>> undoLogStacksByClient = new HashMap<>();

    @Setter
    private int currentClient = 0;

    @Override
    public void clear() {
        List<StudyGroup> deleted = List.copyOf(getCollection());

        super.clear();

        pushToUndoStack(UndoLog.deletedElements(deleted));
        lastId = 0;
    }

    @Override
    public Comparator<Long> getPrimaryKeyComparator() {
        return Long::compareTo;
    }

    @Override
    public Function<StudyGroup, Long> getPrimaryKey() {
        return StudyGroup::getId;
    }

    @Override
    public BiConsumer<StudyGroup, Long> setPrimaryKeyTo() {
        return StudyGroup::setId;
    }

    @Override
    public Class<StudyGroup> getElementClass() {
        return StudyGroup.class;
    }

    @Override
    public String getInfo() {

        StringBuilder builder = new StringBuilder(super.getInfo());

        Path pathToFile = parser.getFilePath();

        if (!Files.exists(pathToFile)) {
            builder.append("\nFile problem: file doesn't exists.");

            return builder.toString();
        }

        BasicFileAttributes attributes;
        try {
            attributes = Files.readAttributes(pathToFile, BasicFileAttributes.class);
        } catch (IOException e) {
            builder.append("\nFile problem: ").append(e.getMessage());

            return builder.toString();
        }

        builder.append("Initialization date: ").append(Date.from(attributes.creationTime().toInstant()))
                .append("\nLast update date: ").append(Date.from(attributes.lastModifiedTime().toInstant()));

        return builder.toString();
    }

    @Override
    public List<StudyGroup> removeGreaterOrLowerThanPrimaryKey(Long id, boolean greater) {
        List<StudyGroup> deleted = super.removeGreaterOrLowerThanPrimaryKey(id, greater);

        pushToUndoStack(UndoLog.deletedElements(deleted));

        return deleted;
    }

    @Override
    public StudyGroup updateElementByPrimaryKey(Long id, StudyGroup new_element) throws IllegalArgumentException {
        StudyGroup deleted = super.updateElementByPrimaryKey(id, new_element);

        pushToUndoStack(UndoLog.changedElement(new_element,deleted));

        return deleted;
    }

    public StudyGroupDatabase(FileParser<parser.StudyGroupList> parser) {

        super(new ArrayDeque<>());
        this.parser = parser;

        try {
            deserialize();
            getCollection().forEach(group -> {
                try {
                    group.checkValues();
                }catch (RuntimeException e) {
                    System.err.println("In element with id: " + group.getId());
                    throw e;
                }
            });

            lastId = checkAllUnique() + 1;

        } catch (IOException | RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    public void serialize() {
        parser.serializeIntoFile(new parser.StudyGroupList(getCollection()));
    }

    public void deserialize() throws IOException, RuntimeException {
        StudyGroupList list = parser.deserializeFromFile();
        if (list == null) {
            System.err.println("File is empty!");
            System.out.println("Created empty database... Type \"exit\" to quit without changes");
            return;
        }

        getCollection().addAll(list.getStudyGroupList());
    }

    @Override
    public StudyGroup removeElementByPrimaryKey(Long id) throws IllegalArgumentException {
        StudyGroup deleted = super.removeElementByPrimaryKey(id);

        pushToUndoStack(UndoLog.deletedElements(deleted));

        return deleted;
    }

    /**
     * Goes through all elements in collection and checks if they are unique
     *
     * @return max id in collection
     * @throws PrimaryKeyIsNotUnique exception if there is two same id's
     */
    public long checkAllUnique() throws PrimaryKeyIsNotUnique {
        long maxId = 0;

        HashSet<Long> setOfUsedIds = new HashSet<>();

        for (StudyGroup group : getCollection()) {
            if (!setOfUsedIds.add(group.getId())) {
                throw new PrimaryKeyIsNotUnique("There is not unique id (" + group.getId() + ") in database file");
            }

            if (group.getId() > maxId) {
                maxId = group.getId();
            }
        }

        return maxId;
    }

    @Override
    public void addElement(StudyGroup group) {
        super.addElement(group);
        group.setId(lastId++);

        pushToUndoStack(UndoLog.addedElements(group));
    }

    @Override
    public void pushToUndoStack(UndoLog<StudyGroup> log) {
        if (log.changesList.isEmpty()) {
            return;
        }

        undoLogStacksByClient.get(currentClient).push(log);
        serialize();
    }

    @Override
    public boolean undo() throws RuntimeException{
        if (undoLogStacksByClient.get(currentClient).isEmpty())
            return false;

        boolean res = undoLogStacksByClient.get(currentClient).pop().undo(getCollection());
        if (!res) {
            throw new RuntimeException("Something went wrong when undoing...");
        }

        return true;
    }
}
