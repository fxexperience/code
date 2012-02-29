package com.fxexperience.tools;

import com.fxexperience.tools.caspianstyler.CaspianStylerMainFrame;
import com.fxexperience.tools.derivationcalc.DerivationCalcContent;
import com.fxexperience.tools.splineeditor.SplineEditor;
import java.io.IOException;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Jasper Potts
 */
public class ToolsApp extends Application {
    private static final Interpolator interpolator = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
    private static double TOOLBAR_WIDTH = 80;
    private Pane root;
    private Parent caspianStyler;
    private StackPane currentPane, sparePane;
    private VBox toolBar;
    private int currentToolIndex = 0;
    private Timeline timeline;
    private Tool nextTool = null;
    private int nextToolIndex;
    private DoubleProperty arrowH = new SimpleDoubleProperty(200);
    
    private Tool[] tools;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override public void start(Stage primaryStage) throws IOException {
        tools = new Tool[]{
            new Tool(
                "Caspian Styler", 
                (Parent) FXMLLoader.load(CaspianStylerMainFrame.class.getResource("CaspianStylerMainFrame.fxml")),
                new Image(ToolsApp.class.getResourceAsStream("images/caspianstyler-icon.png"))
            ),
            new Tool(
                "Animation Spline Editor", 
                new SplineEditor(),
                new Image(ToolsApp.class.getResourceAsStream("images/spline-editor-icon.png"))
            ),
            new Tool(
                "Derived Color Calculator", 
                (Parent) FXMLLoader.load(DerivationCalcContent.class.getResource("DerivationCalcContent.fxml")),
                new Image(ToolsApp.class.getResourceAsStream("images/derive-color-icon.png"))
            )
        };
        
        //create root node
        root = new Pane() {
            @Override protected void layoutChildren() {
                double w = getWidth();
                double h = getHeight();
                toolBar.resizeRelocate(0,0,TOOLBAR_WIDTH,h);
                currentPane.relocate(TOOLBAR_WIDTH-10,0);
                currentPane.resize(w-TOOLBAR_WIDTH+10,h);
                sparePane.relocate(TOOLBAR_WIDTH-10,0);
                sparePane.resize(w-TOOLBAR_WIDTH+10,h);
                Node currentToolButton = toolBar.getChildren().get(currentToolIndex);
                arrowH.set(currentToolButton.getBoundsInParent().getMinY()+(currentToolButton.getLayoutBounds().getHeight()/2));
            }
        };
        
        // create toolbar background path
        Path toolBarBackground = createToolBarPath(null,Color.web("#606060"));
        
        // create toolbar
        toolBar = new VBox(0);
        toolBar.setId("ToolsToolbar");
        toolBar.setClip(createToolBarPath(Color.BLACK, null));
        ToggleGroup toggleGroup = new ToggleGroup();
        for (int i=0; i < tools.length; i++) {
            final int index = i;
            final Tool tool = tools[i];
            final ToggleButton button = new ToggleButton(tool.getName().replace(' ', '\n'));
            ImageView icon = new ImageView(tool.getIcon());
            icon.effectProperty().bind(new ObjectBinding<Effect>() {
                { bind(button.selectedProperty()); }
                @Override protected Effect computeValue() {
                    return button.isSelected() ? null : new ColorAdjust(0, -1, 0, 0);
                }
            });
            button.setGraphic(icon);
            button.setContentDisplay(ContentDisplay.TOP);
            if (i==0) button.setSelected(true);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setAlignment(Pos.CENTER);
            button.setTextAlignment(TextAlignment.CENTER);
            button.setToggleGroup(toggleGroup);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent t) {
                    switchTool(tool, index);
                }
            });
            toolBar.getChildren().add(button);
        }
        
        currentPane = new StackPane();
        currentPane.getChildren().setAll(tools[0].getContent());
        sparePane = new StackPane();
        sparePane.setVisible(false);
        
        
        root.getChildren().addAll(currentPane,sparePane,toolBarBackground, toolBar);
        
        
        primaryStage.setTitle("FX Experience Tools");
        primaryStage.setScene(
            SceneBuilder.create()
                .root(root)
                .stylesheets(ToolsApp.class.getResource("Tools.css").toString())
                .build()
        );
        primaryStage.show();
    }
    
    private Path createToolBarPath(Paint fill, Paint stroke) {
        Path toolBarBackground = new Path();
        toolBarBackground.setFill(fill);
        toolBarBackground.setStroke(stroke);
        toolBarBackground.setStrokeType(StrokeType.OUTSIDE);
        LineTo arrowTop = new LineTo(TOOLBAR_WIDTH,0);
        arrowTop.yProperty().bind(arrowH.add(-8));
        LineTo arrowTip = new LineTo(TOOLBAR_WIDTH-10,0);
        arrowTip.yProperty().bind(arrowH);
        LineTo arrowBottom = new LineTo(TOOLBAR_WIDTH,0);
        arrowBottom.yProperty().bind(arrowH.add(8));
        LineTo bottomRight = new LineTo(TOOLBAR_WIDTH,0);
        bottomRight.yProperty().bind(root.heightProperty());
        LineTo bottomLeft = new LineTo(0,0);
        bottomLeft.yProperty().bind(root.heightProperty());
        toolBarBackground.getElements().addAll(
                new MoveTo(0,0),
                new LineTo(TOOLBAR_WIDTH,0),
                arrowTop, arrowTip, arrowBottom,
                bottomRight, bottomLeft,
                new ClosePath()
        );
        return toolBarBackground;
    }
    
    
    public void switchTool(Tool newTool, final int toolIndex) {
        // check if existing animation running
        if (timeline != null) {
            nextTool = newTool;
            nextToolIndex = toolIndex;
            timeline.setRate(4);
            return;
        } else {
            nextTool = null;
            nextToolIndex = -1;
        }
        // stop any animations
        if (tools[currentToolIndex].getContent() instanceof AnimatedPanel) {
            ((AnimatedPanel)tools[currentToolIndex].getContent()).stopAnimations();
        }
        // load new content
        sparePane.getChildren().setAll(newTool.getContent());
        sparePane.setCache(true);
        currentPane.setCache(true);
        // wait one pulse then animate
        Platform.runLater(new Runnable() {
            @Override public void run() {
                // animate switch
                if (toolIndex > currentToolIndex) { // animate from bottom
                    currentToolIndex = toolIndex;
                    sparePane.setTranslateY(root.getHeight());
                    sparePane.setVisible(true);
                    timeline = TimelineBuilder.create()
                            .keyFrames(
                                new KeyFrame(Duration.millis(0), 
                                    new KeyValue(currentPane.translateYProperty(),0,interpolator),
                                    new KeyValue(sparePane.translateYProperty(),root.getHeight(),interpolator)
                                ),
                                new KeyFrame(Duration.millis(800), 
                                    animationEndEventHandler,
                                    new KeyValue(currentPane.translateYProperty(),-root.getHeight(),interpolator),
                                    new KeyValue(sparePane.translateYProperty(),0,interpolator)
                                )
                            )
                            .build();
                    timeline.play();
                } else { // from top
                    currentToolIndex = toolIndex;
                    sparePane.setTranslateY(-root.getHeight());
                    sparePane.setVisible(true);
                    timeline = TimelineBuilder.create()
                            .keyFrames(
                                new KeyFrame(Duration.millis(0), 
                                    new KeyValue(currentPane.translateYProperty(),0,interpolator),
                                    new KeyValue(sparePane.translateYProperty(),-root.getHeight(),interpolator)
                                ),
                                new KeyFrame(Duration.millis(800), 
                                    animationEndEventHandler,
                                    new KeyValue(currentPane.translateYProperty(),root.getHeight(),interpolator),
                                    new KeyValue(sparePane.translateYProperty(),0,interpolator)
                                )
                            )
                            .build();
                    timeline.play();
                }
            }
        });
    }
    
    private EventHandler<ActionEvent> animationEndEventHandler = new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent t) {
            // switch panes
            StackPane temp = currentPane;
            currentPane = sparePane;
            sparePane = temp;
            // cleanup
            timeline = null;
            currentPane.setTranslateY(0);
            sparePane.setCache(false);
            currentPane.setCache(false);
            sparePane.setVisible(false);
            sparePane.getChildren().clear();
            // start any animations
            if (tools[currentToolIndex].getContent() instanceof AnimatedPanel) {
                ((AnimatedPanel)tools[currentToolIndex].getContent()).startAnimations();
            }
            // check if we have a animation waiting
            if (nextTool != null) {
                switchTool(nextTool, nextToolIndex);
            }
        }
    };
}