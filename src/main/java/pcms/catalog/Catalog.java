package pcms.catalog;

import static pcms.CsvParsingUtil.decode;
import static pcms.CsvParsingUtil.encode;
import static pcms.CsvParsingUtil.splitIntoCols;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pcms.Model;
import pcms.product.Product;
import pcms.product.ProductRepository;
import pcms.user.User;
import pcms.user.UserRepository;

/** Models catalog. */
public final class Catalog implements Model {
    /** Product repository which help to get product. */
    private static Optional<ProductRepository> productRepository = Optional.empty();
    
    /** Product repository which help to get product. */
    private static Optional<UserRepository> userRepository = Optional.empty();
    
    /** ID. */
    private final String id;
    /** Title. */
    private final String title;
    /** Banner File Path. */
    private final String banner;
    /** Description. */
    private final String description;
    /** Season Start Date. */
    private final LocalDate seasonStartDate;
    /** Season End Date. */
    private final LocalDate seasonEndDate;
    /** Product ID - Discount Pairs. */
    private final List<ProductDiscount> productDiscounts;
    /** Created Date and Time. */
    private final Instant timestamp;
    /** User ID. */
    private final String userId;

    /** Construct from Catalog.Builder. */
    private Catalog(final Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.banner = builder.banner;
        this.description = builder.description;
        this.seasonStartDate = builder.seasonStartDate;
        this.seasonEndDate = builder.seasonEndDate;
        this.productDiscounts = builder.productDiscounts;
        this.timestamp = builder.timestamp;
        this.userId = builder.userId;
    }

    /** Construct from row. */
    public Catalog(final String row) {
        final List<String> fields = splitIntoCols(row);
        if (fields.size() != 9) {
            throw new IllegalArgumentException("Fields count incorrect.");
        }

        id = fields.get(0);
        title = decode(fields.get(1));
        banner = decode(fields.get(2));
        description = decode(fields.get(3));
        seasonStartDate = LocalDate.parse(fields.get(4), DateTimeFormatter.ISO_LOCAL_DATE);
        seasonEndDate = LocalDate.parse(fields.get(5), DateTimeFormatter.ISO_LOCAL_DATE);
        
        productDiscounts = new ArrayList<>();
        
        final String column = decode(fields.get(6));
        if (!column.isEmpty()) {
            final String[] array = column.split(",");
            for (final String pair: array) {
                final String[] productPair = pair.split(":");
                productDiscounts.add(new ProductDiscount(
                        productPair[0], Double.parseDouble(productPair[1])));
            }
        }
        
        timestamp = Instant.parse(fields.get(7));
        userId = fields.get(8);
    }

    /** Convert to row. */
    @Override
    public String toRow() {
        
        final ArrayList<String> array = new ArrayList<>(productDiscounts.size());
        for (final ProductDiscount p: productDiscounts) {
            array.add(p.getProductId() + ":" + p.getDiscount());
        }
        
        return String.join(
                ",",
                id,
                encode(title),
                encode(banner),
                encode(description),
                seasonStartDate.toString(),
                seasonEndDate.toString(),
                encode(String.join(",", array)),
                timestamp.toString(),
                userId);
               
    }
    
    /** Get id. */
    @Override
    public String getId() {
        return id;
    }
    
    /** Get title. */
    public String getTitle() {
        return title;
    }
    
    /** Get banner. */
    public String getBanner() {
        return banner;
    }
    
    /** Get description. */
    public String getDescription() {
        return description;
    }
    
    /** Get season start date. */
    public LocalDate getSeasonStartDate() {
        return seasonStartDate;
    }
    
    /** Get season end date. */
    public LocalDate getSeasonEndDate() {
        return seasonEndDate;
    }
    
    /** Get list of product discount pairs. */
    public List<ProductDiscount> getProductDiscounts() {
        return productDiscounts;
    }
    
    /** Get timestamp. */
    public Instant getTimestamp() {
        return timestamp;
    }
    
    /** Get user id. */
    public String getUserId() {
        return userId;
    }
    
    /** Get user. */
    public User getUser() {
        return userRepository.get().findWithId(userId).get();
    }
    
    /** Set user repository. */
    public static void setUserRepository(final UserRepository repository) {
        userRepository = Optional.of(repository);
    }
    
    /** Get product. */
    public Product getProduct() {
        return productRepository.get().findWithId(userId).get();
    }
    
    /** Set user repository. */
    public static void setProductRepository(final ProductRepository repository) {
        productRepository = Optional.of(repository);
    }

    /** Builder. */
    public static class Builder implements Model.Builder {
        /** ID. */
        private String id;
        /** Title. */
        private String title;
        /** Banner File Path. */
        private String banner;
        /** Description. */
        private String description;
        /** Season Start Date. */
        private LocalDate seasonStartDate;
        /** Season End Date. */
        private LocalDate seasonEndDate;
        /** Product ID - Discount Pairs. */
        private List<ProductDiscount> productDiscounts;
        /** Created Date and Time. */
        private Instant timestamp;
        /** User ID. */
        private String userId;
        

        /** Default constructor. */
        public Builder() {
            id = "";
            title = "";
            banner = "";
            description = "";
            seasonStartDate = LocalDate.ofEpochDay(0);
            seasonEndDate = LocalDate.ofEpochDay(0);
            productDiscounts = new ArrayList<>();
            timestamp = Instant.ofEpochSecond(0);
            userId = "";         
        }

        /** Construct from catalog. */
        public Builder(final Catalog catalog) {
            id = catalog.id;
            title = catalog.title;
            banner = catalog.banner;
            description = catalog.description;
            seasonStartDate = catalog.seasonStartDate;
            seasonEndDate = catalog.seasonEndDate;
            productDiscounts = catalog.productDiscounts;
            timestamp = catalog.timestamp;
            userId = catalog.userId;
        }
        
        /** Set id. */
        @Override
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        /** Set title. */
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        /** Set banner. */
        public Builder withBanner(final String banner) {
            this.banner = banner;
            return this;
        }

        /** Set description. */
        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        /** Set season start date. */
        public Builder withSeasonStartDate(final LocalDate seasonStartDate) {
            this.seasonStartDate = seasonStartDate;
            return this;
        }

        /** Set season end date. */
        public Builder withSeasonEndDate(final LocalDate seasonEndDate) {
            this.seasonEndDate = seasonEndDate;
            return this;
        }

        /** Set list of product discount pairs. */
        public Builder withProductDiscounts(final List<ProductDiscount> productDiscounts) {
            this.productDiscounts = productDiscounts;
            return this;
        }

        /** Set timestamp. */
        public Builder withTimestamp(final Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /** Set userId. */
        public Builder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        /** Build catalog. */
        @Override
        public Catalog build() {
            return new Catalog(this);
        }
    }
}
