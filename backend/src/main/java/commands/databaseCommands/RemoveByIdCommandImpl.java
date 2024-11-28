package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.RemoveByIdCommandData;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import database.Database;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RemoveByIdCommandImpl extends CommandImpl {
    private final Database<?, Long> database;

    public RemoveByIdCommandImpl(Database<?, Long> database) {
        this.database = database;
        setCommandData(new RemoveByIdCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        try {
            database.removeElementByPrimaryKey((Long) packedArgs.get(0));

            return "Element is removed!";
        } catch (NumberFormatException | ClassCastException e) {
            throw new IllegalCommandSyntaxException("{id} should be a number!", getCommandData());
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
