package commands.clientSideCommands;

import application.ClientApplication;
import commands.databaseCommands.AddIfMinData;
import commands.databaseCommands.ExistsByIdCommandData;
import commands.databaseCommands.GetMinStudentCountCommandData;
import commands.databaseCommands.UpdateByIdCommandData;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;
import connection.DatabaseConnection;
import dataStructs.communication.Request;
import dataStructs.communication.ServerResponse;
import lombok.AllArgsConstructor;
import validator.Validator;

import java.util.List;

public class AddIfMinCommandImpl extends ClientSideCommand{

    private final DatabaseConnection connection;

    private final Validator validator;

    public AddIfMinCommandImpl(DatabaseConnection connection, Validator validator) {
        this.connection = connection;
        this.validator = validator;
        setCommandData(new AddIfMinData());
    }

    @Override
    public String execute(String args) {
        ServerResponse responseGetMin = connection.sendOneShot(new Request(new GetMinStudentCountCommandData(), List.of()));

        ClientApplication.newMessageHandler(new ServerResponse(responseGetMin.getCode(), "Current min student count: " + responseGetMin.getText()));

        List<Object> packedArguments = validator.checkSyntax(new AddIfMinData(), args);

        ServerResponse responseAddIfMin = connection.sendOneShot(new Request(new AddIfMinData(), packedArguments));

        if (responseAddIfMin.getCode() < 200 || responseAddIfMin.getCode() >= 300) {
            throw new CommandException(responseAddIfMin.getText());
        }

        return responseAddIfMin.getText();
    }
}
