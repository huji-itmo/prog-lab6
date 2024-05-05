package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.PrintDescendingCommandData;
import commands.exceptions.CommandException;
import database.Database;

import java.util.List;

public class PrintDescendingCommandImpl extends CommandImpl {

    private final Database<?, ?> database;

    public PrintDescendingCommandImpl(Database<?, ?> database) {
        this.database = database;
        setCommandData(new PrintDescendingCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        String out = database.getElementsDescendingByPrimaryKey();

        if (out.isBlank()) {
            throw new CommandException("The database is empty!");
        }
        return out;
    }
}
