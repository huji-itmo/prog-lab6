package commands.databaseCommands;

import commands.CommandImpl;
import commands.databaseCommands.CountLessThanFormOfEducationCommandData;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import dataStructs.FormOfEducation;
import dataStructs.StudyGroup;
import database.CollectionDatabase;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CountLessThanFormOfEducationCommandImpl extends CommandImpl {

    private final CollectionDatabase<StudyGroup,?> database;

    public CountLessThanFormOfEducationCommandImpl(CollectionDatabase<StudyGroup, ?> database) {
        this.database = database;
        setCommandData(new CountLessThanFormOfEducationCommandData());
    }

    @Override
    public String execute(List<Object> packedArgs) throws CommandException {
        try {
            FormOfEducation formOfEducation = Optional.ofNullable(packedArgs.get(0))
                    .filter(FormOfEducation.class::isInstance)
                    .map(FormOfEducation.class::cast)
                    .orElseThrow(() -> new CommandException("Bad data! class: FormOfEducation"));

            long count = database.getCollection().stream().filter(group -> group.getFormOfEducation().ordinal() < formOfEducation.ordinal()).count();

            return "The count is " + count + "!";
        } catch (IllegalArgumentException e) {
            throw new IllegalCommandSyntaxException(e.getMessage(), getCommandData());
        }

    }
}
