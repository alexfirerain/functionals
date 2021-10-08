/**
 * Структура данных, представлющая сделку: ширина, длина и стоимость.
 */
public class Deal {
    private final double width;
    private final double length;
    private final double cost;

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

    /**
     * Текстовое представление сделки.
     * @return  описание: #id (ширина*длина), стоимость.
     */
    @Override
    public String toString() {
        return "Сделка №%d: (%.2f x %.2f), стоимость %.2f.".formatted(hashCode(), width, length, cost);
    }
}
