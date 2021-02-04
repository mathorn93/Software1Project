/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mat37
 */
public class ModifyPartScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    //Modify part labels
    @FXML private Label companyNameLabel;
    
    //Modify part text fields
    @FXML private TextField idTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceCostTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField compNameTextField;
    
    //Modify part radio buttons
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outSourcedRadioButton;
    
    private ToggleGroup partToggleGroup;
    
    Inventory inventory = new Inventory();
    Part part;

    public ModifyPartScreenController(Inventory inv, Part part) {
        this.inventory = inv;
        this.part = part;
    }
    
    public void saveButtonPushed(ActionEvent event) throws IOException {
        Boolean error = false;
        Boolean minMaxError  = false;
        Boolean invError = false;
        Boolean negError = false;
        
        int id = Integer.parseInt(this.idTextField.getText());
        String name = this.partNameTextField.getText();
        double price = Double.parseDouble(this.priceCostTextField.getText());
        int inv = Integer.parseInt(this.invTextField.getText());
        int min = Integer.parseInt(this.minTextField.getText());
        int max = Integer.parseInt(this.maxTextField.getText());
        
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
        
        if (this.partToggleGroup.getSelectedToggle().equals(this.inHouseRadioButton)){
                try {
                    int machineId = Integer.parseInt(this.compNameTextField.getText()); 
                }
                catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Error");
                    alert.setContentText("MachineID must be a number");

                    alert.showAndWait();
                    
                    error = true;
                }
                
            }
        
        if (error == false) {
            if (this.partToggleGroup.getSelectedToggle().equals(this.outSourcedRadioButton)){
                String companyName = this.compNameTextField.getText();
                Part newPart = new Outsourced(id, name, price, inv, min, max, companyName);
                inventory.updatePart(newPart);
            
            }
        
            if (this.partToggleGroup.getSelectedToggle().equals(this.inHouseRadioButton)){
                    int machineId = Integer.parseInt(this.compNameTextField.getText()); 
                    Part newPart = new InHouse(id, name, price, inv, min, max, machineId);
                    inventory.updatePart(newPart);
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
        this.compNameTextField.promptTextProperty().set("Machine ID");
    }
    
    public void outsourcedRadioButtonPushed(ActionEvent event) throws IOException {
        this.companyNameLabel.setText("Company Name");
        this.compNameTextField.promptTextProperty().set("Company Name");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partToggleGroup = new ToggleGroup();
        this.outSourcedRadioButton.setToggleGroup(partToggleGroup);
        this.inHouseRadioButton.setToggleGroup(partToggleGroup);
        
        this.idTextField.setText(Integer.toString(part.getPartId()));
        this.partNameTextField.setText(part.getPartName());
        this.invTextField.setText(Integer.toString(part.getPartStock()));
        this.priceCostTextField.setText(Double.toString(part.getPartPrice()));
        this.maxTextField.setText(Integer.toString(part.getPartMax()));
        this.minTextField.setText(Integer.toString(part.getPartMin()));
        if (part instanceof InHouse) {
            InHouse part = (InHouse) this.part;
            this.inHouseRadioButton.setSelected(true);
            this.companyNameLabel.setText("Machine ID");
            this.compNameTextField.setText(Integer.toString(part.getMachineId()));
        }
        if (part instanceof Outsourced) {
            Outsourced part = (Outsourced) this.part;
            this.outSourcedRadioButton.setSelected(true);
            this.companyNameLabel.setText("Company Name");
            this.compNameTextField.setText(part.getCompanyName());
        }
    }    
    
}
