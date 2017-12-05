package fxexperienceplayer;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * A simple knob skin for slider
 * 
 * @author Jasper Potts
 */
public class KnobSkin extends SkinBase<Slider, KnobBehavior> {

    private double knobRadius;
    private double minAngle = -140;
    private double maxAngle = 140;
    private double dragOffset;

    private StackPane knob;
    private StackPane knobOverlay;
    private StackPane knobDot;

    public KnobSkin(Slider slider) {
        super(slider, new KnobBehavior(slider));
        initialize();
        requestLayout();
        registerChangeListener(slider.minProperty(), "MIN");
        registerChangeListener(slider.maxProperty(), "MAX");
        registerChangeListener(slider.valueProperty(), "VALUE");
    }

    private void initialize() {
        knob = new StackPane() {
            @Override protected void layoutChildren() {
                knobDot.autosize();
                knobDot.setLayoutX((knob.getWidth()-knobDot.getWidth())/2);
                knobDot.setLayoutY(5+(knobDot.getHeight()/2));
            }
            
        };
        knob.getStyleClass().setAll("knob");
        knobOverlay = new StackPane();
        knobOverlay.getStyleClass().setAll("knobOverlay");
        knobDot = new StackPane();
        knobDot.getStyleClass().setAll("knobDot");

        getChildren().setAll(knob, knobOverlay);
        knob.getChildren().add(knobDot);
        
        getSkinnable().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                double dragStart = mouseToValue(me.getX(), me.getY());
                double zeroOneValue = (getSkinnable().getValue() - getSkinnable().getMin()) / (getSkinnable().getMax() - getSkinnable().getMin());
                dragOffset = zeroOneValue - dragStart;
                getBehavior().knobPressed(me,dragStart);
            }
        });
        getSkinnable().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                getBehavior().knobRelease(me,mouseToValue(me.getX(), me.getY()));
            }
        });
        getSkinnable().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                getBehavior().knobDragged(me, mouseToValue(me.getX(), me.getY()) + dragOffset);
            }
        });
    }
    
    private double mouseToValue(double mouseX, double mouseY) {
        double cx = getSkinnable().getWidth()/2;
        double cy = getSkinnable().getHeight()/2;
        double mouseAngle = Math.toDegrees(Math.atan((mouseY-cy) / (mouseX-cx)));
        double topZeroAngle;
        if (mouseX<cx) {
            topZeroAngle = 90 - mouseAngle;
        } else {
            topZeroAngle = -(90 + mouseAngle);
        }
        double value = 1 - ((topZeroAngle - minAngle) / (maxAngle - minAngle));
        return value;
    }

    @Override protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);
        requestLayout();
    }

    void rotateKnob() {
        Slider s = getSkinnable();
        double zeroOneValue = (s.getValue()-s.getMin()) / (s.getMax() - s.getMin());
        double angle = minAngle + ((maxAngle-minAngle) * zeroOneValue);
        knob.setRotate(angle);
    }

    @Override protected void layoutChildren() {
        // calculate the available space
        double x = getInsets().getLeft();
        double y = getInsets().getTop();
        double w = getWidth() - (getInsets().getLeft() + getInsets().getRight());
        double h = getHeight() - (getInsets().getTop() + getInsets().getBottom());
        double cx = x+(w/2);
        double cy = y+(h/2);

        // resize thumb to preferred size
        double knobWidth = knob.prefWidth(-1);
        double knobHeight = knob.prefHeight(-1);
        knobRadius = Math.max(knobWidth, knobHeight)/2;
        knob.resize(knobWidth, knobHeight);
        knob.setLayoutX(cx-knobRadius);
        knob.setLayoutY(cy-knobRadius);
        knobOverlay.resize(knobWidth, knobHeight);
        knobOverlay.setLayoutX(cx-knobRadius);
        knobOverlay.setLayoutY(cy-knobRadius);
        rotateKnob();
    }
    
    @Override protected double computeMinWidth(double height) {
        return (getInsets().getLeft() + knob.minWidth(-1) + getInsets().getRight());
    }

    @Override protected double computeMinHeight(double width) {
        return(getInsets().getTop() + knob.minHeight(-1) + getInsets().getBottom());
    }

    @Override protected double computePrefWidth(double height) {
        return (getInsets().getLeft() + knob.prefWidth(-1) + getInsets().getRight());
    }

    @Override protected double computePrefHeight(double width) {
        return(getInsets().getTop() + knob.prefHeight(-1) + getInsets().getBottom());
    }

    @Override protected double computeMaxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override protected double computeMaxHeight(double width) {
        return Double.MAX_VALUE;
    }
}

