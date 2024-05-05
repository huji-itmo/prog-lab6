package commands.databaseCommands;

import commands.CommandImpl;
import commands.exceptions.CommandException;
import database.Database;

import javax.swing.text.StyledEditorKit;
import java.util.List;


public class ExistsIdCommandImpl extends CommandImpl {
    private final Database<?,Long> database;

    public ExistsIdCommandImpl(Database<?, Long> database) {
        this.database = database;
        setCommandData(new ExistsByIdCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        return Boolean.toString(database.existsById((Long) packedArgs.get(0)));
    }
}
