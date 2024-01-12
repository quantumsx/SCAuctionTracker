
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


    static AuctionList.AuctionItem auctionItem = new AuctionList.AuctionItem();
    static AuctionList.AuctionItem.AdditionalInfo additionalInfo = new AuctionList.AuctionItem.AdditionalInfo();

    static int itemType;
    static String separator = "===================";

    static int ptnStart;
    static int ptnEnd;
    static int ptnSingle;

    public static void ApiDescription(String NewItemID, String NewValue, int... potentialRange) throws IOException, InterruptedException {

        LocalTime now = LocalTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        RestAssured.baseURI = "https://eapi.stalcraft.net/ru/auction/";

        AuctionList auctionList = given()
                .contentType(ContentType.JSON)
                .header("HostName", "eapi.stalcraft.net")
                .header("Authorization", "Bearer" + " " + Main.token)
                .when()
                .get(NewItemID + "/lots?sort=buyout_price&order=asc&limit=200&additional=true")
                .then()
                .extract().as(AuctionList.class);




        if (auctionList.getLots().size() != 0) {
            System.out.println(separator);
            for (AuctionList.AuctionItem i : auctionList.getLots()) {

                String itemName = ItemDB.ItemApiValue(i.getItemId());
                String buyoutPriceFormatted = String.format(Locale.US, "%,d", i.getBuyoutPrice());

                String commonPart = dtf.format(now) + "\n" + itemName + " " + buyoutPriceFormatted;
                String additionalPart = " " + i.getAmount() + " (" + (i.getBuyoutPrice() / i.getAmount()) + ")";

                String textToSend;
                if (i.getBuyoutPrice() != 0){
                    if ((i.getBuyoutPrice() / i.getAmount()) <= Integer.parseInt(NewValue)) { //
                        int ptnSend = isPtnSend(potentialRange);
                        if (((ptnSend == 2) && (artefactPotential(i, ptnStart, ptnEnd))) || ((ptnSend == 1) && (artefactPotential(i, ptnSingle))) || (ptnSend == 0)) {
                            if (i.getBuyoutPrice() != 0) {
                            switch (itemType()) {
                                case 0 -> {
                                    if (i.getAmount() == 1) {
                                        System.out.println(commonPart);
                                        textToSend = commonPart;
                                    } else {
                                        System.out.println(commonPart + additionalPart);
                                        textToSend = commonPart + additionalPart;
                                    }
                                }
                                case 1 -> {
                                    System.out.println(commonPart + " " + artefactPercentageCounter(i) + " +" + i.getAdditional().getPtn());
                                    textToSend = commonPart + " " + artefactPercentageCounter(i) + " +" + i.getAdditional().getPtn();
                                }
                                default -> {
                                    System.out.println("Unhandled itemType: " + itemType);
                                    textToSend = "Unhandled itemType: " + itemType;
                                }
                            }
                            Main.outputArray.add(textToSend);
                            System.out.println(separator);
                        }
                    }
                }
                    }
                }
            }
            }



    public static String artefactPercentageCounter(AuctionList.AuctionItem i) {
        DecimalFormat decimalFormat = new DecimalFormat( "#.##" );



        if ((i.getAdditional().getQlt() > 0) && (i.getAdditional().getStats_random() != 0.0)) {
            return decimalFormat.format(i.getAdditional().getQlt() * 10 + (i.getAdditional().getStats_random() + 42) / 0.4) + "%";
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

    public static boolean artefactPotential(AuctionList.AuctionItem i, int potential) {
        if (i.getAdditional().getPtn() == potential) {
            return true;
        }
        return false;
    }
    public static boolean artefactPotential(AuctionList.AuctionItem i, int startRange, int endRange) {

        int itemPotential = i.getAdditional().getPtn();
        return itemPotential >= startRange && itemPotential <= endRange;
    }

    public static int isPtnSend(int... potentialRange) {
        if (potentialRange.length == 2) {
            ptnStart = potentialRange[0];
            ptnEnd = potentialRange[1];
            return 2;
        } else if (potentialRange.length == 1) {
            ptnSingle = potentialRange[0];
            return 1;
        } else {
            return 0;
        }
    }

    public static int itemType() {
        if (additionalInfo.getUpgrade_bonus() != 0.0) {
                return 0; // оружие/армор
            } else {
                return 1; //артефакт
            }
        }

    }


