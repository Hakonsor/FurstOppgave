/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

/**
 *
 * @author hakon
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;


public class Popup {
   
    
public static int antall_linjer = 50;
public static void display()
{
Stage popupwindow=new Stage();
      
popupwindow.initModality(Modality.APPLICATION_MODAL);
popupwindow.setTitle("Velg antall settninger");
      
      
Label label1= new Label("Vennligs velg antall settninger");
      
TextField tekstfield = new TextField("50");
tekstfield.setMaxWidth(100);
Button button1= new Button("OK");
     
button1.setOnAction(e -> {
    String regex = "[0-9]+";
        if (tekstfield.getText().matches(regex)) {
            try{
                antall_linjer = Integer.parseInt(tekstfield.getText());
                label1.setText("Vennligs velg antall settninger");
                popupwindow.close();
            }catch (NumberFormatException feil) {
                label1.setText("For stort tall");
            }            
        } else {
            label1.setText("Kun heltall");
        }
});
    
VBox layout= new VBox(10);
layout.getChildren().addAll(label1,tekstfield , button1);
layout.setAlignment(Pos.CENTER);
Scene scene1= new Scene(layout, 250, 100);
popupwindow.setScene(scene1);
popupwindow.showAndWait();
       
}

}


