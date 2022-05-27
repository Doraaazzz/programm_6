package Commands;

import Server.Module;
import Utility.CollectionManager;

import java.io.IOException;

/**
 * команда сохранения коллекции в файл
 */
public class Save extends AbstractCommand {

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
    public Save(String name, String description) {
        super(name, description);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод вызывает сохранение коллекции в файл
     *
     * @return Возвращает True при выполнении
     * @throws IOException
     * @see CollectionManager#saveCollection()
     */
    @Override
    public boolean exec() {
        Module.addMessage(collectionManager.saveCollection());
        return true;
    }
}
