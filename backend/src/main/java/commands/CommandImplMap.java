package commands;

import commands.databaseCommands.*;
import dataStructs.StudyGroup;
import database.CollectionDatabase;

import java.util.HashMap;
import java.util.Map;

public class CommandImplMap {
    public Map<CommandData, CommandImpl> map = new HashMap<>();

    public CommandImplMap() {
    }

    public void addDatabaseCommands(CollectionDatabase<StudyGroup, Long> database) {
        putCommand(new AddCommandImpl<>(database));
        putCommand(new AddIfMinCommandImpl(database));
        putCommand(new ClearCommandImpl(database));
        putCommand(new CountLessThanFormOfEducationCommandImpl(database));
        putCommand(new InfoCommandImpl(database));
        putCommand(new PrintDescendingCommandImpl(database));
        putCommand(new RemoveByIdCommandImpl(database));
        putCommand(new RemoveGreaterCommandImpl(database));
        putCommand(new RemoveLowerCommandImpl(database));
        putCommand(new ShowCommandImpl(database));
        putCommand(new SumOfAverageMarkCommandImpl(database));
        putCommand(new UndoCommandImpl(database));
        putCommand(new UpdateByIdCommandImpl<>(database));
        putCommand(new ExistsIdCommandImpl(database));
        putCommand(new GetMinStudentCountCommandImpl(database));
    }

    public void putCommand(CommandImpl impl) {
        map.put(impl.getCommandData(), impl);
    }
}
