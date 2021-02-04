/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mat37
 */
public class Product {
   
    //Product variables
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productId;
    private String productName;
    private double productPrice;
    private int productStock;
    private int productMin;
    private int productMax;
    
    //Product methods
    public Product(int productId, String productName, double productPrice, int productStock, int productMin, int productMax) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productMin = productMin;
        this.productMax = productMax;
    }
    
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    public void deleteAssociatedPart(Part associatedPart) {
        associatedParts.remove(associatedPart);
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getProductMin() {
        return productMin;
    }

    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    public int getProductMax() {
        return productMax;
    }

    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }
    
}
