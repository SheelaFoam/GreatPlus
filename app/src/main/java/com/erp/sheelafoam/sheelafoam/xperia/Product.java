package com.erp.sheelafoam.sheelafoam.xperia;

/**
 * Created by sudhirharit on 21/11/17.
 */

public class Product {


    private String productName;
    private String subProduct;

    public Product(String productName, String subProduct) {
        this.productName = productName;
        this.subProduct = subProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(String subProduct) {
        this.subProduct = subProduct;
    }
}
