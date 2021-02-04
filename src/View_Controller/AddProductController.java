/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Product;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author mat37
 */
public class AddProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    //Add product screen text areas
    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField searchTextField;
    
    //List of Parts TableView
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, String> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, String> partInvLvlColumn;
    @FXML private TableColumn<Part, String> partPriceColumn;
    
    //List of associated parts TableView
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, String> associatedPartsIdColumn;
    @FXML private TableColumn<Part, String> associatedPartsNameColumn;
    @FXML private TableColumn<Part, String> associatedPartsInvLvlColumn;
    @FXML private TableColumn<Part, String> associatedPartsPriceColumn;
    
    Inventory inventory = new Inventory();
    Product product = new Product(0, "string", 0.0, 0, 0, 0);

    public AddProductController(Inventory inv) {
        this.inventory = inv;
    }
    
    private void generateProductId() {
        boolean found;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);
        
        if (inventory.getAllProducts().size() == 0) {
            idTextField.setText(num.toString());
        }
        if (inventory.getAllProducts().size() == 1000) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Maximum number of products reached");

            alert.showAndWait();
        } else {
            found = checkNum(num);
            
            if (found == false) {
                idTextField.setText(num.toString());
            } else {
                generateProductId();
            }
        }
    }
    
    private boolean checkNum(Integer num) {
        Product found = inventory.lookUpProduct(num);
        return found != null;
    }
    
    public void searchButtonPushed(ActionEvent event) throws IOException {
        String partSearch = searchTextField.getText().trim();
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
    
    public void addAssociatedPartButton(ActionEvent event) throws IOException {
        Part partSelected = partTable.getSelectionModel().getSelectedItem();
        Boolean doubleError = false;
        for (int i = 0; i < product.getAllAssociatedParts().size(); ++i) {
            if (partSelected.getPartId() == product.getAllAssociatedParts().get(i).getPartId()) {
                doubleError = true;
            }
        }
        if (doubleError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Cannot add two of the same part");

            alert.showAndWait();
        }
        if (doubleError == false) {
            product.addAssociatedPart(partSelected);
            associatedPartsTable.setItems(this.product.getAllAssociatedParts());
        }
    }
    
    public void saveButtonPushed(ActionEvent event) throws IOException {
        int id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        int stock = Integer.parseInt(invTextField.getText());
        int min = Integer.parseInt(minTextField.getText());
        int max = Integer.parseInt(maxTextField.getText());
        
        Boolean error = false;
        Boolean minMaxError  = false;
        Boolean priceError = false;
        Boolean invError = false;
        Boolean associatedPartsError = false;
        Boolean negError = false;
        Boolean emptyError = false;
        double partsPrice = 0.0;
        
        if (max < min) {
            minMaxError = true;
            error = true;
        }
        
        for (int i = 0; i < product.getAllAssociatedParts().size(); i++) {
            partsPrice = partsPrice + product.getAllAssociatedParts().get(i).getPartPrice();
        }
        
        if (partsPrice > price) {
                priceError = true;
                error = true;
            }
        
        if (product.getAllAssociatedParts().isEmpty()) {
            associatedPartsError = true;
            error = true;
        }
        
        if (!((stock <= max) && (stock >= min))) {
            invError = true;
            error = true;
        }
        
        if (price < 0) {
            negError = true;
            error = true;
        }
        
        if ((nameTextField.getText().isEmpty())) {
            error = true;
            emptyError = true;
        }
        if ((invTextField.getText().isEmpty())) {
            error = true;
            emptyError = true;
        }
        if ((priceTextField.getText().isEmpty())) {
            error = true;
            emptyError = true;
        }
        if ((maxTextField.getText().isEmpty())) {
            error = true;
            emptyError = true;
        }
        if ((minTextField.getText().isEmpty())) {
            error = true;
            emptyError = true;
        }
        
        if (emptyError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("All fields must be filled");

            alert.showAndWait();
        }
        
        if (negError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Price cannot be negative");

            alert.showAndWait();
        }
        
        if (associatedPartsError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Product must have at least one associated part");

            alert.showAndWait();
        }
        
        if (invError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Inventory must be between min and max");

            alert.showAndWait();
        }
        
        if (minMaxError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Min cannot be greater than max and max cannot be less than min");

            alert.showAndWait();
        }
        
        if (priceError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Product price cannot be less than the price of the associated parts");

            alert.showAndWait();
        }
        
        if (error != true) {
            product.setProductId(id);
            product.setProductName(name);
            product.setProductPrice(price);
            product.setProductStock(stock);
            product.setProductMin(min);
            product.setProductMax(max);
        
            inventory.addProduct(product);
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
            View_Controller.MainScreenController main = new View_Controller.MainScreenController(inventory);
            loader.setController(main);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        
    }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
            View_Controller.MainScreenController main = new View_Controller.MainScreenController(inventory);
            loader.setController(main);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    
    public void deleteButtonPushed(ActionEvent event) throws IOException {
        Part partSelected = associatedPartsTable.getSelectionModel().getSelectedItem();
        if (partSelected != null) {
           Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                product.deleteAssociatedPart(partSelected);
                associatedPartsTable.refresh();
            } 
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateProductId();
        
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partInvLvlColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        partTable.setItems(inventory.getAllParts());
        
        associatedPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        associatedPartsInvLvlColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        associatedPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        if (!product.getAllAssociatedParts().isEmpty() || product.getAllAssociatedParts() != null) {
            associatedPartsTable.setItems(product.getAllAssociatedParts());
        }
    }    
    
}
