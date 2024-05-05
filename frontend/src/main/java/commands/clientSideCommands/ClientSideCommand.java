package commands.clientSideCommands;

import commands.CommandData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public abstract class ClientSideCommand {

    public abstract String execute(String args);

    private CommandData commandData;
}
