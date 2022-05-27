package Commands;

import Server.Module;

import java.io.IOException;

/** команда завершения программы */
public class Exit extends AbstractCommand{

    private Save save;
    /**
     * коструктор
     * @param name имя команды
     * @param description описание команды
     */
    public Exit(String name, String description) {
        super(name, description);
    }

    public void setSave(Save save) {
        this.save = save;
    }

    public Save getSave() {
        return save;
    }

    /**
     * Метод выводит сообщение о завершении программы. Сама программа завершается автоматически после выполнения цикла в методе Main с введённой командой exit.
     * @return Возвращает True после выполнения
     * @throws IOException
     */
    @Override
    public boolean exec() {
        Module.addMessage("Завершаю программу.");
        return true;
    }
}
