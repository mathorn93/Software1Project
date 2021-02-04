package Main;

import Model.InHouse;
import Model.Outsourced;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Model.Inventory;
import Model.Part;
import Model.Product;
import View_Controller.MainScreenController;

/**
 *
 * @author mat37
 */


public class Software1Project extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inventory = new Inventory();
        addTestData(inventory);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        View_Controller.MainScreenController main = new View_Controller.MainScreenController(inventory);
        loader.setController(main);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    //Adds data for testing purposes
    void addTestData(Inventory inv) {
        Part p1 = new InHouse(1, "doo dad", 6.5, 7, 5, 10, 3000);
        Part p2 = new InHouse(2, "thingy", 8.3, 5, 4, 6, 45);
        Part p3 = new Outsourced(3, "widget", 7.1, 12, 8, 21, "Widget House");
        Part p4 = new Outsourced(4, "whatchamacallit", 9.9, 9, 3, 99, "That One Place");
        
        inv.addPart(p1);
        inv.addPart(p2);
        inv.addPart(p3);
        inv.addPart(p4);
        
        Product r1 = new Product(1, "this product", 20.56, 60, 5, 100);
        r1.addAssociatedPart(p1);
        Product r2 = new Product(2, "that product", 35.40, 84, 12, 89);
        r2.addAssociatedPart(p2);
        Product r3 = new Product(3, "the other one", 29.99, 99, 69, 109);
        r3.addAssociatedPart(p3);
        r3.addAssociatedPart(p4);
        
        inv.addProduct(r1);
        inv.addProduct(r2);
        inv.addProduct(r3);
        
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
