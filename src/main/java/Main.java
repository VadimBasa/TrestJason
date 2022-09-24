import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Хлеб", "Яблоки", "Молоко", "Яйца", "Колбаса"};
        int[] prices = {100, 200, 300, 150, 400};
        Basket pleyShop = new Basket(prices, products);
        ClientLog clientLog = new ClientLog(prices, products);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("shop.xml"));
            Element loadXml = doc.getDocumentElement();
            //Node root = doc.getDocumentElement();

            Element load = (Element) loadXml;
            Element save = (Element) loadXml;
            Element log = (Element) loadXml;
            String textFileBasket = "basket.txt";
            File basketText = new File("basket.txt");
            String jsonFileBasket = "basket.json";
            File basketJsonFile = new File("basket.json");
            File txtFile = new File("log.csv");

            JSONObject basketJson = new JSONObject();//("basket.json");
            File fileJson = new File(load.getElementsByTagName("fileName").item(1).getTextContent());
            String loadBasketJason = load.getElementsByTagName("enabled").item(0).getTextContent();
            if (loadBasketJason.equals("true")) {
                if (basketJsonFile.exists()) {
                    clientLog.loadJson();
                    System.out.println("Файл json загружен");
                    pleyShop.printCart();
                }
            } else if (loadBasketJason.equals("false")) {
                System.out.println("Загружать корзину с прошлого сеанса не требуется");
            } else {
                try {
                    if (fileJson.createNewFile()) {
                        System.out.println("Файл basket.jason создан");
                    }
                } catch (IOException e) {
                    System.out.println("Папка " + jsonFileBasket + " отсутствует.");
                    throw new RuntimeException(e);
                }
            }
            if (basketText.exists()) {
                System.out.println("У Вас есть корзина с прошлого сеанса в формате txt");
                pleyShop.loadFromTextFile(basketText);
            } else {
                try {
                    if (basketText.createNewFile()) {
                        System.out.println("Создана новая корзина");
                    }
                } catch (IOException e) {
                    System.out.println("Папка " + textFileBasket + " отсутствует.");
                    throw new RuntimeException(e);
                }
            }

            System.out.println("Список возможных товаров для покупки");
            for (int i = 0; i < products.length; i++) {
                System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
            }
            while (true) {
                int productNumber = 0;
                int productCount = 0;
                System.out.println("Выберите товар и количество или введите `end`");
                String inputString = scanner.nextLine(); // Считываем номер операции
                if (inputString.equals("end")) {
                    if (save.getElementsByTagName("enabled").item(1).getTextContent().equals("true")) {
                        clientLog.saveJson();
                    }
                    pleyShop.printCart();

                    if (log.getElementsByTagName("enabled").item(1).getTextContent().equals("true")) {

                        clientLog.exportAsCSV(txtFile);

                        System.out.println("Журнал действий пользователя сохранен в client.csv");
                    }
                    break;
                }

                String[] inputProduct = inputString.split(" ");
                productNumber = Integer.parseInt(inputProduct[0]) - 1;
                productCount = Integer.parseInt(inputProduct[1]);
                pleyShop.addToCart(productNumber, productCount);
                pleyShop.saveTxt(new File(textFileBasket));//
                clientLog.log(productNumber, productCount);
                pleyShop.printCart();
            }
        } catch (SAXException | ParserConfigurationException | ParseException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
}

