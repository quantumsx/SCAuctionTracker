import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
    static List<Product> productList = new ArrayList<>();
    public ProductList() {
        ProductList.productList = productList;
    }

    public static void ExcelReaderExample() throws Exception {

       List<Product> productList = readProductsFromExcel("C:\\Users\\Artem\\Desktop\\SCAuction\\ItemPrices.xlsx");

    }


    public static List<Product> readProductsFromExcel(String filePath) {



        try {
            Workbook workbook = new Workbook(filePath);
            Worksheet worksheet = workbook.getWorksheets().get(0);

            int maxRow = worksheet.getCells().getMaxDataRow();

            for (int row = 1; row <= maxRow; row++) {
                Row currentRow = worksheet.getCells().getRows().get(row);

                Product product = Product.createProductFromRow(currentRow);
                productList.add(product);
            }

            workbook.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }
}


