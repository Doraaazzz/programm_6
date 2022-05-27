package Utility;

import Client.Client;
import Commands.*;
import Data.*;
import exceptions.IncompleteData;
import exceptions.IncorrectData;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс сущности, которая будет парсить все поступаемые команды и вызывать их выполнение.
 */
public class CommandManager {

    /**
     * Поля, содержащие объекты команд.
     */
    private final Add add;
    private final Clear clear;
    private final ExecuteScript executeScript;
    private final Exit exit;
    private final Head head;
    private final Help help;
    private final History history;
    private final Info info;
    private final PrintAscending printAscending;
    private final PrintFieldDescendingMpaaRating printFieldDescendingMpaaRating;
    private final RemoveAllByGoldenPalmCount removeAllByGoldenPalmCount;
    private final RemoveById removeById;
    private final RemoveFirst removeFirst;
    private final Save save;
    private final Show show;
    private final UpdateById updateById;
    private final Client client;
    private final Chronicler chrolicler;
    private final Connect connect;

    private ArrayList<String> ScriptsStack = new ArrayList<>();
    private ArrayList<Scanner> scripts = new ArrayList<>();
    private int scriptcounter = -1;

    private String[] commands = {"add", "clear", "execute_script", "exit", "head", "history", "info", "print_ascending", "print_field_descending_mpaa_rating",
            "remove_all_by_golden_palm_count", "remove_by_id", "remove_first", "show", "update", "help", "connect"};

