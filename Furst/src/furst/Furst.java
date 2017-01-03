/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author hakon
 */
public class Furst extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("FXML.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Fürst hjemmeoppgave");
			primaryStage.show();
                        
                        primaryStage.setOnCloseRequest(e ->{
                            Platform.exit();
                            System.exit(0);
                        });
                        
		} catch(Exception e) {
			e.printStackTrace();
                       
		}
        /*
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
      
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show(); 
        */
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
