package pcms;

public class ProductDiscount {
    /** Product Id. */
    private String productId;
    /** Discount. */
    private Double discount;
    
    /** Construct. */
    public ProductDiscount(String productId, Double discount){
        this.productId = productId;
        this.discount = discount;
    }
    
    /** Ser product ID. */
    public void setProductId(String productId){
        this.productId = productId;
    }
    
    /** Set discount. */
    public void setDiscount(Double discount){
        this.discount = discount;
    }
    
    /** Get product ID. */
    public String getProductId(){
        return productId;
    }
    
    /** Get discount. */
    public Double getDiscount(){
        return discount;
    }
    
}
