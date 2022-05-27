package Commands;
import java.io.IOException;

/**
 * интерфейс с объявленными методами для всех комманд
 */
public interface Command {
    /** получение имени команды */
    String getName();

    /** получение описания команды */
    String getDescription();

    /** исполнение команды */
    boolean exec();
}
