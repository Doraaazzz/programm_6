package Commands;

import Server.Module;


public class Connect extends AbstractCommand{
    /**
     * конструктор
     *
     * @param name        имя команды
     * @param description описание команды
     */
    public Connect(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean exec() {
        Module.addMessage("Подключение установлено.");
        return true;
    }
}
