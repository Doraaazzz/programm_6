package Utility;

import Data.Movie;
import Utility.FileManager;
import exceptions.IncorrectData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * класс, объект которого хранит в себе коллекцию и управляет ей.
 */
public class CollectionManager {

    /**
     * поле коллекция фильмов
     */
    private LinkedList<Movie> MoviesCollection = new LinkedList<>();
    /**
     * поле тип
     */
    private final String type;
    /**
     * поле время инициализации
     */
    private final LocalDateTime initTime;

    /**
     * Конструктор класса. Загружает коллекцию из файла.
     * Записывает время инициализации коллекции и её тип.
     *
     * @throws IOException
     */
    public CollectionManager() {
        loadCollection();
        type = "LinkedList";
        initTime = LocalDateTime.now();
    }

    /**
     * Метод обращается к Файл-Менеджеру, тот читает коллекцию и передаёт её методу, метод записывает полученную коллекцию в своё поле.
     *
     * @throws IOException
     * @see FileManager#readCollection()
     */
    public void loadCollection() {
        MoviesCollection = FileManager.readCollection();
        sortCollection();
    }

    /**
     * Метод выполняет замену элемента с указанным id на новый (с тем же id)
     *
     * @param newMovie объект Movie, который необходимо добавить в коллекцию на замену старому.
     * @param id       id элемента, который нужно заменить
     */
    public String replaceElementByID(Movie newMovie, Integer id) {
        if (checkMatchingID(id)) {
            removeElement(findElementByID(id));
            MoviesCollection.add(newMovie);
            sortCollection();
            return "Элемент обновлён.";
        } else {
            return "Отсутствует элемент для обновления.";
        }
    }

    /**
     * Метод добавляет объект в коллекцию, предварительно устанавливая ему id минимальный из тех, что не будет повторён.
     *
     * @param movie полуает на вход объект типа Movie, который необходимо добавить
     */
    public String addElement(Movie movie) {
        String answer = "";
        try {
            movie.setId(getNewID());
            MoviesCollection.add(movie);
            sortCollection();
            answer += "Элемент добавлен.";
        } catch (IncorrectData e) {
            answer += e.getMessage();
        }
        return answer;
    }

    /**
     * Метод удаляет объект из коллекции.
     *
     * @param movie объект, который нужно удалить из коллекции.
     */
    public void removeElement(Movie movie) {
        MoviesCollection.remove(movie);
        sortCollection();
    }

    /**
     * Метод удаляет элемент из коллекции по указанному ID
     *
     * @param id ID объекта, который нужно удалить
     */
    public String removeElementByID(Integer id) {
        if (!(findElementByID(id)==null)) {
            removeElement(findElementByID(id));
            sortCollection();
            return "Элемент удалён.";
        } else {
            return "Элемента с таким id нет в коллекции.";
        }
    }

    /**
     * Метод перебирает элементы коллекции и ищет соответствие по ID
     *
     * @param id ID элемента, который нужно найти
     * @return Возвращает объект типа Movie, которому соответствует искомый ID. Если элемент не найден, вернёт null
     */
    public Movie findElementByID(int id) {
        for (Movie m : MoviesCollection) {
            Integer mID = m.getId();
            if (mID.equals(id)) {
                return m;
            }
        }
        return null;
    }

    /**
     * @return геттер на коллекцию
     */
    public LinkedList<Movie> getMoviesCollection() {
        return MoviesCollection;
    }

    /**
     * Метод удаляет все элементы в коллекции
     */
    public String removeAllElements() {
        MoviesCollection.removeAll(MoviesCollection);
        return "Коллекция очищена.";
    }

    /**
     * Метод генерирует имя файла для сохранения, передаёт его и имеющуюся коллекцию Файл-Менеджеру
     *
     * @throws IOException
     * @see FileManager#saveCollection(LinkedList)
     */
    public String saveCollection() {
        try {
            FileManager.saveCollection(MoviesCollection);
            return "Коллекция сохранена";
        } catch (IOException e) {
            return "Ошибка сохранения коллекции.";
        }
    }

    /**
     * Метод выводит по-очерёдно строковое представление объектов коллекции.
     */
    public String printAscendingCollection() {
        String answer = "";
        sortCollectionByComp(hashcodeComparator);
        for (Movie m : MoviesCollection) {
            answer += m.toString() + "\n";
        }
        return answer;
    }

