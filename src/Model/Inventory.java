/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mat37
 */
public class Inventory {
    
    //Inventory variables
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    
    
    //Inventory methods
    public void addPart(Part newPart) {
        this.allParts.add(newPart);
    }
    
    public ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> searchedParts = FXCollections.observableArrayList();
        
        for (Part p : allParts) {
            if (p.getPartName().contains(partName)) {
                searchedParts.add(p);
            }
        }
        
        return searchedParts;
    }
    
    public Part lookUpPart(int id) {
        Part part;
        for (int i = 0; i < allParts.size(); ++i) {
            if (allParts.get(i).getPartId() == id) {
                part = allParts.get(i);
                return part;
            }
        }
        return null;
    }
    
    public void updatePart(Part selectedPart) {
        for (int i=0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartId() == selectedPart.getPartId()) {
                allParts.set(i, selectedPart);
            }
        }
    }
    
    public void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }
    
    public ObservableList<Part> getAllParts(){
        return this.allParts;
    }
    
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    public ObservableList<Product> lookUpProduct(String productName) {
        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();
        
        for (Product p : allProducts) {
            if (p.getProductName().contains(productName)) {
                searchedProduct.add(p);
            }
        }
        return searchedProduct;
    }
    
    public Product lookUpProduct(int id) {
        Product product;
        for (int i = 0; i < allProducts.size(); ++i) {
            if (allProducts.get(i).getProductId() == id) {
                product = allProducts.get(i);
                return product;
            }
        }
        return null;
    }
    
    public void updateProduct(Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductId() == selectedProduct.getProductId()) {
                allProducts.set(i, selectedProduct);
            }
        }
    }
    
    public void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }
    
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
