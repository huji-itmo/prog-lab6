package commands.databaseCommands;

import com.google.gson.internal.ObjectConstructor;
import commands.CommandData;
import commands.CommandImpl;
import commands.databaseCommands.UndoCommandData;
import commands.exceptions.CommandException;
import database.Database;

import java.util.List;

public class UndoCommandImpl extends CommandImpl {
    private final Database<?, ?> database;

    public UndoCommandImpl(Database<?, ?> database) {
        this.database = database;
        setCommandData(new UndoCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        boolean res = database.undo();

        if (!res) {
            throw new CommandException("Nothing to undo.");
        }
        return "Success.";
    }
}
