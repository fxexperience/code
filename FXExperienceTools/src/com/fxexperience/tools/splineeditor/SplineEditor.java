package com.fxexperience.tools.splineeditor;

import com.fxexperience.tools.AnimatedPanel;
import java.util.Collections;
import javafx.animation.*;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Jasper Potts
 */
public class SplineEditor extends GridPane implements AnimatedPanel {

    private Circle scaleCircle;
    private Rectangle rotateSquare;
    private Rectangle fadeSquare;
    private Circle linearCircle;
    private Rectangle linearTrack;
    private Group linearGroup;
    private Timeline timeline;
    private SplineEditorControl splineEditorControl;
    private Region background = new Region();

    public SplineEditor() {
        scaleCircle = new Circle();
        scaleCircle.setStroke(Color.WHITE);
        scaleCircle.setFill(Color.web("#9ef2ff"));
        scaleCircle.setRadius(50);
        
        rotateSquare = new Rectangle();
        rotateSquare.setWidth(80);
        rotateSquare.setHeight(80);
        rotateSquare.setStroke(Color.WHITE);
        rotateSquare.setFill(Color.web("#9ef2ff"));
        
        fadeSquare = new Rectangle();
        fadeSquare.setWidth(100);
        fadeSquare.setHeight(100);
        fadeSquare.setStroke(Color.WHITE);
        fadeSquare.setFill(Color.web("#9ef2ff"));
        
        linearCircle = new Circle();
        linearCircle.setStroke(Color.WHITE);
        linearCircle.setFill(Color.web("#9ef2ff"));
        linearCircle.setRadius(10);
        linearCircle.setCenterX(10);
        linearCircle.setCenterY(10);
        
        linearTrack = new Rectangle();
        linearTrack.setWidth(200);
        linearTrack.setHeight(20);
        linearTrack.setStroke(Color.rgb(255, 255, 255, 0.5));
        linearTrack.setFill(null);
        linearTrack.setArcWidth(20);
        linearTrack.setArcHeight(20);
        
        linearGroup = new Group();
        linearGroup.setTranslateX(0.5);
        linearGroup.setTranslateY(0.5);
        linearGroup.getChildren().addAll(linearTrack, linearCircle);
        
        splineEditorControl = new SplineEditorControl();

        setId("SplineEditor");
        background.setId("Background");
        background.setManaged(false);
        background.setCache(true);
        getStylesheets().add(SplineEditor.class.getResource("SplineEditor.css").toExternalForm());
        setPadding(new Insets(10, 20, 10, 10));
        setVgap(10);
        setHgap(10);
        GridPane.setConstraints(splineEditorControl, 0, 0, 1, 10,
                 HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        final Label codeLabel = new Label();
        codeLabel.setId("CodeLabel");
        codeLabel.textProperty().bind(new StringBinding() {
            {
                bind(splineEditorControl.controlPoint1xProperty(),
                        splineEditorControl.controlPoint1yProperty(),
                        splineEditorControl.controlPoint2xProperty(),
                        splineEditorControl.controlPoint2yProperty());
            }

            @Override
            protected String computeValue() {
                return String.format("Interpolator.SPLINE(%.4f, %.4f, %.4f, %.4f);",
                        splineEditorControl.getControlPoint1x(),
                        splineEditorControl.getControlPoint1y(),
                        splineEditorControl.getControlPoint2x(),
                        splineEditorControl.getControlPoint2y());
            }
        });
        GridPane.setConstraints(codeLabel, 0, 10, 1, 1,
                 HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        Button copyButton = new Button("Copy Code");
        copyButton.getStyleClass().add("big-button");
        copyButton.setOnAction((ActionEvent t) -> {
            Clipboard.getSystemClipboard().setContent(
                    Collections.singletonMap(DataFormat.PLAIN_TEXT, (Object) codeLabel.getText()));
        });
        GridPane.setConstraints(copyButton, 1, 10, 1, 1,
                 HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);

        // preview
        Label previewLabel = new Label("Animation Preview");
        previewLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1.1em;");
        GridPane.setConstraints(previewLabel, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        // scale
        Label scaleLabel = new Label("Scale");
        GridPane.setConstraints(scaleLabel, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(scaleCircle, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        // rotate
        Label rotateLabel = new Label("Rotate");
        GridPane.setConstraints(rotateLabel, 1, 3, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(rotateSquare, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);
        // fade
        Label fadeLabel = new Label("Fade");
        GridPane.setConstraints(fadeLabel, 1, 5, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(fadeSquare, 1, 6, 1, 1, HPos.CENTER, VPos.CENTER);
        // linear
        Label linearLabel = new Label("Linear");
        GridPane.setConstraints(linearLabel, 1, 7, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(linearGroup, 1, 8, 1, 1, HPos.CENTER, VPos.CENTER);

        getColumnConstraints().addAll(
                new ColumnConstraints(300, USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true),
                new ColumnConstraints(200)
        );
        getRowConstraints().addAll(
                new RowConstraints(),
                new RowConstraints(), // scale
                new RowConstraints(120),
                new RowConstraints(), // rotate
                new RowConstraints(120),
                new RowConstraints(), // fade
                new RowConstraints(120),
                new RowConstraints(), // linear
                new RowConstraints(30),
                new RowConstraints(0, 0, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true), // spacer
                new RowConstraints() // code
        );

        getChildren().addAll(background, splineEditorControl, codeLabel, copyButton,
                previewLabel,
                scaleLabel, scaleCircle,
                rotateLabel, rotateSquare,
                fadeLabel, fadeSquare,
                linearLabel, linearGroup);

        // create anaimation updater
        ChangeListener<Number> animUpdater = (ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            updateAnimation();
        };
        splineEditorControl.controlPoint1xProperty().addListener(animUpdater);
        splineEditorControl.controlPoint1yProperty().addListener(animUpdater);
        splineEditorControl.controlPoint2xProperty().addListener(animUpdater);
        splineEditorControl.controlPoint2yProperty().addListener(animUpdater);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        background.resize(getWidth(), getHeight());
    }

    @Override
    public void startAnimations() {
        updateAnimation();
    }

    @Override
    public void stopAnimations() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void updateAnimation() {
        if (timeline != null) {
            timeline.stop();
        }
        Interpolator spline = Interpolator.SPLINE(
                splineEditorControl.getControlPoint1x(),
                splineEditorControl.getControlPoint1y(),
                splineEditorControl.getControlPoint2x(),
                splineEditorControl.getControlPoint2y());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().addAll(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(scaleCircle.scaleXProperty(), 0d, spline),
                        new KeyValue(scaleCircle.scaleYProperty(), 0d, spline),
                        new KeyValue(rotateSquare.rotateProperty(), 0d, spline),
                        new KeyValue(fadeSquare.opacityProperty(), 0d, spline),
                        new KeyValue(linearCircle.translateXProperty(), 0d, spline)
                ),
                new KeyFrame(
                        Duration.seconds(5),
                        new KeyValue(scaleCircle.scaleXProperty(), 1d, spline),
                        new KeyValue(scaleCircle.scaleYProperty(), 1d, spline),
                        new KeyValue(rotateSquare.rotateProperty(), 360d, spline),
                        new KeyValue(fadeSquare.opacityProperty(), 1d, spline),
                        new KeyValue(linearCircle.translateXProperty(), 180d, spline)
                )
        );
        timeline.play();
    }
}
