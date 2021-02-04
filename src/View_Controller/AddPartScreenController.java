/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import Model.Outsourced;
import Model.Inventory;
import Model.Part;
import Model.InHouse;
import Model.Product;
import java.util.Set;
import javafx.scene.control.Label;
import View_Controller.MainScreenController;
import java.util.Optional;
import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;


/**
 * FXML Controller class
 *
 * @author mat37
 */
public class AddPartScreenController implements Initializable {

    //Add part labels
    @FXML private Label companyNameLabel;
    
    //Add part text fields
    @FXML private TextField partIdTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partInventoryTextField;
    @FXML private TextField partPriceTextField;
    @FXML private TextField partMaxTextField;
    @FXML private TextField partMinTextField;
    @FXML private TextField partCompNameTextField;
    
    //Add part radio buttons
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    private ToggleGroup partToggleGroup;
    
    Inventory inventory = new Inventory();

    public AddPartScreenController(Inventory inv) {
        this.inventory = inv;
    }
    
    private void generatePartId() {
        boolean found;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);
        
        if (inventory.getAllParts().size() == 0) {
            partIdTextField.setText(num.toString());
        }
        if (inventory.getAllParts().size() == 1000) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Maximum number of parts reached");

            alert.showAndWait();
        } else {
            found = checkNum(num);
            
            if (found == false) {
                partIdTextField.setText(num.toString());
            } else {
                generatePartId();
            }
        }
    }
    
    private boolean checkNum(Integer num) {
        Part found = inventory.lookUpPart(num);
        return found != null;
    }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
    
    public void inHouseRadioButtonPushed(ActionEvent event) throws IOException {
        this.companyNameLabel.setText("Machine ID");
        this.partCompNameTextField.promptTextProperty().set("Machine ID");
    }
    
    public void outsourcedRadioButtonPushed(ActionEvent event) throws IOException {
        this.companyNameLabel.setText("Company Name");
        this.partCompNameTextField.promptTextProperty().set("Company Name");
        
    }
    
    public void saveButtonPushed(ActionEvent event) throws IOException {
        Boolean error = false;
        Boolean minMaxError  = false;
        Boolean invError = false;
        Boolean negError = false;
        
        int id = Integer.parseInt(this.partIdTextField.getText());
        String name = this.partNameTextField.getText();
        double price = Double.parseDouble(this.partPriceTextField.getText());
        int inv = Integer.parseInt(this.partInventoryTextField.getText());
        int min = Integer.parseInt(this.partMinTextField.getText());
        int max = Integer.parseInt(this.partMaxTextField.getText());
        
        if ((min > max) || (max < min)) {
            error = true;
            minMaxError = true;
        }
        
        if (!((inv >= min) && (inv <= max))) {
            error = true;
            invError = true;
        }
        
        if (price < 0) {
            error = true;
            negError = true;
        }
        
        if (minMaxError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Min cannot be greater than max and max cannot be less than min");

            alert.showAndWait();
        }
        
        if (invError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Inventory must be between min and max");

            alert.showAndWait();
        }
        
        if (negError == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Price cannot be negative");

            alert.showAndWait();
        }
        
        if (error == false) {
            if (this.partToggleGroup.getSelectedToggle().equals(this.outsourcedRadioButton)){
                String companyName = this.partCompNameTextField.getText();
                Part newPart = new Outsourced(id, name, price, inv, min, max, companyName);
                inventory.addPart(newPart);
            
            }
        
            if (this.partToggleGroup.getSelectedToggle().equals(this.inHouseRadioButton)){
                int machineId = Integer.parseInt(this.partCompNameTextField.getText()); 
                Part newPart = new InHouse(id, name, price, inv, min, max, machineId);
                inventory.addPart(newPart);
            }
        
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartId();
        
        partToggleGroup = new ToggleGroup();
        this.outsourcedRadioButton.setToggleGroup(partToggleGroup);
        this.inHouseRadioButton.setToggleGroup(partToggleGroup);
        this.outsourcedRadioButton.setSelected(true);
    }    
    
}
