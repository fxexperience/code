package fxexperienceplayer;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Loading playlist/music dialog
 * 
 * @author Jasper Potts
 */
public class LoadDialog {
    public static void loadPlayList(final PlayList playList, Stage owner) {
        System.out.println("existingUrl = " + playList.getUrl());
        
        final TextField urlField = TextFieldBuilder.create()
            .text(playList.getUrl())
            .prefWidth(500)
            .style("-fx-font-size: 0.9em;")
            .build();
        HBox.setHgrow(urlField, Priority.ALWAYS);
        
        final Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(SceneBuilder.create()
                .fill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.REPEAT, new Stop(0,Color.web("#282828")), new Stop(1,Color.web("#202020"))))
                .root(
                    VBoxBuilder.create()
                        .spacing(20)
                        .padding(new Insets(25))
                        .style("-fx-base: #282828; -fx-background: #282828; -fx-font-size: 1.1em;")
                        .children(
                            new Label("Enter URL for a m3u, Phlow xml, or mp3 file."),
                            HBoxBuilder.create()
                                .spacing(20)
                                .children(
                                    urlField,
                                    ButtonBuilder.create()
                                        .text("Browse...")
                                        .onAction(new EventHandler<ActionEvent>() {
                                            @Override public void handle(ActionEvent arg0) {
                                                FileChooser fc = new FileChooser();
                                                File file = fc.showOpenDialog(dialog);
                                                if (file != null) urlField.setText(file.toURI().toString());
                                            }
                                        })
                                        .build()
                                )
                                .build(),
                            HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                    ButtonBuilder.create()
                                        .text("Cancel")
                                        .cancelButton(true)
                                        .onAction(new EventHandler<ActionEvent>() {
                                            @Override public void handle(ActionEvent arg0) {
                                                dialog.hide();
                                            }
                                        })
                                        .build(),
                                    ButtonBuilder.create()
                                        .text("Load...")
                                        .defaultButton(true)
                                        .onAction(new EventHandler<ActionEvent>() {
                                            @Override public void handle(ActionEvent arg0) {
                                                dialog.hide();
                                                playList.load(urlField.getText());
                                            }
                                        })
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build());
        dialog.show();
    }
}
