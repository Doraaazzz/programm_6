package Client;

import Utility.CommandManager;
import Utility.Console;
import Utility.Inquiry;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        int port = Inquiry.askPort();
        Client client = new Client(port);
        try {
            CommandManager commandManager = new CommandManager(client);
            Console.run(commandManager);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
