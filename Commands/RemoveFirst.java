package Commands;

import Server.Module;
import Utility.CollectionManager;

import java.io.IOException;

/** команда удаления первого элемента коллекции */
public class RemoveFirst extends AbstractCommand {
    /** поле менеджер коллекции */
    private CollectionManager collectionManager;

    /**
     * конструктор
     * @param name имя команды
     * @param description описание команды
     */
    public RemoveFirst(String name, String description) {
        super(name, description);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод вызывает удаление первого элемента коллекции
     * @return Возвращает True при выполнении
     * @throws IOException
     * @see CollectionManager#removeFirst()
     */
    @Override
    public boolean exec() {
        Module.addMessage(collectionManager.removeFirst());
        return true;
    }
}