    /**
     * Метод выводит информацию о коллекции: тип, дата инициализации, количество элементов и выполняет перебор всех элементов с выводом их номера ID
     */
    public String printInfo() {
        String answer = "Тип коллекции: " + type + ". \n" + "Дата инициализации: " + initTime + ". \n" + "Количество элементов: "
                + MoviesCollection.size() + ". \n" + "Элементы коллекции по хэшкодам: \n";
        sortCollectionByComp(hashcodeComparator);
        for (Movie m : MoviesCollection) {
            answer += m.hashCode() + " ";
        }
        answer += "\n";
        sortCollection();
        return answer;
    }

    /**
     * Метод выполняет проверку, имеется ли объект с заданным ID в коллекции
     *
     * @param id заданный ID
     * @return Возвращает True, если объект найден, и False, если нет.
     */
    public boolean checkMatchingID(Integer id) {
        for (Movie m : MoviesCollection) {
            if (id.equals(m.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод сравнивает два объекта по хешкоду
     */
    public static Comparator<Movie> hashcodeComparator = new Comparator<Movie>() {

        @Override
        public int compare(Movie c1, Movie c2) {
            return c1.hashCode() - c2.hashCode();
        }
    };

    /**
     * Метод получения минимального нового id
     *
     * @return новое значение id
     */
    public Integer getNewID() {
        sortCollection();
        int id = 1;
        while (id > 0) {
            if (!checkMatchingID(id)) {
                break;
            } else {
                id += 1;
            }
        }
        return id;
    }

    /**
     * Метод получения первого элемента коллекции, если она не пуста
     *
     * @return возвращает первый элемент коллекции  или null
     */
    public Movie getHead() {
        sortCollection();
        if (MoviesCollection.isEmpty()) {
            return null;
        } else {
            return MoviesCollection.get(0);
        }
    }

    /**
     * Метод выводит MPAA RATINGS элементов коллекции в порядке убывания
     */
    public String printDescendingMpaaRatings() {
        String answer = "";
        sortCollectionByComp(MpaaRatingsComparator);
        answer += "Вывожу MPAA RATINGS элементов коллекции в порядке убывания: ";
        for (Movie m : MoviesCollection) {
            answer += m.getMpaaRating() + "\n";
        }
        sortCollection();
        return answer;
    }


    public static Comparator<Movie> MpaaRatingsComparator = new Comparator<Movie>() {

        /**
         * Метод сравнивает объекты по значению возрастного рейтинга
         * @return разность значений
         */
        @Override
        public int compare(Movie m1, Movie m2) {
            return m2.getMpaaRating().ordinal() - m1.getMpaaRating().ordinal();
        }
    };

    /**
     * метод удаляет элементы коллекции, значение поля goldenpalmcount которых равно переданному аргументу
     *
     * @param goldenpalmcount число goldenpalmcount, элементы имеющие поля с таким значением должны быть удалены
     */
    public String removeAllByGoldenPalmCount(long goldenpalmcount) {
        int deletedCounter = 0;
        for (Movie m : MoviesCollection) {
            if (m.getGoldenPalmCount().equals(goldenpalmcount)) {
                removeElement(m);
                deletedCounter += 1;
            }
        }
        return "Команда выполнена. Удалено " + deletedCounter + " элементов.";
    }

    /**
     * метод удаляет первый элемент колеекции
     */
    public String removeFirst() {
        if (!(getHead()==null)) {
            removeElement(getHead());
            return  "Элемент удалён.";
        } else {
            return "Коллекция пуста.";
        }
    }

    /**
     * метод выводит строковое представление элементов коллекции
     */
    public String printCollection() {
        if (MoviesCollection.isEmpty()) {
            return "Коллекция пуста.";
        } else {
            sortCollectionByComp(hashcodeComparator);
            String answer = "Вывожу строковое представление элементов коллекции: ";
            for (Movie m : MoviesCollection) {
                answer += m.toString() + "\n";
            }
            sortCollection();
            return answer;
        }
    }

    public void sortCollection() {
        if (!MoviesCollection.isEmpty()) {
            Collections.sort(MoviesCollection);
        }
    }

    public void sortCollectionByComp(Comparator<Movie> comparator) {
        if (!MoviesCollection.isEmpty()) {
            Collections.sort(MoviesCollection, comparator);
        }
    }
}
