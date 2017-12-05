package buttonstyles;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ToolBarBuilder;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Jasper
 */
public class ButtonStyles extends Application {

    public static void main(String[] args) { launch(args); }
    
    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX CSS Buttons");
        primaryStage.setScene(SceneBuilder.create()
            .stylesheets(ButtonStyles.class.getResource("Buttons.css").toExternalForm())
            .fill(Color.gray(0.9))
            .root(
                BorderPaneBuilder.create()
                    .left(
                        VBoxBuilder.create()
                            .spacing(10)
                            .padding(new Insets(20))
                            .alignment(Pos.CENTER)
                            .children(
                                ButtonBuilder.create().text("Green").id("green").build(),
                                ButtonBuilder.create().text("Round Red").id("round-red").build(),
                                ButtonBuilder.create().text("Bevel Grey").id("bevel-grey").build(),
                                ButtonBuilder.create().text("Glass Grey").id("glass-grey").build(),
                                ButtonBuilder.create().text("Shiny Orange").id("shiny-orange").build(),
                                ButtonBuilder.create().text("DARK BLUE").id("dark-blue").build(),
                                ButtonBuilder.create().text("Record Sales").id("record-sales").build(),
                                ButtonBuilder.create().text("Rich Blue").id("rich-blue").build(),
                                ButtonBuilder.create().text("Big Yellow").id("big-yellow").build(),
                                ToolBarBuilder.create()
                                    .id("iphone-toolbar")
                                    .items(
                                        ButtonBuilder.create().text("iPhone").id("iphone").build()
                                    )
                                    .build(),
                                ButtonBuilder.create().text("Large iPad Dark Grey").id("ipad-dark-grey").build(),
                                ButtonBuilder.create().text("Large iPad Grey").id("ipad-grey").build(),
                                ButtonBuilder.create().text("OSX Lion (Default)").id("lion-default").build(),
                                ButtonBuilder.create().text("OSX Lion").id("lion").build(),
                                ButtonBuilder.create().text("Windows 7 (Default)").id("windows7-default").build(),
                                ButtonBuilder.create().text("Windows 7").id("windows7").build()
                            )
                            .build()
                    )
                    .center(
                        VBoxBuilder.create()
                            .spacing(10)
                            .padding(new Insets(20))
                            .alignment(Pos.CENTER)
                            .style("-fx-background-color: #373737;")
                            .children(
                                ButtonBuilder.create().text("Green").id("green").build(),
                                ButtonBuilder.create().text("Round Red").id("round-red").build(),
                                ButtonBuilder.create().text("Bevel Grey").id("bevel-grey").build(),
                                ButtonBuilder.create().text("Glass Grey").id("glass-grey").build(),
                                ButtonBuilder.create().text("Shiny Orange").id("shiny-orange").build(),
                                ButtonBuilder.create().text("DARK BLUE").id("dark-blue").build(),
                                ButtonBuilder.create().text("Record Sales").id("record-sales").build(),
                                ButtonBuilder.create().text("Rich Blue").id("rich-blue").build(),
                                ButtonBuilder.create().text("Big Yellow").id("big-yellow").build(),
                                ToolBarBuilder.create()
                                    .id("iphone-toolbar")
                                    .items(
                                        ButtonBuilder.create().text("iPhone").id("iphone").build()
                                    )
                                    .build(),
                                ButtonBuilder.create().text("Large iPad Dark Grey").id("ipad-dark-grey").build(),
                                ButtonBuilder.create().text("Large iPad Grey").id("ipad-grey").build(),
                                ButtonBuilder.create().text("OSX Lion (Default)").id("lion-default").build(),
                                ButtonBuilder.create().text("OSX Lion").id("lion").build(),
                                ButtonBuilder.create().text("Windows 7 (Default)").id("windows7-default").build(),
                                ButtonBuilder.create().text("Windows 7").id("windows7").build()
                            )
                            .build()
                    )
                .build()
            )
            .build());
        primaryStage.show();
    }
}
