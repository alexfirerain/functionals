import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.*;
import java.util.stream.Stream;

public class MainFunctionalist {
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

    public static void main(String[] args) {
        System.out.println(WELCOME);
        while(true) {
            try {
                String input = scanner.nextLine();

                if ("/exit".equals(input)) break;

                if ("/log".equals(input)) {
                    dicPrinter.accept(dictionary);

                } else if (input.startsWith("/txt ")) {
                    dictionary = fileAdder.apply(pathExtractor.apply(input), dictionary);

                } else if (input.startsWith("/save ")) {
                    fileSaver.accept(pathExtractor.apply(input));

                } else {
                    dictionary = inputAdder.apply(input, dictionary);
                }
            } catch (Exception e) {
                System.out.println("Ошибка команды:" + e.getMessage());
            }
        }
        System.out.println("До свидания!");
    }

    /**
     * Трансформирует переданную строку в сортированный массив слов.
     */
    private static final Function<String, String[]> dictionarize =
            x -> Arrays.stream(x.replaceAll("\\p{Punct}", "")
                            .split(" "))
                    .filter(s -> !s.isBlank())
                    .map(String::toLowerCase)
                    .distinct()
                    .sorted()
                    .toArray(String[]::new);
    // Эта функция не используется, так как функционал полностью поглощён inputAdder и fileAdder,
    // обеспечивающими добавление слов к существующему списку.
    // Альтернативный способ состоит в использовании Function dictionarize
    // совместно с BinaryOperator uniter. Но в нём происходит необязательный повтор некоторых вызовов.

    /**
     * Сливатель двух массивов в новый сортированный словарь
     */
    private static final BinaryOperator<String[]> uniter = (dic1, dic2) ->
            Stream.concat(
                Arrays.stream(dic1), Arrays.stream(dic2)
            )
            .distinct()
            .sorted()
            .toArray(String[]::new);

    /**
     * Добавлятель слов из новой строки ввода к существующему словарю.
     * inputAdder.apply(input, oldDic) эквивалентно uniter.apply(dictionarize.apply(input), oldDic)
     */
    private static final BiFunction<String, String[], String[]> inputAdder = (input, oldDic) -> Stream
            .concat(
                    Arrays.stream(oldDic),
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

    /**
     * Сохранятель статического массива Словаря в файл по переданному адресу.
     */
    private static final Consumer<String> fileSaver = path -> {
        try(FileWriter saver = new FileWriter(path, false)) {
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
        System.out.println("Словарь сохранён в файл " + path);
    };

    /**
     * Вычленятель адреса из текста команды.
     */
    private static final UnaryOperator<String> pathExtractor = input -> {
        String[] members = input.split(" ");
        if (members.length != 2) {
            throw new IllegalArgumentException("Путь не распознан!");
        }
        return members[1];
    };


    /**
     * Добавлятель в Словарь слов из файла.
     */
    private static final BiFunction<String, String[], String[]> fileAdder = (path, oldDic) ->
    {
        try {
            return inputAdder.apply(new String(Files.readAllBytes(Paths.get(path))), oldDic);
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
        return oldDic;
    };

    /**
     * Построчный выводитель в консоль содержимого массива строк.
     */
    private static final Consumer<String[]> dicPrinter = dic -> {
        if (dictionary.length == 0) {
            System.out.println("Словарь пуст.");
        } else {
            Arrays.stream(dictionary).forEach(System.out::println);
        }
    };


}
