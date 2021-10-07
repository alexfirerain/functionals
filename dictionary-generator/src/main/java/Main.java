import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

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
                    dictionary = addFileToDictionary(extractPath(input));

                } else if (input.startsWith("/save ")) {
                    saveDictionaryToFile(extractPath(input));

                } else {
                    dictionary = addToDictionary(input);
                }
            } catch (Exception e) {
                System.out.println("Ошибка команды:" + e.getMessage());
            }
        }
        System.out.println("До свидания!");
    }

    private static String[] addToDictionary(String input) {

        String[] r =
                Arrays.stream(input
                        .replaceAll("\\p{Punct}", "")
                        .split(" "))
                .filter(s -> !s.isBlank())
                .distinct()
                .sorted()
                .map(String::toLowerCase)
                .toArray(String[]::new);

        System.out.println("New deal: " + Arrays.toString(r));


        String[] portion = input.split(" ");
        String[] newDic = new String[dictionary.length + portion.length];
        System.arraycopy(dictionary, 0, newDic, 0, dictionary.length);
        System.arraycopy(portion, 0, newDic, dictionary.length, portion.length);
        Arrays.sort(newDic);


        return newDic;

//        return Stream.concat(
//                        Arrays.stream(dictionary),
//                        Arrays.stream(input.chars()
//                                .filter(codePoint ->
//                                        Character.isSpaceChar(codePoint) || Character.isLetter(codePoint))
//                                .toString()
//                                .split(" ")
//                        ).filter(String::isBlank)
//                ).sorted()
//                .toArray(String[]::new);
    }

    private static void saveDictionaryToFile(String savePath) {

        try(FileWriter saver = new FileWriter(savePath, false)) {
            Arrays.stream(dictionary).forEach(x -> {
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

    private static String extractPath(String input) throws IllegalArgumentException {
        String[] members = input.split(" ");
        if (members.length != 2) {
            throw new IllegalArgumentException("Путь не распознан!");
        }
        return members[1];
    }

    private static String[] addFileToDictionary(String sourcePath) throws IOException {
        return addToDictionary(new String(Files.readAllBytes(Paths.get(sourcePath))));
    }

    private static void showDictionary() {
        if (dictionary.length == 0) {
            System.out.println("Словарь пуст.");
        } else {
            Arrays.stream(dictionary).forEach(System.out::println);
        }
    }
}
