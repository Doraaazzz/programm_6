package Commands;

import java.io.IOException;

/** команда вывода имени и описания всех возможных команд */
public class Help extends AbstractCommand{

    private final Add add;
    private final Clear clear;
    private final ExecuteScript executeScript;
    private final Exit exit;
    private final Head head;
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

    /**
     * конструктор
     * @param name имя команды
     * @param description описание команды
     * @param add добавление элемента
     * @param clear очистка коллекции
     * @param executeScript запуск скрипта
     * @param exit завершение программы
     * @param head получение первого элемента коллекции
     * @param history история
     * @param info информация о коллекции
     * @param printAscending вывод элементов по возрастанию
     * @param printFieldDescendingMpaaRating вывод MpaaRating по уменьшению
     * @param removeAllByGoldenPalmCount удаление всех элементов, чей GoldenPalmCount равен введенному
     * @param removeById удаление по id
     * @param removeFirst удаление первого элемента
     * @param save сохранение коллекции в файл
     * @param show вывод информации о коллекции
     * @param updateById обновление элемента по id
     */
    public Help(String name, String description, Add add, Clear clear, ExecuteScript executeScript, Exit exit, Head head, History history, Info info, PrintAscending printAscending, PrintFieldDescendingMpaaRating printFieldDescendingMpaaRating, RemoveAllByGoldenPalmCount removeAllByGoldenPalmCount, RemoveById removeById, RemoveFirst removeFirst, Save save, Show show, UpdateById updateById) {
        super(name, description);
        this.add = add;
        this.clear = clear;
        this.executeScript = executeScript;
        this.exit = exit;
        this.head = head;
        this.history = history;
        this.info = info;
        this.printAscending = printAscending;
        this.printFieldDescendingMpaaRating = printFieldDescendingMpaaRating;
        this.removeAllByGoldenPalmCount = removeAllByGoldenPalmCount;
        this.removeById = removeById;
        this.removeFirst = removeFirst;
        this.save = save;
        this.show = show;
        this.updateById = updateById;
    }

    /**
     * Метод вызывает у команд геттеры имён и описаний и выводит их в консоль.
     * @return Возвращает True при выполнении
     * @throws IOException
     */
    @Override
    public boolean exec() {
        System.out.println("Справка по командам: ");
        System.out.println(add.getName()+": "+add.getDescription());
        System.out.println(clear.getName()+": "+clear.getDescription());
        System.out.println(executeScript.getName()+": "+executeScript.getDescription());
        System.out.println(exit.getName()+": "+exit.getDescription());
        System.out.println(head.getName()+": "+head.getDescription());
        System.out.println(history.getName()+": "+history.getDescription());
        System.out.println(info.getName()+": "+info.getDescription());
        System.out.println(printAscending.getName()+": "+printAscending.getDescription());
        System.out.println(printFieldDescendingMpaaRating.getName()+": "+printFieldDescendingMpaaRating.getDescription());
        System.out.println(removeAllByGoldenPalmCount.getName()+": "+removeAllByGoldenPalmCount.getDescription());
        System.out.println(removeById.getName()+": "+removeById.getDescription());
        System.out.println(removeFirst.getName()+": "+removeFirst.getDescription());
        System.out.println(save.getName()+": "+save.getDescription());
        System.out.println(show.getName()+": "+show.getDescription());
        System.out.println(updateById.getName()+": "+updateById.getDescription());
        return true;
    }
}
