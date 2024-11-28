package commands.databaseCommands;

import commands.CommandImpl;
import commands.exceptions.CommandException;
import dataStructs.StudyGroup;
import database.CollectionDatabase;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GetMinStudentCountCommandImpl extends CommandImpl {

    private final CollectionDatabase<StudyGroup, ?> collectionDatabase;

    public GetMinStudentCountCommandImpl(CollectionDatabase<StudyGroup, ?> collectionDatabase) {
        this.collectionDatabase = collectionDatabase;
        setCommandData(new GetMinStudentCountCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        Optional<StudyGroup> res = collectionDatabase.getCollection()
                .stream()
                .filter(studyGroup -> studyGroup.getStudentsCount() != null)
                .min(Comparator.comparing(StudyGroup::getStudentsCount));

        return res.map(studyGroup -> Integer.toString(studyGroup.getStudentsCount())).orElse("null");
    }
}
