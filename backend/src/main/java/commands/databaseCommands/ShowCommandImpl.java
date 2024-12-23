package commands.databaseCommands;

import commands.CommandImpl;
import commands.exceptions.CommandException;
import database.Database;

import java.util.List;

public class ShowCommandImpl extends CommandImpl {
    Database<?, ?> database;
    public ShowCommandImpl(Database<?, ?> database) {
        this.database = database;
        setCommandData(new ShowCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {

        return database.serializeAllElements();
    }
}
