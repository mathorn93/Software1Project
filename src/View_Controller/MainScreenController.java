/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Product;
import Model.Part;
import Model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.Inventory;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author mat37
 */
public class MainScreenController implements Initializable {
    
    //Main Screen text fields
    @FXML private TextField partsSearchTextField;
    @FXML private TextField productsSearchTextField;
    
    //Main Screen tables
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, String> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, String> partInventoryColumn;
    @FXML private TableColumn<Part, String> partCostColumn;
    
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, String> productInventoryColumn;
    @FXML private TableColumn<Product, String> productCostColumn;
    
    @FXML private Button exitButton;
    
    Inventory inventory = new Inventory();
    

    public MainScreenController(Inventory inv) {
        this.inventory = inv;
    }
    
    
    
    public void addPartButtonPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddPartScreen.fxml"));
        View_Controller.AddPartScreenController addPartController = new View_Controller.AddPartScreenController(inventory);
        loader.setController(addPartController);
        Parent addPartParent = loader.load();
        Scene addPartScene = new Scene(addPartParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addPartScene);
        window.show();
    }
    
    public void modifyPartButtonPushed(ActionEvent event) throws IOException {
        Part partSelected = partTable.getSelectionModel().getSelectedItem();
        
        if (partSelected != null) {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyPartScreen.fxml"));
            View_Controller.ModifyPartScreenController modifyPartController = new View_Controller.ModifyPartScreenController(inventory, partSelected);
            loader.setController(modifyPartController);
            Parent modifyPartParent = loader.load();
            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modifyPartScene);
            window.show();
        }
    }
    
    public void deletePartButtonPushed(ActionEvent event) throws IOException {
        Part partSelected = partTable.getSelectionModel().getSelectedItem();
        if (partSelected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete this part?");
            
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
               for (Product product : inventory.getAllProducts()) {
                    if (!product.getAllAssociatedParts().contains(partSelected)) {
                        inventory.deletePart(partSelected);
                    }
                    else {
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setTitle("Information Dialog");
                        errorAlert.setHeaderText("Error");
                        errorAlert.setContentText("This is part in use by a product");

                        errorAlert.showAndWait();
                    }
                } 
            }
        }
        
    }
    
    public void addProductButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddProduct.fxml"));
        View_Controller.AddProductController addProductController = new View_Controller.AddProductController(inventory);
        loader.setController(addProductController);
        Parent addProductParent = loader.load();
        Scene addProductScene = new Scene(addProductParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addProductScene);
        window.show();
    }
    
    public void modifyProductButtonPushed(ActionEvent event) throws IOException {
        Product productSelected = productTable.getSelectionModel().getSelectedItem();
        
        if (productSelected != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyProductScreen.fxml"));
            View_Controller.ModifyProductScreenController modifyProductController = new View_Controller.ModifyProductScreenController(inventory, productSelected);
            loader.setController(modifyProductController);
            Parent modifyProductParent = loader.load();
            Scene modifyProductScene = new Scene(modifyProductParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modifyProductScene);
            window.show();
        }
    }
    
    public void deleteProductButtonPushed(ActionEvent event) throws IOException {
        Product productSelected = productTable.getSelectionModel().getSelectedItem();
        if (productSelected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete this product?");
            
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                inventory.deleteProduct(productSelected);
                productTable.refresh();
            }
        }
        
    }
    
    public void partsSearchButtonPushed(ActionEvent event) throws IOException {
        String partSearch = partsSearchTextField.getText().trim();
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        Boolean found = false;
        searchResults = inventory.lookUpPart(partSearch);
        if (searchResults != null && !searchResults.isEmpty()) {
            found = true;
            partTable.setItems(searchResults);
            partTable.refresh();
        }
        
        if (partSearch == null || partSearch.isEmpty()) {
            partTable.setItems(this.inventory.getAllParts());
        }
        
        if (found == false) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Part not found");

            alert.showAndWait();
        }
    }
    
    public void productsSearchButtonPushed(ActionEvent event) throws IOException {
        String productSearch = productsSearchTextField.getText().trim();
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        Boolean found = false;
        searchResults = inventory.lookUpProduct(productSearch);
        if (searchResults != null && !searchResults.isEmpty()) {
            found = true;
            productTable.setItems(searchResults);
            productTable.refresh();
        }
        
        if (productSearch == null || productSearch.isEmpty()) {
            productTable.setItems(this.inventory.getAllProducts());
        }
        
        if (found == false) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Product not found");

            alert.showAndWait();
        }
    }
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        partTable.setItems(this.inventory.getAllParts());
        
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        
        productTable.setItems(this.inventory.getAllProducts());
        
        
        
    }    
    
}
