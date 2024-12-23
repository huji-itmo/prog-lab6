package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.RemoveGreaterCommandData;
import commands.exceptions.CommandException;
import database.Database;

import java.util.List;

public class RemoveLowerCommandImpl extends CommandImpl {
    private final Database<?, Long> database;

    public RemoveLowerCommandImpl(Database<?,Long> decorator) {
        this.database = decorator;
        setCommandData(new RemoveLowerCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {

        try {
            long count = database.removeGreaterOrLowerThanPrimaryKey((Long) packedArgs.get(0), false).size();

            return "Removed " + count + " elements.";
        }
        catch (NumberFormatException e) {
            throw new CommandException("id should be a number!");
        }
    }
}
