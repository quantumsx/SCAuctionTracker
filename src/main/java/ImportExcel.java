import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImportExcel {

    public static void Import() throws Exception {

        Workbook workbook = new Workbook("C:\\Users\\Artem\\Desktop\\ItemPrices.xlsx");
        Worksheet worksheet = workbook.getWorksheets().get(0);
        Cells cells = worksheet.getCells();

        int columnIndex = 0;
        String itemID = null;

        String[] itemTypeList = new String[]{"Armor", "Artifacts", "Attachments", "Containers", "Misc", "Other", "Weapon"};
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")){
        MongoDatabase database = mongoClient.getDatabase("ItemList");

        for (int row = 1; row <= cells.getMaxDataRow(); row++) {
            Cell currentCell = cells.get(row, columnIndex);

            if (currentCell.getValue() != null && !currentCell.getStringValue().trim().isEmpty()) {

                String cellValue = currentCell.getStringValue().trim();

                if (!Main.NameIdPairs.containsKey(cellValue)) {

                    for (String collectionName : itemTypeList) {
                        MongoCollection<Document> collection = database.getCollection(collectionName);

                        Document filter = new Document("ItemName", cellValue);

                        FindIterable<Document> result = collection.find(filter);

                        for (Document document : result) {
                            itemID = document.getString("ItemID");
                            Main.NameIdPairs.put(cellValue, itemID);
                            if (itemID != null) {
                                itemID = null;
                                break;
                            }
                        }
                        if (itemID != null) {
                            break;
                        }
                    }
                }
            }
        }
        }
        }
    }

