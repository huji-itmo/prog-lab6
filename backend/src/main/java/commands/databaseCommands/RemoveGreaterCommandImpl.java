package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.RemoveGreaterCommandData;
import commands.exceptions.CommandException;
import database.Database;

import java.util.List;

public class RemoveGreaterCommandImpl extends CommandImpl {

    private final Database<?, Long> database;

    public RemoveGreaterCommandImpl(Database<?, Long> decorator) {
        this.database = decorator;
        setCommandData(new RemoveGreaterCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {

        try {
            long count = database.removeGreaterOrLowerThanPrimaryKey((Long) packedArgs.get(0), true).size();

            return "Removed " + count + " elements.";

        }
        catch (NumberFormatException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
