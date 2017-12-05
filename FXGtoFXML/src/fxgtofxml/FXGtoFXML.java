package fxgtofxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Jasper
 */
public class FXGtoFXML extends Application {

    public static void main(String[] args) { Application.launch(args); }
    
    @Override public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("FXG to FXML Converter");
        BorderPane root = new BorderPane();
        ToolBar toolbar = new ToolBar();
        root.setTop(toolbar);
        Button loadButton = new Button("Convert FXG...");
        Button zoomInButton = new Button("Zoom In");
        Button zoomOutButton = new Button("Zoom Out");
        Button zoomFitButton = new Button("Zoom Fit");
        toolbar.getItems().addAll(loadButton, zoomInButton, zoomOutButton, zoomFitButton);
        
        TabPane tabPane = new TabPane();
        root.setCenter(tabPane);
        Tab sourceTab = new Tab("FXML Source");
        final Label sourceLabel = new Label("fxml");
        ScrollPane sourceScrollPane = new ScrollPane();
        sourceScrollPane.setContent(sourceLabel);
        sourceTab.setContent(sourceScrollPane);
        Tab previewTab = new Tab("FXML Preview");
        final ScrollPane previewScrollPane = new ScrollPane();
        previewTab.setContent(previewScrollPane);
        final Group contentGroup = new Group();
        previewScrollPane.setContent(contentGroup);
        final Scale contentScale = new Scale();
        tabPane.getTabs().addAll(previewTab,sourceTab);
        
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FXG", "*.fxg"));
                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                File fxgFile = fileChooser.showOpenDialog(primaryStage);
                try {
                    // XSLT process FXG into FXML
                    TransformerFactory tFactory = TransformerFactory.newInstance();
                    Transformer transformer = tFactory.newTransformer(
                            new StreamSource(FXGtoFXML.class.getResourceAsStream("FXGtoFXML.xsl")));
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    
                    StringWriter stringOut = new StringWriter();
                    transformer.transform(
                            new StreamSource(new FileInputStream(fxgFile)),
                            new StreamResult(stringOut));
                    File outFile =  new File(fxgFile.getParentFile(), fxgFile.getName().split("\\.")[0]+".fxml");
                    System.out.println("outFile = " + outFile);
                    FileWriter writer = new FileWriter(outFile);
                    writer.write(stringOut.toString());
                    writer.flush();
                    writer.close();
                    
                    sourceLabel.setText(stringOut.toString());
                    Node content = (Node) FXMLLoader.load(outFile.toURI().toURL(), null, new JavaFXBuilderFactory());
                    content.getTransforms().add(contentScale);
                    contentGroup.getChildren().setAll(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                double scale = contentScale.getX() * 1.1;
                contentScale.setX(scale);
                contentScale.setY(scale);
            }
        });
        
        zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                double scale = contentScale.getX() * 0.9;
                contentScale.setX(scale);
                contentScale.setY(scale);
            }
        });
        
        zoomFitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Node content = previewScrollPane.getContent();
                contentScale.setX(1);
                contentScale.setY(1);
                double scaleX = previewScrollPane.getViewportBounds().getWidth() / content.getBoundsInParent().getWidth();
                double scaleY = previewScrollPane.getViewportBounds().getHeight() / content.getBoundsInParent().getHeight();
                double scale = Math.min(scaleX, scaleY);
                contentScale.setX(scale);
                contentScale.setY(scale);
                previewScrollPane.setVvalue(0);
                previewScrollPane.setHvalue(0);
            }
        });
        
        primaryStage.setScene(new Scene(root, 800,600));
        primaryStage.show();
    }
}
