package exceptions;

/** класс ошибки некорректных данных */
public class IncorrectData extends Exception{
    public IncorrectData(String errorMessage){
        super(errorMessage);
    }
}
