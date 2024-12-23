package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.InfoCommandData;
import commands.exceptions.CommandException;
import database.Database;

import java.util.List;

public class InfoCommandImpl extends CommandImpl {
    Database<?, ?> database;
    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        return database.getInfo();
    }

    public InfoCommandImpl(Database<?, ?> database) {
        this.database = database;
        setCommandData(new InfoCommandData());
    }
}
