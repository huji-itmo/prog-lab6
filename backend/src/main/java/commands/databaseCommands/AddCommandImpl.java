package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.AddCommandData;
import commands.exceptions.CommandException;
import dataStructs.StudyGroup;
import database.CollectionDatabase;
import database.Database;
import dataStructs.DatabaseEntity;

import java.util.List;
import java.util.Optional;


public class AddCommandImpl<T extends DatabaseEntity> extends CommandImpl {

    CollectionDatabase<T, ?> database;

    public AddCommandImpl(CollectionDatabase<T, ?> database) {
        this.database = database;
        setCommandData(new AddCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        try {
            Class<? extends T> clazz = getCommandData().getArguments()[0].getClazz().asSubclass(database.getElementClass());

            T element = Optional.ofNullable(packedArgs.get(0))
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .orElseThrow(() -> new CommandException("Bad data! class: " + clazz.getName()));

            database.addElement(element);

            return "Added element with id: " +  database.getPrimaryKey().apply(element) + " (" + element.getValues(",") + ")";

        } catch (ClassCastException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
