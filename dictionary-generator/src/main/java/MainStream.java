import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class MainStream {
    private static final Scanner scanner = new Scanner(System.in);
    public static final String WELCOME = """
                -= Программа извлечения лексикона из текста =-
                            
            Вводите с консоли текст.
                Или команды:
            Ввод из файла:               "/txt имя_файла"
            Вывод построенного словаря:  "/log"
            Запись словаря в файл:       "/save имя_файла"
            Выход из программы:          "/exit"
                            
                -= = = = = = = = = = = = = = = = = = = =-
            """;
    /**
     * Статический массив словаря.
     */
    private static String[] dictionary = new String[0];
    // накопление результатов работы в массиве строк

    public static void main(String[] args) {
        System.out.println(WELCOME);
        while(true) {
            try {
                String input = scanner.nextLine();

                if ("/exit".equals(input)) break;

                if ("/log".equals(input)) {
                    showDictionary(dictionary);

                } else if (input.startsWith("/txt ")) {
                    dictionary = addFileToDictionary(extractPath(input), dictionary);

                } else if (input.startsWith("/save ")) {
                    saveDictionaryToFile(extractPath(input), dictionary);

                } else {
                    dictionary = addToDictionary(input, dictionary);
                }
            } catch (Exception e) {
                System.out.println("Ошибка команды:" + e.getMessage());
            }
        }
        System.out.println("До свидания!");
    }

    /**
     * Принимает входную строку, разбирает её на слова и добавляет их к ранее созданному словарю.
     * @param input         входная строка.
     * @param oldDictionary ранее созданный словарь.
     * @return              новую отсортированную версию словаря с добавлением слов, полученных из входной строки.
     */
    private static String[] addToDictionary(String input, String[] oldDictionary) {
        return Stream
                .concat(
                        Arrays.stream(oldDictionary),
                        Arrays.stream(input
                                        .replaceAll("\\p{Punct}", "")
                                        .split(" ")
                                )
                                .filter(s -> !s.isBlank())
                                .map(String::toLowerCase)
                )
                .distinct()
                .sorted()
                .toArray(String[]::new);

    }
    // тело функции представляет собой монаду, функция детерминирована и чиста.

    /**
     * Создаёт (по крайней мере пытается) файл, содержащий строку из всех слов переданного словаря.
     * @param savePath          адрес файла, в который записывается строка.
     * @param dictionaryToSave  массив строк, который будет записан в файл.
     */
    private static void saveDictionaryToFile(String savePath, String[] dictionaryToSave) {

        try(FileWriter saver = new FileWriter(savePath, false)) {
            Arrays.stream(dictionaryToSave).forEach(x -> {
                try {
                    saver.write(x);
                    saver.append(" ");
                } catch (IOException e) {
                    System.out.println("Ошибка записи: " + e.getMessage());
                }
            });
            saver.flush();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения:" + e.getMessage());
        }
        System.out.println("Словарь сохранён в файл " + savePath);
    }
    // Функция детерминирована: одинаковый ввод в норме вызывает одинаковый результат,
    // но опосредованно средствами ОС. Побочный эффект заключается в операции вывода на диск
    // и на экран, а также в обработке исключений.

    /**
     * Получает адрес файла из текста команды.
     * @param input строка ввода.
     * @return      адрес файла из текста строки.
     * @throws IllegalArgumentException если в строке не распознано поле адреса.
     */
    private static String extractPath(String input) throws IllegalArgumentException {
        String[] members = input.split(" ");
        if (members.length != 2) {
            throw new IllegalArgumentException("Путь не распознан!");
        }
        return members[1];
    }
    // Функция детерминирована: одинаковый ввод в норме даёт одинаковый результат.
    // Побочный эффект заключается в обработке исключения.


    /**
     * Читает содержимое файла и трактует его как ввод для добавления к существующему Словарю новых слов.
     * @param sourcePath    адрес файла, с которого читается.
     * @param oldDictionary массив, к которому добавляются новые слова.
     * @return              новую отсортированную версию словаря с добавлением слов, полученных из файла.
     * @throws IOException при ошибках чтения.
     */
    private static String[] addFileToDictionary(String sourcePath, String[] oldDictionary) throws IOException {
        return addToDictionary(new String(Files.readAllBytes(Paths.get(sourcePath))), oldDictionary);
    }
    // Функция детерминирована, побочный эффект заключается во вводе с диска и выбросе исключения.

    /**
     * Выводит на экран все слова из массива каждое с новой строки
     * или сообщение о пустом словаре, если массив пуст.
     * @param dictionary    отображаемый массив слов.
     */
    private static void showDictionary(String[] dictionary) {
        if (dictionary.length == 0) {
            System.out.println("Словарь пуст.");
        } else {
            Arrays.stream(dictionary).forEach(System.out::println);
        }
    }
    // Функция детерминирована. Побочный эффект заключается в выводе на экран.

}
