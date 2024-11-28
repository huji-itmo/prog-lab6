package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.AddIfMinData;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import dataStructs.StudyGroup;
import database.CollectionDatabase;
import database.Database;
import dataStructs.DatabaseEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AddIfMinCommandImpl extends CommandImpl {

    private final CollectionDatabase<StudyGroup, ?> collectionDatabase;
    public AddIfMinCommandImpl(CollectionDatabase<StudyGroup, ?> database) {
        this.collectionDatabase = database;
        setCommandData(new AddIfMinData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        Optional<StudyGroup> res = collectionDatabase.getCollection()
                .stream()
                .filter(studyGroup -> studyGroup.getStudentsCount() != null)
                .min(Comparator.comparing(StudyGroup::getStudentsCount))
                .filter(studyGroup -> studyGroup.getStudentsCount() <= ((StudyGroup)packedArgs.get(0)).getStudentsCount());

        if (res.isPresent()) {
            return "Element is NOT added because it's student count wasn't minimal!";
        }

        StudyGroup newElement = (StudyGroup)packedArgs.get(0);

        collectionDatabase.addElement(newElement);

        return "Added element with id: " + newElement.getId() + " (" + newElement.getValues(", ") +")";
    }
}