    /**
     * Конструктор менеджера. Автоматически инициализирует объекты всех команд при создании и менеджера коллекций.
     *
     * @throws IOException
     * @see CollectionManager
     */
    public CommandManager(Client client) throws IOException {
        this.client = client;
        chrolicler = new Chronicler();

        add = new Add("add", "добавить новый элемент в коллекцию");
        clear = new Clear("clear", "очистить коллекцию");
        executeScript = new ExecuteScript("execute_script filename", "считать и исполнить скрипт из указанного файла." +
                " В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.", this);
        exit = new Exit("exit", "завершить программу (без сохранения в файл)");
        head = new Head("head", "вывести первый элемент коллекции");
        history = new History("history", "вывести последние 13 команд (без их аргументов)", chrolicler);
        info = new Info("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата " +
                "инициализации, количество элементов и т.д.)");
        printAscending = new PrintAscending("print_ascending", "вывести элементы коллекции в порядке возрастания");
        printFieldDescendingMpaaRating = new PrintFieldDescendingMpaaRating("print_field_descending_mpaa_rating",
                "вывести значения поля mpaaRating всех элементов в порядке убывания");
        removeAllByGoldenPalmCount = new RemoveAllByGoldenPalmCount("remove_all_by_golden_palm_count goldenPalmCount",
                "удалить из коллекции все элементы, значение поля goldenPalmCount которого эквивалентно заданному");
        removeById = new RemoveById("remove_by_id id", "удалить элемент из коллекции по его id");
        removeFirst = new RemoveFirst("remove_first", "удалить первый элемент из коллекции");
        save = new Save("save", "сохранить коллекцию в файл");
        show = new Show("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        updateById = new UpdateById("update id", "обновить значение элемента коллекции, id которого равен заданному");
        help = new Help("help", "вывести справку по доступным командам", add, clear, executeScript, exit,
                head, history, info, printAscending, printFieldDescendingMpaaRating, removeAllByGoldenPalmCount, removeById, removeFirst, save, show, updateById);
        connect = new Connect("connect", "команда для проверки соединения");
    }

    /**
     * @param line принимает на вход строку, парсит её на команду и её аргументы, по полученному имени запускает выполнение нужной команды, передаёт на вход команде аргумент или пустую строку, выводит ошибку, если не удалось сопоставить строке команду
     * @throws IOException
     * @see CommandManager#commandParser(String)
     * @see CommandManager#chooseCommand(String)
     */
    public void managerWork(String line) throws IOException {
        String[] data = commandParser(line);
        switch (chooseCommand(data[0])) {
            case (0): {
                Movie newMovie;
                try {
                    newMovie = Inquiry.askMovie();
                    add.setArgument(newMovie);
                    System.out.println(client.run(add));
                    chrolicler.addCommandInHistory("add");
                } catch (IncorrectData e) {
                    System.out.println(e.getMessage());
                    System.out.println("Add aborted");
                }
                break;
            }
            case (1): {
                System.out.println(client.run(clear));
                chrolicler.addCommandInHistory("clear");
                break;
            }
            case (2): {
                executeScript.setArgument(data[1]);
                executeScript.exec();
                chrolicler.addCommandInHistory("execute_script");
                break;
            }
            case (3): {
                exit.setSave(save);
                System.out.println(client.run(exit));
                chrolicler.addCommandInHistory("exit");
                break;
            }
            case (4): {
                System.out.println(client.run(head));
                chrolicler.addCommandInHistory("head");
                break;
            }
            case (5): {
                history.exec();
                chrolicler.addCommandInHistory("history");
                break;
            }
            case (6): {
                System.out.println(client.run(info));
                chrolicler.addCommandInHistory("info");
                break;
            }
            case (7): {
                System.out.println(client.run(printAscending));
                chrolicler.addCommandInHistory("print_ascending");
                break;
            }
            case (8): {
                System.out.println(client.run(printFieldDescendingMpaaRating));
                chrolicler.addCommandInHistory("print_field_descending_mpaa_rating");
                break;
            }
            case (9): {
                Long goldenpalmcount;
                try {
                    goldenpalmcount = Long.parseLong(data[1]);
                    if (!(goldenpalmcount > 0)) {
                        throw new InputMismatchException("<=0");
                    }
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Передан некорректный аргумент программе.");
                    goldenpalmcount = Inquiry.askGoldenPalmCount();
                }
                removeAllByGoldenPalmCount.setArgument(goldenpalmcount);
                System.out.println(client.run(removeAllByGoldenPalmCount));
                chrolicler.addCommandInHistory("remove_all_by_golden_palm_count");
                break;
            }
            case (10): {
                int id;
                try {
                    id = Integer.parseInt(data[1]);
                    if (!(id > 0)) {
                        throw new InputMismatchException("<=0");
                    }
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Передан некорректный аргумент программе.");
                    id = Inquiry.askID();
                }
                removeById.setArgument(id);
                System.out.println(client.run(removeById));
                chrolicler.addCommandInHistory("remove_by_id");
                break;
            }
            case (11): {
                System.out.println(client.run(removeFirst));
                chrolicler.addCommandInHistory("remove_first");
                break;
            }
            case (12): {
                System.out.println(client.run(show));
                chrolicler.addCommandInHistory("show");
                break;
            }
            case (13): {
                int id;
                try {
                    id = Integer.parseInt(data[1]);
                    if (!(id > 0)) {
                        throw new InputMismatchException("<=0");
                    }
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Передан некорректный аргумент программе.");
                    id = Inquiry.askID();
                }
                try {
                    Movie newMovie = Inquiry.askMovie();
                    newMovie.setId(id);
                    updateById.setArgument(newMovie);
                    System.out.println(client.run(updateById));
                    chrolicler.addCommandInHistory("update");
                } catch (IncorrectData e) {
                    System.out.println(e.getMessage());
                    System.out.println("Update aborted.");
                }
                break;
            }
            case (14): {
                help.exec();
                chrolicler.addCommandInHistory("help");
                break;
            }
            case (15): {
                System.out.println(client.run(connect));
                break;
            }
            case (-1): {
                System.out.println("Команда не распознана.");
                break;
            }
        }
    }

    /**
     * @param line получает на вход строку, разбивает её на пробелы, первое слово - команда, второе (если есть) - аргумент.
     * @return возвращает массив строк, где 0й элемент - команда, 1й (если есть) - аргумент.
     */
    public String[] commandParser(String line) {

        try {
            Scanner scanner = new Scanner(line);
            if (!(!line.contains(" "))) {
                scanner.useDelimiter("\\s");
                String command = scanner.next();
                String data = "";
                if (scanner.hasNext()) {
                    data = scanner.next();
                }
                return new String[]{command, data};
            } else {
                String commandwodata = scanner.next();
                return new String[]{commandwodata, " "};
            }
        } catch (NoSuchElementException e) {
            return new String[]{"  "};
        }
    }

    /**
     * @param command принимает на вход команду, сопоставляет ей элемент из списка команд.
     * @return возвращает порядковый номер элемента, который удалось сопоставить или -1, если не получилось.
     */
    private int chooseCommand(String command) {
        for (int i = 0; i < commands.length; i++) {
            if (command.equals(commands[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Метод для запуска скрипта. Парсит его и по очереди запускает команды, тащит в себе дополнительные аргументы-строки, если нужны.
     *
     * @param script
     * @throws IOException
     */
    public void executeScriptCommand(Scanner script)  {
        scripts.add(script);
        while (scripts.get(scriptcounter).hasNext()) {
            String[] data = commandParser(scripts.get(scriptcounter).next());
            switch (chooseCommand(data[0])) {
                case (0): {
                    try {
                        Movie newMovie = new Movie();
                        String answer;
                        newMovie.setName(getNextLineFromScript());
                        newMovie.setCoordinates(new Coordinates(Long.parseLong(getNextLineFromScript()), Double.parseDouble(getNextLineFromScript())));
                        newMovie.setCreationDate(ZonedDateTime.now());
                        answer = getNextLineFromScript();
                        if (answer == null) {
                            newMovie.setOscarsCount(null);
                        } else {
                            newMovie.setOscarsCount(Integer.parseInt(answer));
                        }
                        answer = getNextLineFromScript();
                        if (answer == null) {
                            newMovie.setGoldenPalmCount(null);
                        } else {
                            newMovie.setGoldenPalmCount(Long.parseLong(answer));
                        }
                        newMovie.setGenre(MovieGenre.valueOf(getNextLineFromScript()));
                        newMovie.setMpaaRating(MpaaRating.valueOf(getNextLineFromScript()));
                        Person operator = new Person();
                        operator.setName(getNextLineFromScript());
                        operator.setHeight(Double.parseDouble(getNextLineFromScript()));
                        operator.setEyeColor(Color.valueOf(getNextLineFromScript()));
                        answer = getNextLineFromScript();
                        if (answer == null) {
                            operator.setHairColor(null);
                        } else {
                            operator.setHairColor(Color.valueOf(answer));
                        }
                        newMovie.setOperator(operator);
                        add.setArgument(newMovie);
                        System.out.println(client.run(add));
                    } catch (IncompleteData e) {
                        System.out.println(e.getMessage() + " Skip add.");
                    } catch (Exception e) {
                        System.out.println("Unreadable data. Skip add.");
                        rollScriptForNextCommand();
                    }
                    break;
                }

                case (1): {
                    System.out.println(client.run(clear));
                    break;
                }
                case (2): {
                    executeScript.setArgument(data[1]);
                    executeScript.setFlag(true);
                    executeScript.exec();
                    executeScript.setFlag(false);
                    break;
                }
                case (3): {
                    //pass exit
                    break;
                }
                case (4): {
                    System.out.println(client.run(head));
                    break;
                }
                case (5): {
                    history.exec();
                    break;
                }
                case (6): {
                    System.out.println(client.run(info));
                    break;
                }
                case (7): {
                    System.out.println(client.run(printAscending));
                    break;
                }
                case (8): {
                    System.out.println(client.run(printFieldDescendingMpaaRating));
                    break;
                }
                case (9): {
                    Long goldenpalmcount;
                    try {
                        goldenpalmcount = Long.parseLong(data[1]);
                        if(!(goldenpalmcount>0)){
                            throw new InputMismatchException("<=0");
                        }
                        removeAllByGoldenPalmCount.setArgument(goldenpalmcount);
                        System.out.println(client.run(removeAllByGoldenPalmCount));
                    } catch (InputMismatchException | NumberFormatException e){
                        System.out.println("Передан некорректный аргумент программе remove_all_by_golden_palm в скрипте.");
                    }
                    break;
                }
                case (10): {
                    int id;
                    try{
                        id = Integer.parseInt(data[1]);
                        if(!(id>0)){
                            throw new InputMismatchException("<=0");
                        }
                        removeById.setArgument(id);
                        System.out.println(client.run(removeById));
                    } catch (InputMismatchException | NumberFormatException e){
                        System.out.println("Передан некорректный аргумент программе remove_by_id в скрипте.");
                    }
                    break;
                }
                case (11): {
                    System.out.println(client.run(removeFirst));
                    break;
                }
                case (12): {
                    System.out.println(client.run(show));
                    break;
                }
                case (13): {
                    int id;
                    try {
                        id = Integer.parseInt(data[1]);
                        if (!(id > 0)) {
                            throw new InputMismatchException("<=0");
                        }

                        Movie newMovie = new Movie();
                        String answer;
                        newMovie.setName(getNextLineFromScript());
                        newMovie.setCoordinates(new Coordinates(Long.parseLong(getNextLineFromScript()), Double.parseDouble(getNextLineFromScript())));
                        newMovie.setCreationDate(ZonedDateTime.now());
                        answer = getNextLineFromScript();
                        if (answer == null) {
                            newMovie.setOscarsCount(null);
                        } else {
                            newMovie.setOscarsCount(Integer.parseInt(answer));
                        }
                        answer = getNextLineFromScript();
                        if (answer == null) {
                            newMovie.setGoldenPalmCount(null);
                        } else {
                            newMovie.setGoldenPalmCount(Long.parseLong(answer));
                        }
                        newMovie.setGenre(MovieGenre.valueOf(getNextLineFromScript()));
                        newMovie.setMpaaRating(MpaaRating.valueOf(getNextLineFromScript()));
                        Person operator = new Person();
                        operator.setName(getNextLineFromScript());
                        operator.setHeight(Double.parseDouble(getNextLineFromScript()));
                        operator.setEyeColor(Color.valueOf(getNextLineFromScript()));
                        answer = getNextLineFromScript();
                        if (answer == null) {
                            operator.setHairColor(null);
                        } else {
                            operator.setHairColor(Color.valueOf(answer));
                        }
                        newMovie.setOperator(operator);
                        newMovie.setId(id);
                        updateById.setArgument(newMovie);
                        System.out.println(client.run(updateById));
                    } catch (InputMismatchException | NumberFormatException e) {
                        System.out.println("Передан некорректный аргумент программе.");
                    } catch (IncompleteData e) {
                        System.out.println(e.getMessage() + " Skip update.");
                    } catch (Exception e) {
                        System.out.println("Unreadable data. Skip update.");
                        rollScriptForNextCommand();
                    }
                    break;
                }
                case (14): {
                    help.exec();
                    break;
                }
                case (15): {
                    //connect
                }
                case (-1): {
                    System.out.println("Команда не распознана.");
                    break;
                }
            }
        }
    }

    public String getNextLineFromScript() throws IncompleteData {
        Scanner scanner = scripts.get(scriptcounter);
        for (String c : commands) {
            if (scanner.hasNext(c)) {
                throw new IncompleteData("Данные объекта неполные.");
            }
        }
        String nextLine = scanner.next();
        if (nextLine.equals("")) {
            return null;
        } else {
            return nextLine;
        }
    }

    public void rollScriptForNextCommand() {
        boolean rollComplete = false;
        Scanner scanner = scripts.get(scriptcounter);
        while (scanner.hasNext()) {
            for (String c : commands) {
                if (scanner.hasNext(c)) {
                    rollComplete = true;
                    break;
                }
            }
            if (!rollComplete) {
                scanner.next();
            } else {
                break;
            }
        }
    }

    public void scriptscounterIncrement() {
        scriptcounter += 1;
    }

    public void scriptscounterDecrement() {
        scripts.remove(scriptcounter);
        scriptcounter -= 1;
        if (scriptcounter == -1) {
            ScriptsStack.removeAll(ScriptsStack);
        }
    }


    public Add getAdd() {
        return add;
    }

    public Exit getExit() {
        return exit;
    }

    public Help getHelp() {
        return help;
    }


    public Clear getClear() {
        return clear;
    }

    public ExecuteScript getExecuteScript() {
        return executeScript;
    }

    public Info getInfo() {
        return info;
    }

    public Save getSave() {
        return save;
    }

    public Show getShow() {
        return show;
    }

    public ArrayList<String> getScriptsStack() {
        return ScriptsStack;
    }

    public void setScriptsStack(ArrayList<String> scriptsStack) {
        ScriptsStack = scriptsStack;
    }

    public void addScriptsStack(String newScriptFile) {
        ScriptsStack.add(newScriptFile);
    }

    public boolean checkLoopTry(String newScriptFile) {
        for (String s : ScriptsStack) {
            if (s.equals(newScriptFile)) {
                return true;
            }
        }
        return false;
    }

    public void setScripts(ArrayList<Scanner> scripts) {
        this.scripts = scripts;
    }

    public void setScriptcounter(int scriptcounter) {
        this.scriptcounter = scriptcounter;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }
}