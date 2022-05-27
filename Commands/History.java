package Commands;

import Utility.Chronicler;

import java.io.IOException;

/** команда истории  */
public class History extends AbstractCommand{
    /** поле летописец */
    private final Chronicler chronicler;

    /**
     * конструктор
     * @param name имя команды
     * @param description описание команды
     * @param chronicler летописец
     */
    public History(String name, String description, Chronicler chronicler) {
        super(name, description);
        this.chronicler=chronicler;
    }

    /**
     * Команда выводит последние 13 команд
     * @return Возвращает True при выполнении
     * @throws IOException
     */
    @Override
    public boolean exec() {
        String[] history = chronicler.getHistory();
        System.out.println("Вывожу последние 13 команд:");
        for (String s : history){
            if(!(s==null)){
                System.out.println(s);
            }
        }
        return true;
    }
}
