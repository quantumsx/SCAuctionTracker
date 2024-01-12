
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.Cells;
import com.aspose.cells.Cell;
public class Main {

    public static String token;
    public static Date TimeStamp = new Date();

    public static String key;

    public static String value;

    public static void main(String[] args) throws IOException, InterruptedException, FileNotFoundException, ExecutionException {
        ItemDB.ItemApiDB();

        File myObjPrice = new File("C:\\Users\\Artem\\IdeaProjects\\ScMonitorGit\\out\\artifacts\\ScMonitor_jar\\price.txt");
        File myObjToken = new File("C:\\Users\\Artem\\IdeaProjects\\ScMonitorGit\\out\\artifacts\\ScMonitor_jar\\token.txt");


        Scanner sc = new Scanner(myObjToken);
        token = sc.nextLine();


        Scanner sc2 = new Scanner(myObjPrice);
        while (sc2.hasNextLine()) {
            String data = sc2.nextLine();
            if (data.equals("")) {
                break;
            }
            String[] arrayOfStrings = data.split(" ");

            ItemInput(arrayOfStrings[0], arrayOfStrings[1]);
        }

        sc2.close();

        String i = "4lml";
        String j = "60000";

        ApiCall.ApiDescription(i, j);
        sendToTelegram();

    }


    static HashMap<String, String> InputItemDB = new HashMap<>();

    static Set<String> InputItemDBKey = InputItemDB.keySet();

    public static void ItemInput(String firstArg, String secondArg) {

        InputItemDB.put(firstArg, secondArg);

    }

    public static String InputItemDBGetValue(String itemName) {

        return InputItemDB.get(itemName);
    }

    static ArrayList<String> outputArray = new ArrayList<String>();

    public static void sendToTelegram() throws IOException, InterruptedException {
        String separator = "===================";
        String chat_id = "-1001953749462";
        String bot_token = "bot6444986965:AAGDYVU7sNwLiVLxm-fobgoJoiocE7QBoDo";

        StringJoiner joiner = new StringJoiner("\n");
        for (String item : outputArray) {
            joiner.add(separator + "\n" + item.toString());
        }
        joiner.add(separator);

        String myString = joiner.toString();


        String chatUrl = "https://api.telegram.org/" + bot_token + "/sendMessage";

        RestAssured.given()
                .queryParam("chat_id", chat_id)
                .queryParam("text", myString)
                .post(chatUrl);
    }

    public static void excelImport() throws Exception {

        Workbook workbook = new Workbook("ItemPrices.xlsx"); //Возможно полный путь нужно
        Worksheet worksheet = workbook.getWorksheets().get(0);
        Cells cells = worksheet.getCells();

        int nonEmptyCellCountInFirstColumn = 0;
        int columnIndex = 0;

        for (int row = 0; row <= cells.getMaxDataRow(); row++) {
            Cell currentCell = cells.get(row, columnIndex);

            if (currentCell.getValue() != null && !currentCell.getStringValue().trim().isEmpty()) {
                nonEmptyCellCountInFirstColumn++;
            } else {
                break;
            }
        }


    }
}

