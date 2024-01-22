import com.aspose.cells.*;

public class Product {
    private String name;
    private long price;
    private int quality;
    private int potential;

    public Product(String name, long price) {
        this(name, price, 0, 0);
    }

    public Product(String name, long price, int quality, int potential) {
        this.name = name;
        this.price = price;
        this.quality = quality;
        this.potential = potential;
    }

    public static Product createProductFromRow(Row row) {
        {
            String name = row.get(0).getStringValue().trim();
            long price = (long) row.get(1).getDoubleValue();
            int quality = 0;
            int potential = 0;

            if (row.get(2).getType() == com.aspose.cells.CellValueType.IS_NUMERIC) {
                quality = (int) row.get(2).getDoubleValue();
            }

            if (row.get(3).getType() == com.aspose.cells.CellValueType.IS_NUMERIC) {
                potential = (int) row.get(3).getDoubleValue();
            }

            return new Product(name, price, quality, potential);
        }
    }


    public boolean artefactFilter(String apiName, long apiPrice, int apiQuality, int apiPotential) {
        return name.equals(apiName) &&
                ((apiPrice <= price) && (apiPrice != 0) ) &&
                (apiQuality == quality) &&
                (apiPotential == potential);
    }
    public boolean weaponFilter(String apiName, long apiPrice) {
        return name.equals(apiName) &&
                (apiPrice <= price && apiPrice != 0);
    }

}


