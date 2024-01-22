
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static io.restassured.RestAssured.given;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ApiCall {


    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static void ApiDescription(String NewItemID, String NewItemName, String token) throws IOException, InterruptedException {

        RestAssured.baseURI = "https://eapi.stalcraft.net/ru/auction/";

        AuctionList auctionList = given()
                .contentType(ContentType.JSON)
                .header("HostName", "eapi.stalcraft.net")
                .header("Authorization", "Bearer" + " " + token)
                .when()
                .get(NewItemID + "/lots?sort=buyout_price&order=desc&limit=200&additional=true")
                .then()
                .extract().as(AuctionList.class);




        if (auctionList.getLots().size() != 0) {

            for (AuctionList.AuctionItem item : auctionList.getLots()) {
                filterAuctionItem(item, NewItemName);
            }
        }
    }


    private static void filterAuctionItem(AuctionList.AuctionItem item, String NewItemName) {

        LocalTime currentTime = LocalTime.now();

        String buyoutPriceFormatted = String.format(Locale.US, "%,d", item.getBuyoutPrice());

        String commonPart = dtf.format(currentTime) + "\n" + "<code>" + NewItemName + "</code>" + " " + buyoutPriceFormatted;
        String additionalPart = " " + item.getAmount() + " (" + (item.getBuyoutPrice() / item.getAmount()) + ")";

        String textToSend;


        for (Product product : ProductList.productList) {

            switch (itemType(item)) {
                    case 0 -> { //оружие
                        if (item.getAmount() == 1) {
                            if (product.weaponFilter(NewItemName, item.getBuyoutPrice())) {
                                System.out.println(commonPart);
                                textToSend = commonPart;
                                synchronized(Main.outputArray){
                                    Main.outputArray.add(textToSend);
                                }
                                //Main.outputArrayNew.put(NewItemName,textToSend);
                            }
                        } else {
                            if (product.weaponFilter(NewItemName, item.getBuyoutPrice() / item.getAmount())) {
                                System.out.println(commonPart + additionalPart);
                                textToSend = commonPart + additionalPart;
                                synchronized(Main.outputArray){
                                    Main.outputArray.add(textToSend);
                                }
                                //Main.outputArrayNew.put(NewItemName,textToSend);
                            }
                        }
                    }
                    case 1 -> { //артефакты
                        if (product.artefactFilter(NewItemName, item.getBuyoutPrice(), item.getAdditional().getQlt(), item.getAdditional().getPtn())) {
                            System.out.println(commonPart + " " + artefactPercentageCounter(item) + " +" + item.getAdditional().getPtn());
                            textToSend = commonPart + " " + artefactPercentageCounter(item) + " +" + item.getAdditional().getPtn();
                            synchronized(Main.outputArray){
                                Main.outputArray.add(textToSend);
                            }
                        }
                    }
                    }
            }
        }

    public static String artefactPercentageCounter(AuctionList.AuctionItem i) {
        DecimalFormat decimalFormat = new DecimalFormat( "#.##" );



        if ((i.getAdditional().getQlt() > 0) && (i.getAdditional().getStats_random() != 0.0)) {
            return decimalFormat.format((i.getAdditional().getQlt() - 1) * 10 + (i.getAdditional().getStats_random() + 42) / 0.4) + "%";
        }
        else if ((i.getAdditional().getQlt() > 0) && (i.getAdditional().getStats_random() == 0)) {
            return "Не изучен";
        }
        else if ((i.getAdditional().getQlt() == 0)  & (i.getAdditional().getStats_random() != 0.0)){
            return decimalFormat.format((i.getAdditional().getStats_random() + 2) / 0.04) + "%";
        }
        else {
            return "Не изучен";
        }
    }


    public static int itemType(AuctionList.AuctionItem item) {
        if (item.getAdditional().getUpgrade_bonus() == null) {
                return 0;
            } else {
                return 1;
            }
        }


    }


