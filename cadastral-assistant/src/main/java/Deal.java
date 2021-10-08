/**
 * Структура данных, представляющая сделку: ширина, длина и стоимость.
 */
public class Deal {
    private final double width;
    private final double length;
    private final double cost;
    // поля задаются раз и навсегда

    /**
     * Новая сделка.
     * @param width  ширина участка.
     * @param length длина участка.
     * @param cost   стоимость сделки.
     */
    public Deal(double width, double length, double cost) {
        this.width = width;
        this.length = length;
        this.cost = cost;
    }

    /**
     * Выдаёт для данной сделки цену за единицу площади.
     * @return частное от деления стоимости на произведение ширины и длины.
     */
    public double getPrice() {
        return cost / (width * length);
    }
    // чистая функция, к тому же выдающая константный результат,
    // поэтому может быть кеширована

    /**
     * Текстовое представление сделки.
     * @return  описание: #id (ширина*длина), стоимость.
     */
    @Override
    public String toString() {
        return "Сделка №%d: (%.2fм x %.2fм): стоимость: %.2f.".formatted(hashCode(), width, length, cost);
    }
    // чистый поставщик строки, выдающий константу
}
