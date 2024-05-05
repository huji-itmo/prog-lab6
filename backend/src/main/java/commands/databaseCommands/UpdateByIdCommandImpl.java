package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.UpdateByIdCommandData;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import dataStructs.exceptions.IllegalValueException;
import database.Database;
import dataStructs.DatabaseEntity;

import java.util.List;
import java.util.Optional;

public class UpdateByIdCommandImpl<T extends DatabaseEntity> extends CommandImpl {

    private final Database<T, Long> database;

    public UpdateByIdCommandImpl(Database<T, Long> database) {
        this.database = database;
        setCommandData(new UpdateByIdCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        try {
            Long id = (Long) packedArgs.get(0);

            Class<? extends T> clazz = getCommandData().getArguments()[1].getClazz().asSubclass(database.getElementClass());

            T element = Optional.ofNullable(packedArgs.get(1))
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .orElseThrow(() -> new CommandException("Bad data! class: " + clazz.getName()));

            return "Updated id: " + id + " with value (" + database.updateElementByPrimaryKey(id, element).getValues(", ") + ")";
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
