package pcms.product;

import static pcms.CsvParsingUtil.decode;
import static pcms.CsvParsingUtil.encode;
import static pcms.CsvParsingUtil.splitIntoCols;

import java.util.List;
import java.util.Optional;
import pcms.Model;
import pcms.category.Category;
import pcms.category.CategoryRepository;
import pcms.supplier.Supplier;
import pcms.supplier.SupplierRepository;

/** Models product. */
public final class Product implements Model {
    /** Category repository which help to get category. */
    private static Optional<CategoryRepository> categoryRepository = Optional.empty();
    /** Supplier repository which help to get supplier. */
    private static Optional<SupplierRepository> supplierRepository = Optional.empty();
    
    /** ID. */
    private final String id;
    /** Image. Its a filename. */
    private final String image;
    /** Full name. */
    private final String name;
    /** Brand. */
    private final String brand;
    /** Category ID. */
    private final String categoryId;
    /** Quantity. */
    private final int quantity;
    /** Description. */
    private final String description;
    /** Retail Price. */
    private final double retailPrice;
    /** Discount. E.g. 0.3 for 30% discount. */
    private final double discount;
    /** Supplier ID. */
    private final String supplierId;

    /** Construct from Product.Builder. */
    private Product(final Builder builder) {
        this.id = builder.id;
        this.image = builder.image;
        this.name = builder.name;
        this.brand = builder.brand;
        this.categoryId = builder.categoryId;
        this.quantity = builder.quantity;
        this.description = builder.description;
        this.retailPrice = builder.retailPrice;
        this.discount = builder.discount;
        this.supplierId = builder.supplierId;
    }

    /** Construct from row. */
    public Product(final String row) {
        final List<String> fields = splitIntoCols(row);
        if (fields.size() != 10) {
            throw new IllegalArgumentException("Fields count incorrect.");
        }

        id = fields.get(0);
        image = decode(fields.get(1));
        name = decode(fields.get(2));
        brand = decode(fields.get(3));
        categoryId = fields.get(4);
        quantity = Integer.parseInt(fields.get(5));
        description = decode(fields.get(6));
        retailPrice = Double.parseDouble(fields.get(7));
        discount = Double.parseDouble(fields.get(8));
        supplierId = fields.get(9);
    }

    /** Convert to row. */
    @Override
    public String toRow() {
        return String.join(
                ",",
                id,
                encode(image),
                encode(name),
                encode(brand),
                categoryId,
                Integer.toString(quantity),
                encode(description),
                Double.toString(retailPrice),
                Double.toString(discount),
                supplierId);
    }
    
    @Override
    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public String getSupplierId() {
        return supplierId;
    }
    
    /** Get category. */
    public Category getCategory() {
        return categoryRepository.get().findWithId(categoryId).get();
    }
    
    /** Set category repository. */
    public static void setCategoryRepository(final CategoryRepository repository) {
        categoryRepository = Optional.of(repository);
    }
    
    /** Get supplier. */
    public Supplier getSupplier() {
        return supplierRepository.get().findWithId(supplierId).get();
    }
    
    /** Set category repository. */
    public static void setSupplierRepository(final SupplierRepository repository) {
        supplierRepository = Optional.of(repository);
    }

    /** Builder. */
    public static class Builder implements Model.Builder {
        /** ID. */
        private String id;
        /** Image. It is a filename. */
        private String image;
        /** Full name. */
        private String name;
        /** Brand. */
        private String brand;
        /** Category ID. */
        private String categoryId;
        /** Quantity. */
        private int quantity;
        /** Description. */
        private String description;
        /** Retail Price. */
        private double retailPrice;
        /** Discount. */
        private double discount; //percent exp 30
        /** Supplier ID. */
        private String supplierId;
        

        /** Default constructor. */
        public Builder() {
            id = "";
            image = "";
            name = "";
            brand = "";
            categoryId = "";
            quantity = 0;
            description = "";
            retailPrice = 0.0;
            discount = 0.0;
            supplierId = "";
            
            
        }

        /** Construct from product. */
        public Builder(final Product product) {
            id = product.id;
            image = product.image;
            name = product.name;
            brand = product.brand;
            categoryId = product.categoryId;
            quantity = product.quantity;
            description = product.description;
            retailPrice = product.retailPrice;
            discount = product.discount;
            supplierId = product.supplierId;
        }
        
        /** Set id. */
        @Override
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        /** Set image. */
        public Builder withImage(final String image) {
            this.image = image;
            return this;
        }

        /** Set name. */
        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        /** Set brand. */
        public Builder withBrand(final String brand) {
            this.brand = brand;
            return this;
        }

        /** Set category ID. */
        public Builder withCategoryId(final String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        /** Set quantity. */
        public Builder withQuantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        /** Set description. */
        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        /** Set retail price. */
        public Builder withRetailPrice(final double retailPrice) {
            this.retailPrice = retailPrice;
            return this;
        }

        /** Set discount. */
        public Builder withDiscount(final double discount) {
            this.discount = discount;
            return this;
        }

        /** Set supplier ID. */
        public Builder withSupplierId(final String supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        /** Build product. */
        @Override
        public Product build() {
            return new Product(this);
        }
    }
}
