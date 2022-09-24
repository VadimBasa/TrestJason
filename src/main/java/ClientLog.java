import com.opencsv.CSVWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.Arrays;

public class ClientLog {

    protected final String[] products;
    public static int[] marcetProduct;
    protected final int[] prices;

    ClientLog(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;

        marcetProduct = new int[products.length];
    }

    public void log(int productNum, int amount) {// метод добавления всех действий пользователя в историю
        marcetProduct[productNum] += amount;
    }

    public void saveJson() throws IOException {
        JSONObject basketJson = new JSONObject();
        JSONArray basketJsonArr = new JSONArray();
        for (int k : marcetProduct) {
            basketJsonArr.add(k);
        }
        basketJson.put("marcetProduct", basketJsonArr);
        try (FileWriter basketJsonFile = new FileWriter("basket.json")) {
            basketJsonFile.write(basketJson.toJSONString());
            basketJsonFile.flush();
        }
    }

    public void loadJson() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("basket.json"));
            JSONObject basketJasonObj = (JSONObject) obj;
            JSONArray marcetProduct = (JSONArray) basketJasonObj.get("marcetProduct");
            for (Object baskObj : marcetProduct) {
                for (int i = 0; i < ClientLog.marcetProduct.length; i++) {//this
                    ClientLog.marcetProduct[i] = Integer.parseInt(String.valueOf(baskObj));
                }
            }
        } catch (IOException | NumberFormatException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportAsCSV(File txtFile) { //метод сохранения журнала действий в файл log.csv

        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile, true))) {
            String[] saveStringLine = Arrays.toString(marcetProduct).split(", ");
            writer.writeNext(saveStringLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

