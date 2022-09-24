import java.io.*;

public class Basket implements Serializable {
    protected final int[] prices;
    protected final String[] products;
    public static int[] marcetProduct;

    Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        marcetProduct = new int[products.length];
    }

    public void addToCart(int productNum, int amount) {//метод добавления штук в корзину
        marcetProduct[productNum] += amount; // создаю массив корзины и добавляю в ячеку количество товара
    }

    public void printCart() {// метод вывода корзины на экран
        System.out.println("Ваша корзина:");
        int TotalsumProducts = 0;
        for (int i = 0; i < products.length; i++) {
            if (marcetProduct[i] > 0) {
                int sumProduct = marcetProduct[i] * prices[i];
                System.out.println(products[i] + " " + marcetProduct[i] + " шт " + prices[i] + " руб/шт " + sumProduct + " руб в сумме");
            }
            TotalsumProducts += prices[i] * marcetProduct[i];
        }
        System.out.println("Итого: " + TotalsumProducts + " руб");
    }

    public void saveTxt(File textFile) throws IOException {//метод сохранения корзины в текстовый файл

        try (PrintWriter out = new PrintWriter(new FileWriter(textFile))) {

            for (int e : marcetProduct)
                out.print(e + " ");
        } catch (IOException e) {
            System.out.println("Файл или путь basket.txt отсутствует");
            throw new RuntimeException(e);
        }
    }

    protected static int[] loadFromTextFile(File textFile) throws IOException {///метод восстановления объекта корзины из текстового файла
        try (BufferedReader in = new BufferedReader(new FileReader(textFile))) {
            String[] itemSplit = in.readLine().split(" ");
            for (int i = 0; i < itemSplit.length; i++) {
                marcetProduct[i] = Integer.parseInt(itemSplit[i]);
            }
            return marcetProduct;
        }
    }
}