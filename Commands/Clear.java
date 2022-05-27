package Commands;

import Server.Module;
import Utility.CollectionManager;

import java.io.IOException;

/**
 * команда очиски коллекции
 */
public class Clear extends AbstractCommand {

    /**
     * поле менеджер коллекции
     */
    private CollectionManager collectionManager;

    /**
     * конструктор
     *
     * @param name        имя команды
     * @param description описание команды
     */
    public Clear(String name, String description) {
        super(name, description);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод удаляет все элементы коллекции.
     *
     * @return Возвращает True при выполнении.
     * @throws IOException
     * @see CollectionManager#removeAllElements()
     */
    @Override
    public boolean exec() {
        Module.addMessage(collectionManager.removeAllElements());
        return true;
    }
}
