import commands.CommandImplMap;
import commands.exceptions.CommandException;
import dataStructs.communication.Request;
import parser.StudyGroupList;
import dataStructs.communication.ServerResponse;
import database.StudyGroupDatabase;
import parser.JsonFileParser;

import java.nio.file.Path;
import java.util.Stack;

public class ServerApplication {
    public static CommandImplMap implMap = new CommandImplMap();

    public static StudyGroupDatabase database;

    public static void run(String[] args) {

        if (args == null || args.length == 0 || args[0].isBlank()) {
            System.err.println("Please add path to arguments of this program!");
            System.exit(-1);
        }

        database = new StudyGroupDatabase(new JsonFileParser<>(Path.of(args[0]), StudyGroupList.class));

        implMap.addDatabaseCommands(database);

        Server server = new Server(5252, ServerApplication::messageHandler);

        server.setOnConnected((handler) -> {
            database.getUndoLogStacksByClient().put(handler.getClientId(), new Stack<>());
        });

        server.startClientAcceptingLoop();
    }

    public static void messageHandler(Request request, ConnectionHandler handler) {
        try {
            database.setCurrentClient(handler.getClientId());

            String out = implMap.map.get(request.getCommandData()).execute(request.getParams());
            handler.send( new ServerResponse(200, out));
        } catch (CommandException e) {
            handler.send(new ServerResponse(500, e.getMessage()));
        }
    }
}
