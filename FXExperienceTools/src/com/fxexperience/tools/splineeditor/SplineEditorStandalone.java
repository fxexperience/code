package com.fxexperience.tools.splineeditor;

import com.fxexperience.tools.Tool;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Jasper Potts
 */
public class SplineEditorStandalone extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override public void start(Stage primaryStage) throws IOException {
        SplineEditor splineEditor = new SplineEditor();
        Scene scene = new Scene(splineEditor);
        scene.getStylesheets().add(Tool.class.getResource("Tools.css").toExternalForm());
      
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
   
    }
}
