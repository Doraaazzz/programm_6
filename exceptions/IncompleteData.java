package exceptions;

/** класс ошибки Неполных даннных */
public class IncompleteData extends Exception{
    public IncompleteData(String ErrorMesage){
        super(ErrorMesage);
    }
}