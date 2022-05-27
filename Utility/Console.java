package Utility;

import java.io.EOFException;
import java.io.IOException;

public class Console {

    /**
     * Метод запускает работу программы, пока не будет введено exit
     * @param commandManager менеджер команд
     * @throws IOException
     */
    public static void run(CommandManager commandManager) throws IOException {
        System.out.println("Приложение запущено. Для справки введите \"help\"");
        String input = "run";
        while (!(input.equals("exit"))){
            try {
                input = Inquiry.askCommand();
                commandManager.managerWork(input);
                System.out.println("");
            } catch (EOFException ingnore){
                //pass
            }
        }
    }
}
