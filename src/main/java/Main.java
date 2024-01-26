
import io.restassured.RestAssured;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Thread.sleep;


public class Main {

    public static String token;

    private static List<String> tokenList;
    private static int currentIndex;

    static Map<String, String> NameIdPairs = new HashMap<>();
    public static void main(String[] args) throws Exception {
        createTokenArray();
        ImportExcel.Import();

        ProductList.ExcelReaderExample();


        while (true) {
            List<Thread> threads = new ArrayList<>();

            for (Map.Entry<String, String> entry : NameIdPairs.entrySet()) {
                String token = getNewToken();
                Thread thread = new Thread(new ApiThread(entry.getValue(),entry.getKey(),token));

                thread.start();
                threads.add(thread);
            }


            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            sendToTelegram();
            LocalTime currentTime = LocalTime.now();
            System.out.println("Test" + " " + currentTime);
            sleep(6000);
        }
    }


    static final ArrayList<String> outputArray = new ArrayList<String>();
    static HashMap<String, String> outputArrayNew = new HashMap<>();

    public static void sendToTelegram() throws IOException, InterruptedException {
        String separator = "===================";
        String chat_id = "-1001953749462";
        String bot_token = "bot6444986965:AAGDYVU7sNwLiVLxm-fobgoJoiocE7QBoDo";

        StringJoiner joiner = new StringJoiner("\n");
        if (!outputArray.isEmpty()) {
            for (String item : outputArray) {
                joiner.add(separator + "\n"  + item.toString());

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

        File myObjToken = new File("C:\\Users\\Artem\\IdeaProjects\\ScMonitorGit\\out\\artifacts\\ScMonitor_jar\\token.txt");
        Scanner sc = new Scanner(myObjToken);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            tokenList.add(line);
        }
        sc.close();
    }

}
