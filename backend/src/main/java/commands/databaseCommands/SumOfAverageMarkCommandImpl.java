package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.SumOfAverageMarkCommandData;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import dataStructs.StudyGroup;
import database.CollectionDatabase;

import java.util.List;
import java.util.Objects;

public class SumOfAverageMarkCommandImpl extends CommandImpl {
    private final CollectionDatabase<StudyGroup, ?> database;

    public SumOfAverageMarkCommandImpl(CollectionDatabase<StudyGroup, ?> database) {
        this.database = database;
        setCommandData(new SumOfAverageMarkCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {

        return Double.toString(database.getCollection().stream()
                .mapToLong(StudyGroup::getAverageMark)
                .summaryStatistics()
                .getAverage());
    }
}
