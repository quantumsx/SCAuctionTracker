import io.restassured.RestAssured;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {

    public static String token;

    private static List<String> tokenList;
    private static int currentIndex;

    static Map<String, String> NameIdPairs = new HashMap<>();

    public static void main(String[] args) throws Exception {
        createTokenArray();
        ImportExcel.Import();

        ProductList.ExcelReaderExample();

        int numThreads = 3;


        List<Runnable> tasks = new ArrayList<>();

        while (true) {
            tasks.clear();

            for (Map.Entry<String, String> entry : NameIdPairs.entrySet()) {
                String token = getNewToken();
                tasks.add(new ApiThread(entry.getValue(), entry.getKey(), token));

            }

            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            for (Runnable task : tasks) {
                executor.submit(task);
            }

            executor.shutdown();

            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ScanImage scanImage = new ScanImage();

            if(!outputArrayNew.isEmpty()) {

                Map.Entry<String, Long> firstEntry = outputArrayNew.entrySet().iterator().next();

                String key = firstEntry.getKey();
                Long value = firstEntry.getValue();

                System.out.println(value);
                scanImage.pressButtons(key, String.valueOf(value));

                outputArrayNew.clear();
            }

            sendToTelegram();
            LocalTime currentTime = LocalTime.now();
            System.out.println("Test" + " " + currentTime);

            // Thread.sleep (5000);

        }
    }


    static final ArrayList<String> outputArray = new ArrayList<String>();
    static Map<String, Long> outputArrayNew = new HashMap<>();

    public static void sendToTelegram() throws IOException, InterruptedException {
        String separator = "===================";
        String chat_id = "-1001953749462";
        String bot_token = "bot6444986965:AAGDYVU7sNwLiVLxm-fobgoJoiocE7QBoDo";

        StringJoiner joiner = new StringJoiner("\n");
        if (!outputArray.isEmpty()) {
            for (String item : outputArray) {
                joiner.add(separator + "\n" + item.toString());

            }
            joiner.add(separator);
        }


        String myString = joiner.toString();


        String chatUrl = "https://api.telegram.org/" + bot_token + "/sendMessage";

        RestAssured.given()
                .queryParam("chat_id", chat_id)
                .queryParam("text", myString)
                .queryParam("parse_mode", "HTML")
                .post(chatUrl);

        outputArray.clear();
    }

    public static String getNewToken() {
        String currentToken = tokenList.get(currentIndex);

        currentIndex = (currentIndex + 1) % tokenList.size();
        return currentToken;
    }

    public static void createTokenArray() throws FileNotFoundException {
        tokenList = new ArrayList<>();

        File myObjToken = new File("C:\\Users\\Artem\\Desktop\\SCAuction\\token.txt");
        Scanner sc = new Scanner(myObjToken);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            tokenList.add(line);
        }
        sc.close();
    }
}
