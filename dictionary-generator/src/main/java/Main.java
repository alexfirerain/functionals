import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
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
    private static String[] dictionary = new String[0];

    public static void main(String[] args) {
        System.out.println(WELCOME);
        while(true) {
            try {
                String input = scanner.nextLine();
                if ("/exit".equals(input)) break;
                if ("/log".equals(input)) {
                    showDictionary();
                } else if (input.startsWith("/txt ")) {
                    addFileToDictionary(input);
                } else if (input.startsWith("/save ")) {
                    saveDictionaryToFile(input);
                } else {
                    dictionary = addToDictionary(input);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("До свидания!");
    }

    private static String[] addToDictionary(String input) {
        String[] portion = input.split(" ");
//        Arrays.stream(portion).filter(String::isBlank).;
        String[] newDic = new String[dictionary.length + portion.length];
        // TODO: удалить всю пунктуацию (возможно ещё до разрезания) // в данной версии игнорируется из-за сроков
        System.arraycopy(dictionary, 0, newDic, 0, dictionary.length);
        System.arraycopy(portion, 0, newDic, dictionary.length, portion.length);
        Arrays.sort(newDic);
        return newDic;
    }

    private static void saveDictionaryToFile(String saveCommand) {
        String path;
        try {
            path = extractPath(saveCommand);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        /*
            Записать строки словаря в файл
         */
    }

    private static String extractPath(String input) throws IllegalArgumentException {
        String[] members = input.split(" ");
        if (members.length != 2) {
            throw new IllegalArgumentException("Путь не распознан!");
        }
        return members[1];
    }

    private static void addFileToDictionary(String addCommand) {
        String path;
        try {
            path = extractPath(addCommand);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }        /*
            Открыть поток из файла и сделать из него строку.
            Строку запустить на основной процесс: раскрошить, добавить, упорядочить.
         */
    }

    private static void showDictionary() {
        Arrays.stream(dictionary).forEach(System.out::println);
    }
}
