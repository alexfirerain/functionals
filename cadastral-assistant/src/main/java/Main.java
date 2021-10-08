import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.*;
import java.util.stream.IntStream;

public class Main {
    // пороговые константы для генерации случайных сделок:
    private static final double MIN_EXTENSION = 0.1;
    private static final double MAX_EXTENSION = 3000;
    private static final double MAX_RATIO = 6;
    private static final double MEAN_PRICE = 127;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Программа анализа честности сделок.");
        boolean run = true;
        while(run) {
            final int threshold = valueInput.apply("Введите рекомендованную цену для определения честности: ");
            final int dealsNumber = valueInput.apply("Введите, сколько сделок проанализировать: ");
            final Deal[] dealsBatch = dealsArchive.apply(dealsNumber);
            System.out.println("""
                    Чтобы вывести сделки честные (с ценой выше рекомендованной) введите '+'
                    Чтобы вывести сделки нечестные (с ценой ниже рекомендованной), введите '-'
                    Чтобы проанализировать новый список сделок, введите 'more'
                    Чтобы завершить выполнение программы, введите 'end'""");
            boolean analyze = true;
            while (analyze) {
                switch (scanner.next()) {
                    case "+":
                        fairPrinter.accept(dealsBatch, threshold);
                        break;
                    case "-":
                        unfairPrinter.accept(dealsBatch, threshold);
                        break;
                    case "end":
                        run = false;
                    case "more":
                        analyze = false;
                        break;
                    default:
                        System.out.println("?");
                }
            }
        }
    }

    /**
     * Сообщает, является ли сделка честной.
     * Возвращает true, если цена выше или равна порогу, иначе false.
     */
    private static final BiPredicate<Deal, Integer> isFair =
            (deal, threshold) -> deal.getPrice() >= threshold;
    // является детерминированной и чистой (результат полностью определён аргументами)


    /**
     * Выводит на консоль те сделки из переданного массива, цена за единицу
     * площади в которых выше или равна переданному порогу.
     */
    private static final BiConsumer<Deal[], Integer> fairPrinter = (deals, threshold) -> {
        System.out.println("Честные сделки:");
        Arrays.stream(deals).filter(deal -> isFair.test(deal, threshold)).forEach(System.out::println);
    };
    // детерминированная функция (выдаёт одинаковый результат при одинаков вводе)
    // чистой не является, так как производит вывод на экран

    /**
     * Выводит на консоль те сделки из переданного массива, цена за единицу
     * площади в которых ниже, чем переданный порог.
     */
    private static final BiConsumer<Deal[], Integer> unfairPrinter = (deals, threshold) -> {
        System.out.println("Нечестные сделки:");
        Arrays.stream(deals).filter(deal -> !isFair.test(deal, threshold)).forEach(System.out::println);
    };
    // детерминированная функция (выдаёт одинаковый результат при одинаков вводе)
    // чистой не является, так как производит вывод на экран


    /**
     * Печатает в консоль переданное приглашение и возвращает прочитанное с консоли целое число.
     */
    private static final Function<String, Integer> valueInput = prompt -> {
        System.out.print(prompt);
        return scanner.nextInt();
    };
    // не является детерминированной, так как результат определяется пользователем,
    // имеет побочный эффект в виде операций вывода и ввода

    /**
     * Порождает случайную сделку в пределах параметров статических констант
     * (наибольшее и наименьшее измерение, предельное соотношение размерностей и средняя цена).
     */
    private static final Supplier<Deal> generateDeal = () -> {
        Random random = new Random();
        double width = random.nextDouble() * (MAX_EXTENSION - MIN_EXTENSION) + MIN_EXTENSION;
        double length;
        do {
            length = random.nextDouble() * (MAX_EXTENSION - MIN_EXTENSION) + MIN_EXTENSION;
        } while (length < width / MAX_RATIO || length > width * MAX_RATIO);
        double cost = width * length * (
                (random.nextDouble() > 0.5) ?
                random.nextDouble() * MEAN_PRICE + MEAN_PRICE :
                random.nextDouble() * MEAN_PRICE / 2 + MEAN_PRICE / 2
        );
        return new Deal(width, length, cost);
    };
    // функция не детерминирована, так как призвана генерировать случайный объект
    // побочных эффектов не имеет, так как ничего не модифицирует,
    // хотя и обращается к глобальным переменным

    /**
     * Порождает массив случайных сделок, размер определяется переданным числом.
     */
    private static final Function<Integer, Deal[]> dealsArchive = number ->
            IntStream.range(0, number)
                    .mapToObj(i -> generateDeal.get())
                    .toArray(Deal[]::new);
    // детерминирована в том смысле, что выдаёт массив определённой длины,
    // однако элементы массива генерируются поставщиком generateDeal случайно
    // побочных эффектов не имеет

}
